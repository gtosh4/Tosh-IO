package toshio.network

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.Material
import toshio.ToshIO
import toshio.connector.Cable
import toshio.connector.Connector
import toshio.util.DirectionMap

abstract class NetworkBlock : Block {
    var network: Network? = null

    constructor() : super(Properties.of(Material.GLASS))

    companion object {
        @JvmStatic val DOWN = BooleanProperty.create("down")
        @JvmStatic val UP = BooleanProperty.create("up")
        @JvmStatic val NORTH = BooleanProperty.create("north")
        @JvmStatic val SOUTH = BooleanProperty.create("south")
        @JvmStatic val WEST = BooleanProperty.create("west")
        @JvmStatic val EAST = BooleanProperty.create("east")

        val props = DirectionMap(DOWN, UP, NORTH, SOUTH, WEST, EAST)

        fun isConnectedBlock(b: Block): Boolean =
                when (b) {
                    is Cable -> true
                    is Connector -> true
                    else -> false
                }

        fun printArea(world: LevelAccessor, pos: BlockPos) {
            ToshIO.LOGGER.info(
                    "{} :: {} :: {}",
                    pos,
                    world.getBlockState(pos).block.name.string,
                    Direction.values().map { dir ->
                        "${dir}: ${world.getBlockState(pos.relative(dir)).block.name.string}"
                    }
            )
        }
    }

    init {
        registerDefaultState(
                stateDefinition.any().apply { props.values.forEach { setValue(it, false) } }
        )
    }

    fun testConnected(world: LevelAccessor, pos: BlockPos, facing: Direction): Boolean {
        val other = world.getBlockState(pos.relative(facing))
        if (!isConnectedBlock(other.block)) return false

        return true
    }

    override fun createBlockStateDefinition(
            builder: StateDefinition.Builder<Block, BlockState>
    ) {
        props.forEach { builder.add(it.value) }
    }

    private fun state(world: LevelAccessor, pos: BlockPos, oldState: BlockState?): BlockState {
        return defaultBlockState().let { initState ->
            props.entries.fold(initState) { state, prop ->
                state.setValue(prop.value, testConnected(world, pos, prop.key))
            }
        }
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return state(context.level, context.clickedPos, null)
    }

    override fun onPlace(
            state: BlockState,
            world: Level,
            pos: BlockPos,
            state2: BlockState,
            b: Boolean
    ) {}

    override fun neighborChanged(
            state: BlockState,
            world: Level,
            pos: BlockPos,
            block: Block,
            pos1: BlockPos,
            b: Boolean
    ) {
        super.neighborChanged(state, world, pos, block, pos1, b)

        val newState = state(world, pos, state)

        if (newState.properties.all { prop -> newState.getValue(prop) == state.getValue(prop) }) {
            return
        }

        world.setBlockAndUpdate(pos, newState)
    }
}

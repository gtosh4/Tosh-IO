package toshio.connector

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import toshio.ToshIO
import toshio.network.NetworkBlock

class Connector : NetworkBlock(), EntityBlock {
    override fun newBlockEntity(blockGetter: BlockGetter): BlockEntity = ConnectorEntity()

    @SuppressWarnings("deprecation")
    override fun use(
        blockState: BlockState?,
        level: Level?,
        blockPos: BlockPos?,
        player: Player?,
        interactionHand: InteractionHand?,
        blockHitResult: BlockHitResult?
    ): InteractionResult {
        val s = { super.use(blockState, level, blockPos, player, interactionHand, blockHitResult) }
        val entity = level?.getBlockEntity(blockPos) ?: return s()
        if (entity !is ConnectorEntity) return s()
        if (level.isClientSide) return InteractionResult.SUCCESS
        if (player is ServerPlayer) {
            ConnectorMenuProvider.open(player, entity, blockHitResult?.direction ?: Direction.UP)
            return InteractionResult.SUCCESS
        }
        return s()
    }

    override fun triggerEvent(blockState: BlockState?, level: Level, blockPos: BlockPos?, i: Int, j: Int): Boolean {
        super.triggerEvent(blockState, level, blockPos, i, j)
        val blockEntity = level.getBlockEntity(blockPos)
        return blockEntity?.triggerEvent(i, j) ?: false
    }

    override fun getMenuProvider(blockState: BlockState?, level: Level, blockPos: BlockPos?): MenuProvider? {
        val blockEntity = level.getBlockEntity(blockPos)
        return if (blockEntity is MenuProvider) blockEntity else null
    }
}

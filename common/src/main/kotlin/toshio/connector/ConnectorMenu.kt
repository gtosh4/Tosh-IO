package toshio.connector

import me.shedaniel.architectury.registry.MenuRegistry
import net.minecraft.core.Direction
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import toshio.ToshIO

class ConnectorMenu(i: Int, inventory: Inventory?, val connector: ConnectorEntity?, private var _direction: Direction) :
    AbstractContainerMenu(Registrations.menus.CONN.get(), i) {

    constructor(i: Int, inventory: Inventory?) : this(i, inventory, null, Direction.UP)

    var direction
        get() = _direction
        set(d) {
            _direction = d
            connector?.setChanged()
        }

    companion object {
        const val startX = 97
        const val startY = 21
        const val slotSize = 18 // pixels
        const val maxSlots = 12
    }

    init {
        if (connector != null) {
            for (idx in 0 until maxSlots) {
                val row = idx / 4
                val col = idx % 4
                addSlot(
                    ConnectorPartSlot(
                        this,
                        idx,
                        startX + col * slotSize,
                        startY + row * slotSize
                    )
                )
            }
        }

        for (l in (0 until 3)) {
            for (m in (0 until 9)) {
                val slot = m + l * 9 + 9
                addSlot(Slot(inventory, slot, 8 + m * slotSize, 84 + l * slotSize));
            }
        }

        for (l in (0 until 9)) {
            addSlot(Slot(inventory, l, 8 + l * slotSize, 142))
        }
    }

    override fun stillValid(player: Player?): Boolean = connector?.stillValid(player) ?: true

    override fun quickMoveStack(player: Player?, i: Int): ItemStack? {
        if (connector == null) return ItemStack.EMPTY

        val fromSlot = slots[i]
        if (fromSlot == null || !fromSlot.hasItem()) return ItemStack.EMPTY

        val fromItem = fromSlot.item
        var itemStack = fromItem.copy()

        if (i < maxSlots) { // connector -> player
            if (!moveItemStackTo(fromItem, maxSlots, slots.size, true)) {
                return ItemStack.EMPTY
            }
        } else { // player -> connector
            for (toIdx in 0 until connector.containerSize) {
                val toSlot = slots[toIdx]
                if (!toSlot.hasItem() && toSlot.mayPlace(fromItem)) {
                    toSlot.set(fromItem.split(1))
                    itemStack = ItemStack.EMPTY // only place one
                    break
                }
            }
        }
        if (fromItem.isEmpty) {
            fromSlot.set(ItemStack.EMPTY)
        } else {
            fromSlot.setChanged()
        }
        return itemStack
    }
}

class ConnectorMenuProvider(private val entity: ConnectorEntity, private val direction: Direction) : MenuProvider {
    override fun createMenu(i: Int, inventory: Inventory?, player: Player?): AbstractContainerMenu {
        return ConnectorMenu(i, inventory, entity, direction)
    }

    override fun getDisplayName(): Component = entity.displayName

    companion object {
        fun open(player: Player, entity: ConnectorEntity, direction: Direction) {
            if (player is ServerPlayer) {
                MenuRegistry.openExtendedMenu(player, ConnectorMenuProvider(entity, direction)) {
                    it.writeBlockPos(entity.blockPos)
                    it.writeEnum(direction)
                }
            }
        }

        fun create(i: Int, inventory: Inventory?, buf: FriendlyByteBuf?): ConnectorMenu {
            val pos = buf?.readBlockPos() ?: return ConnectorMenu(i, inventory)
            val entity = inventory?.player?.level?.getBlockEntity(pos) ?: return ConnectorMenu(i, inventory)
            val dir = buf.readEnum(Direction::class.java) ?: Direction.UP

            return when (entity) {
                is ConnectorEntity -> ConnectorMenu(i, inventory, entity, dir)
                else -> ConnectorMenu(i, inventory)
            }
        }
    }
}

package toshio.connector

import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.MenuProvider
import net.minecraft.world.Nameable
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.TickableBlockEntity
import net.minecraft.world.level.block.state.BlockState
import toshio.network.Network

class ConnectorEntity : BlockEntity(Registrations.entities.CONN.get()), TickableBlockEntity, Nameable, Container {
    val parts = ConnectorParts()
    private var network: Network? = null

    override fun load(blockState: BlockState, compoundTag: CompoundTag) {
        super.load(blockState, compoundTag)
        val tierLevel = compoundTag.getInt("Tier")
        parts.tier = ConnectorTier.values()[tierLevel]
        parts.entries.forEach { (dir, dirItems) ->
            dirItems.clear()
            val tag2 = compoundTag.getCompound(dir.getName())
            ContainerHelper.loadAllItems(tag2, dirItems)
        }
    }

    override fun save(compoundTag: CompoundTag): CompoundTag {
        super.save(compoundTag)
        compoundTag.putInt("Tier", parts.tier.ordinal)
        parts.entries.forEach { (dir, list) ->
            val tag2 = CompoundTag()
            ContainerHelper.saveAllItems(tag2, list)
            compoundTag.put(dir.getName(), tag2)
        }
        return compoundTag
    }

    override fun tick() {
        network?.tick(level)
    }

    override fun clearContent() = parts.clear()

    override fun getContainerSize(): Int = parts.capacity

    override fun isEmpty(): Boolean = parts.isEmpty()

    override fun getItem(i: Int): ItemStack = parts[i]

    fun getItem(dir: Direction, idx: Int): ItemStack = getItem(parts.makeIndex(dir, idx))

    override fun removeItem(i: Int, j: Int): ItemStack {
        val item = getItem(i)
        if (item.isEmpty) return ItemStack.EMPTY
        val result = item.split(j)
        if (!result.isEmpty) {
            setChanged()
        }
        return result
    }

    fun removeItem(dir: Direction, idx: Int, count: Int): ItemStack =
        removeItem(parts.makeIndex(dir, idx), count)

    override fun removeItemNoUpdate(i: Int): ItemStack =
        parts.set(i, ItemStack.EMPTY)

    override fun setItem(i: Int, itemStack: ItemStack) {
        parts[i] = itemStack
        if (itemStack.count > this.maxStackSize) {
            itemStack.count = this.maxStackSize
        }
        setChanged()
    }

    fun setItem(dir: Direction, idx: Int, itemStack: ItemStack) = setItem(parts.makeIndex(dir, idx), itemStack)

    override fun stillValid(player: Player?): Boolean = true

    override fun getName(): Component = TranslatableComponent("block.toshio.connector")

    override fun getDisplayName(): Component = name

    fun sideItems(side: Direction): NonNullList<ItemStack> = parts[side]!!
}

enum class ConnectorTier(val size: Int) {
    ENTRY(1),
    BASIC(2),
    ADVANCED(4),
    ULTIMATE(8)
}

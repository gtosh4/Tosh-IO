package toshio.connector

import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import toshio.part.Parts

class ConnectorPartSlot(private val menu: ConnectorMenu, private val dirSlot: Int, j: Int, k: Int) :
    Slot(null, -1, j, k) {

    override fun mayPlace(itemStack: ItemStack?): Boolean =
        itemStack?.item?.`is`(Parts.TAG) == true

    override fun getItem(): ItemStack {
        if (!isActive) return ItemStack.EMPTY
        return menu.connector?.getItem(menu.direction, dirSlot) ?: ItemStack.EMPTY
    }

    override fun set(itemStack: ItemStack) {
        if (!isActive) return
        menu.connector?.setItem(menu.direction, dirSlot, itemStack)
    }

    override fun setChanged() {
        menu.connector?.setChanged()
    }

    override fun remove(i: Int): ItemStack {
        if (!isActive) return ItemStack.EMPTY
        return menu.connector?.removeItem(menu.direction, dirSlot, i) ?: ItemStack.EMPTY
    }

    override fun isActive(): Boolean {
        val parts = menu.connector?.parts ?: return true
        return dirSlot < parts.tier.size
    }

    override fun getMaxStackSize(): Int = 1
}
package toshio.part

import net.minecraft.world.item.Item
import toshio.Register

object Parts {
    fun init() {}

    val INVENTORY = Register.item("part/inventory") { Inventory(Item.Properties().tab(Register.TAB)) }

    val TAG = Register.itemTag("part")
}
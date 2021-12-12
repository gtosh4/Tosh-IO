package toshio.part

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import toshio.connector.ConnectorEntity

abstract class Part(props: Properties) : Item(props) {
    abstract fun tick(itemStack: ItemStack, entity: ConnectorEntity)
}

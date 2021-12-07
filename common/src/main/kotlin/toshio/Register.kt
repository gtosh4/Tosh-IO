package toshio

import java.util.function.Supplier
import me.shedaniel.architectury.registry.CreativeTabs
import me.shedaniel.architectury.registry.DeferredRegister
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object Register {

    fun init() {
        items.register()
        blocks.register()
    }

    private val itemGroup =
            CreativeTabs.create(ToshIO.id("toshio_tab")) {
                null // TODO
            }

    private val items = DeferredRegister.create(ToshIO.MOD_ID, Registry.ITEM_REGISTRY)

    public fun item(id: ResourceLocation, supplier: Supplier<Item>): RegistrySupplier<Item> {
        return items.register(id, supplier)
    }

    private val blocks = DeferredRegister.create(ToshIO.MOD_ID, Registry.BLOCK_REGISTRY)

    public fun block(id: ResourceLocation, supplier: Supplier<Block>): RegistrySupplier<Block> {
        return blocks.register(id, supplier)
    }
}

package toshio

import dev.architectury.injectables.annotations.ExpectPlatform
import java.util.function.Supplier
import me.shedaniel.architectury.registry.CreativeTabs
import me.shedaniel.architectury.registry.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.Tag
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import toshio.connector.Registrations

object Register {

    fun init() {
        items.register()
        blocks.register()
        blockEntities.register()
        menus.register()
        ToshIO.LOGGER.info("Register init")
    }

    val TAB: CreativeModeTab =
        CreativeTabs.create(ToshIO.id("toshio_tab")) { ItemStack(Registrations.blocks.CABLE.get().asItem()) }

    private val items = DeferredRegister.create(ToshIO.MOD_ID, Registry.ITEM_REGISTRY)

    fun <T : Item> item(id: ResourceLocation, supplier: Supplier<T>) =
        items.register(id, supplier).also { ToshIO.LOGGER.info("Registered item {}", id) }

    fun <T : Item> item(id: String, supplier: Supplier<T>) = item(ToshIO.id(id), supplier)

    private val blocks = DeferredRegister.create(ToshIO.MOD_ID, Registry.BLOCK_REGISTRY)

    fun <T : Block> block(id: ResourceLocation, supplier: Supplier<T>) =
        blocks.register(id, supplier).also {
            item(id) { BlockItem(it.get(), Item.Properties().tab(TAB)) }
            ToshIO.LOGGER.info("Registered block {}", id)
        }

    fun <T : Block> block(id: String, supplier: Supplier<T>) = block(ToshIO.id(id), supplier)

    private val blockEntities =
        DeferredRegister.create(ToshIO.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY)

    fun <T : BlockEntityType<*>> blockEntity(id: ResourceLocation, supplier: Supplier<T>) =
        blockEntities.register(id, supplier).also {
            ToshIO.LOGGER.info("Registered block entity {}", id)
        }

    fun <T : BlockEntityType<*>> blockEntity(id: String, supplier: Supplier<T>) =
        blockEntity(ToshIO.id(id), supplier)

    private val menus =
        DeferredRegister.create(ToshIO.MOD_ID, Registry.MENU_REGISTRY)

    fun <T : MenuType<*>> menu(id: ResourceLocation, supplier: Supplier<T>) =
        menus.register(id, supplier)

    fun <T : MenuType<*>> menu(id: String, supplier: Supplier<T>) =
        menu(ToshIO.id(id), supplier)

    @ExpectPlatform
    @JvmStatic
    fun itemTag(id: ResourceLocation): Tag.Named<Item> = TODO("ExpectPlatform")

    fun itemTag(id: String): Tag.Named<Item> = itemTag(ToshIO.id(id))
}

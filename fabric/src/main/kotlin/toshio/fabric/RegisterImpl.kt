package toshio.fabric;

import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.Tag
import net.minecraft.world.item.Item

class RegisterImpl {
    companion object {
        @JvmStatic
        fun itemTag(id: ResourceLocation): Tag.Named<Item> {
            return TagRegistry.item(id) as Tag.Named<Item>
        }
    }
}
package toshio.forge

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.Tag
import net.minecraft.world.item.Item

class RegisterImpl {
    companion object {
        @JvmStatic
        fun itemTag(id: ResourceLocation): Tag.Named<Item> {
            return ItemTags.createOptional(id)
        }
    }
}
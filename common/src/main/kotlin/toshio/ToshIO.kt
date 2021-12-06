package toshio

import me.shedaniel.architectury.registry.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.LazyLoadedValue
import org.apache.logging.log4j.LogManager

object ToshIO {
    const val MOD_ID = "toshio"

    val LOGGER = LogManager.getLogger("ToshIO")

    val REGISTRIES = LazyLoadedValue { Registries.get(MOD_ID) }

    fun init() {}

    fun id(path: String): ResourceLocation {
        return ResourceLocation(MOD_ID, path)
    }
}

package toshio

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import toshio.connector.*
import toshio.part.Parts

object ToshIO {
    const val MOD_ID = "toshio"

    val LOGGER = LogManager.getLogger("ToshIO")

    fun init() {
        Register.init()
        Registrations.init()
        Parts.init()
    }

    fun id(path: String): ResourceLocation {
        return ResourceLocation(MOD_ID, path)
    }
}

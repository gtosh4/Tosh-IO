package toshio

import me.shedaniel.architectury.registry.Registries

object ToshIO {
    const val MOD_ID = "toshio"
    
    val LOGGER = LogManager.getLogger("ToshIO")
    
    val REGISTRIES = LazyLoadedValue{ Registries.get(MOD_ID) }

    fun id(path: String): Identifier {
        return Identifier(MOD_ID, path)
    }
}

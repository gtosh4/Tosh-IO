package toshio.fabric

import net.fabricmc.api.ModInitializer
import toshio.ToshIO

class ToshIOFabric : ModInitializer {
    override fun onInitialize() {
        ToshIO.init()
    }
}

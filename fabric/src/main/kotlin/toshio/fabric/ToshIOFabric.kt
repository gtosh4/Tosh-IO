package toshio.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import toshio.ToshIO
import toshio.connector.Registrations

class ToshIOFabric : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        ToshIO.init()
    }

    override fun onInitializeClient() {
        Registrations.initClient()
    }
}

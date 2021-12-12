package toshio.forge

import me.shedaniel.architectury.platform.forge.EventBuses
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT
import toshio.ToshIO
import toshio.connector.Registrations

@Mod(ToshIO.MOD_ID)
class ToshIOForge {
    init {
        val bus = MOD_CONTEXT.getKEventBus()
        EventBuses.registerModEventBus(ToshIO.MOD_ID, bus)
        ToshIO.init()
        ToshIO.LOGGER.info("init complete")
        bus.register(this)
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    fun registerClient(event: FMLClientSetupEvent) {
        ToshIO.LOGGER.info("init client")
        Registrations.initClient()
    }
}

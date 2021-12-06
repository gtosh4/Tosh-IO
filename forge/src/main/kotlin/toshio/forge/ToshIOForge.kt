package toshio.forge

import me.shedaniel.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT
import toshio.ToshIO

@Mod(ToshIO.MOD_ID)
class ToshIOForge {
    init {
        EventBuses.registerModEventBus(ToshIO.MOD_ID, MOD_CONTEXT.getKEventBus())
        ToshIO.init()
    }
}

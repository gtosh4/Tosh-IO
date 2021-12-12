package toshio.connector

import me.shedaniel.architectury.registry.MenuRegistry
import net.minecraft.world.level.block.entity.BlockEntityType
import toshio.Register

object Registrations {
    fun init() {
        blocks.init()
        entities.init()
        menus.init()
    }

    fun initClient() {
        MenuRegistry.registerScreenFactory(menus.CONN.get(), ::ConnectorScreen)
    }

    object blocks {
        fun init() {}

        val CABLE = Register.block("cable") { Cable() }
        val CONN = Register.block("connector") { Connector() }
    }

    object entities {
        fun init() {}

        val CONN = Register.blockEntity("connector") {
            BlockEntityType(::ConnectorEntity, setOf(blocks.CONN.get()), null)
        }
    }

    object menus {
        fun init() {}

        val CONN= Register.menu("connector") {
            MenuRegistry.ofExtended(ConnectorMenuProvider::create)
        }
    }
}

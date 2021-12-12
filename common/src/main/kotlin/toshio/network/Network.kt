package toshio.network

import net.minecraft.world.level.Level
import toshio.connector.Cable
import java.util.concurrent.ConcurrentHashMap
import toshio.connector.ConnectorEntity
import toshio.part.Part
import java.util.function.Predicate

class Network {
    private val cables = ConcurrentHashMap.newKeySet<Cable>()
    private val connectors = ConcurrentHashMap.newKeySet<ConnectorEntity>()
    private var lastTick: Long = 0

    fun addCable(cable: Cable) = cables.add(cable)
    fun removeCable(cable: Cable) = cables.remove(cable)
    fun removeCable(predicate: Predicate<Cable>) = cables.removeIf(predicate)

    fun addConnector(connector: ConnectorEntity) = connectors.add(connector)
    fun removeConnector(connector: ConnectorEntity) = connectors.remove(connector)
    fun removeConnector(predicate: Predicate<ConnectorEntity>) = connectors.removeIf(predicate)

    operator fun plus(other: Network): Network {
        val net = Network()
        net.cables.addAll(other.cables)
        net.cables.addAll(this.cables)
        net.connectors.addAll(other.connectors)
        net.connectors.addAll(this.connectors)
        return net
    }

    operator fun plus(cable: Cable) = this.also { addCable(cable) }
    operator fun plus(connector: ConnectorEntity) = this.also { addConnector(connector) }

    fun tick(level: Level?) {
        if (level?.isClientSide != false) return
        if (lastTick == level.gameTime) return
        lastTick = level.gameTime
    }
}

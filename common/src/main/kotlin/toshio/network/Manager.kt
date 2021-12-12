package toshio.network

import java.util.concurrent.ConcurrentHashMap

object Manager {
    val networks = ConcurrentHashMap.newKeySet<Network>()
}

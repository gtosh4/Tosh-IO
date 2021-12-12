package toshio.connector

import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import toshio.util.DirectionMap

class ConnectorParts : DirectionMap<NonNullList<ItemStack>> {
    private var _tier: ConnectorTier

    constructor() : this(ConnectorTier.ENTRY)

    constructor(tier: ConnectorTier) : super({ NonNullList.withSize(tier.size, ItemStack.EMPTY) }) {
        _tier = tier
    }

    var tier: ConnectorTier
        get() = _tier
        set(next)  {
            if (next.size <= tier.size) return

            val pad = (0 .. (next.size - tier.size)).map { ItemStack.EMPTY }
            values.forEach { items ->
                items.addAll(pad)
            }
            _tier = next
        }

    val capacity get() = super.size * tier.size

    override fun clear() = values.forEach { it.clear() }
    override fun isEmpty() = values.all { it.all(ItemStack::isEmpty) }

    fun splitIndex(index: Int): Pair<Direction, Int> {
        if (index >= capacity) throw IndexOutOfBoundsException("Index $index greater than capacity $capacity")
        if (index < 0) throw IndexOutOfBoundsException("Negative index $index")

        val d = index/tier.size
        val idx = index % tier.size
        return Pair(Direction.from3DDataValue(d), idx)
    }

    fun makeIndex(dir: Direction, idx: Int): Int = dir.get3DDataValue() * tier.size + idx

    operator fun get(index: Int): ItemStack {
        val (dir, idx) = splitIndex(index)
        return this[dir]?.get(idx) ?: throw IndexOutOfBoundsException("Index $index ($dir, $idx) out of bounds")
    }

    operator fun set(index: Int, element: ItemStack): ItemStack {
        val (dir, idx) = splitIndex(index)
        return this[dir]?.set(idx, element) ?: throw IndexOutOfBoundsException("Index $index ($dir, $idx) out of bounds")
    }
}
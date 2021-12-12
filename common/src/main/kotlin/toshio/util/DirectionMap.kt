package toshio.util

import java.util.EnumMap
import net.minecraft.core.Direction
import java.util.function.Supplier

open class DirectionMap<T> : EnumMap<Direction, T> {
    constructor() : super(Direction::class.javaObjectType)

    constructor(
            down: T,
            up: T,
            north: T,
            south: T,
            west: T,
            east: T
    ) : super(
            mapOf(
                    Direction.DOWN to down,
                    Direction.UP to up,
                    Direction.NORTH to north,
                    Direction.SOUTH to south,
                    Direction.WEST to west,
                    Direction.EAST to east,
            )
    )

    constructor(init: Supplier<T>) : this(init.get(), init.get(), init.get(), init.get(), init.get(), init.get())
}

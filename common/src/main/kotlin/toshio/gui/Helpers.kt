package toshio.gui

data class Position(val x: Int, val y: Int) {
    operator fun plus(add: Position): Position = Position(x+add.x, y+add.y)
}

data class Size(val w: Int, val h: Int)
package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)

open class SquareBoardImpl(override val width: Int): SquareBoard {

    var board = arrayOf<Array<Cell>>()

    init {
        makeBoard(width)
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (i > width || j > width || i <= 0 || j <= 0)
            null
        else
            board[i-1][j-1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        return board[i-1][j-1]
    }

    override fun getAllCells(): Collection<Cell> {
        val allCells = mutableListOf<Cell>()

        for (i in 0 until width) {
            for (j in 0 until width) {
                allCells.add(board[i][j])
            }
        }

        return allCells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val row = mutableListOf<Cell>()

        for (j in jRange) {
            if (j > width) break
            row.add(board[i-1][j-1])
        }
        return row
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val column = mutableListOf<Cell>()

        for (i in iRange) {
            if (i > width) break
            column.add(board[i-1][j-1])
        }
        return column
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        var i = this.i
        var j = this.j

        when (direction) {
            UP -> i--
            DOWN -> i++
            LEFT -> j--
            RIGHT -> j++
        }

        return getCellOrNull(i, j)
    }

    fun makeBoard(width: Int) {
        for (i in 1..width) {
            var row = arrayOf<Cell>()
            for (j in 1..width) {
                row += Cell(i, j)
            }
            board += row
        }
    }

}

class GameBoardImpl<T>(override val width: Int): GameBoard<T>, SquareBoardImpl(width) {
    private val boardMap = mutableMapOf<Cell, T?>()

    init {
        super.makeBoard(width)
        getAllCells().forEach{ boardMap[it] = null}
    }

    override fun get(cell: Cell): T? {
        return boardMap.getOrDefault(cell, null)
    }

    override fun set(cell: Cell, value: T?) {
        boardMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return boardMap.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return boardMap.filterValues(predicate).keys.firstOrNull()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return boardMap.filterValues(predicate).keys.isNotEmpty()
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return boardMap.filterValues(predicate).size == boardMap.size
    }

}
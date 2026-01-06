package com.tictactoe

/**
 * Represents the state of a cell on the board
 */
enum class CellState {
    EMPTY,
    X,
    O
}

/**
 * Represents the game result
 */
sealed class GameResult {
    object Ongoing : GameResult()
    data class Win(val winner: CellState, val winningCells: List<Pair<Int, Int>>) : GameResult()
    object Draw : GameResult()
}

/**
 * Manages the game state and logic for Tic Tac Toe
 */
class GameLogic {
    private val board = Array(3) { Array(3) { CellState.EMPTY } }
    private var currentPlayer = CellState.X
    private var gameOver = false

    /**
     * Get the current state of the board
     */
    fun getBoard(): Array<Array<CellState>> {
        return board.map { it.clone() }.toTypedArray()
    }

    /**
     * Get the current player
     */
    fun getCurrentPlayer(): CellState = currentPlayer

    /**
     * Check if the game is over
     */
    fun isGameOver(): Boolean = gameOver

    /**
     * Make a move at the specified position
     * @param row Row index (0-2)
     * @param col Column index (0-2)
     * @return true if the move was successful, false otherwise
     */
    fun makeMove(row: Int, col: Int): Boolean {
        if (gameOver || row !in 0..2 || col !in 0..2 || board[row][col] != CellState.EMPTY) {
            return false
        }

        board[row][col] = currentPlayer
        val result = checkGameResult()

        if (result is GameResult.Ongoing) {
            currentPlayer = if (currentPlayer == CellState.X) CellState.O else CellState.X
        } else {
            gameOver = true
        }

        return true
    }

    /**
     * Check the current game result
     */
    fun checkGameResult(): GameResult {
        // Check rows
        for (row in 0..2) {
            if (board[row][0] != CellState.EMPTY &&
                board[row][0] == board[row][1] &&
                board[row][1] == board[row][2]
            ) {
                return GameResult.Win(board[row][0], listOf(Pair(row, 0), Pair(row, 1), Pair(row, 2)))
            }
        }

        // Check columns
        for (col in 0..2) {
            if (board[0][col] != CellState.EMPTY &&
                board[0][col] == board[1][col] &&
                board[1][col] == board[2][col]
            ) {
                return GameResult.Win(board[0][col], listOf(Pair(0, col), Pair(1, col), Pair(2, col)))
            }
        }

        // Check main diagonal
        if (board[0][0] != CellState.EMPTY &&
            board[0][0] == board[1][1] &&
            board[1][1] == board[2][2]
        ) {
            return GameResult.Win(board[0][0], listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)))
        }

        // Check anti-diagonal
        if (board[0][2] != CellState.EMPTY &&
            board[0][2] == board[1][1] &&
            board[1][1] == board[2][0]
        ) {
            return GameResult.Win(board[0][2], listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0)))
        }

        // Check for draw
        var hasEmpty = false
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == CellState.EMPTY) {
                    hasEmpty = true
                    break
                }
            }
            if (hasEmpty) break
        }

        return if (hasEmpty) {
            GameResult.Ongoing
        } else {
            GameResult.Draw
        }
    }

    /**
     * Reset the game to initial state
     */
    fun reset() {
        for (row in 0..2) {
            for (col in 0..2) {
                board[row][col] = CellState.EMPTY
            }
        }
        currentPlayer = CellState.X
        gameOver = false
    }

    /**
     * Get available moves
     */
    fun getAvailableMoves(): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == CellState.EMPTY) {
                    moves.add(Pair(row, col))
                }
            }
        }
        return moves
    }

    /**
     * Create a deep copy of the game logic
     */
    fun copy(): GameLogic {
        val copy = GameLogic()
        for (row in 0..2) {
            for (col in 0..2) {
                copy.board[row][col] = this.board[row][col]
            }
        }
        copy.currentPlayer = this.currentPlayer
        copy.gameOver = this.gameOver
        return copy
    }
}

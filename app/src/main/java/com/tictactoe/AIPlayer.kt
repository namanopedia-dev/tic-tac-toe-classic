package com.tictactoe

/**
 * AI difficulty levels
 */
enum class AIDifficulty {
    EASY,    // Random moves
    MEDIUM,  // Mix of random and optimal
    HARD     // Minimax algorithm (optimal)
}

/**
 * AI Player implementation using Minimax algorithm
 */
class AIPlayer(private val difficulty: AIDifficulty = AIDifficulty.HARD) {
    
    /**
     * Get the best move for the AI player
     * @param gameLogic Current game state
     * @param aiPlayer The cell state the AI is playing as (X or O)
     * @return Pair of (row, col) for the best move, or null if no moves available
     */
    fun getBestMove(gameLogic: GameLogic, aiPlayer: CellState): Pair<Int, Int>? {
        val availableMoves = gameLogic.getAvailableMoves()
        if (availableMoves.isEmpty()) {
            return null
        }

        return when (difficulty) {
            AIDifficulty.EASY -> {
                // Random move
                availableMoves.random()
            }
            AIDifficulty.MEDIUM -> {
                // 50% chance of optimal move, 50% random
                if (kotlin.random.Random.nextBoolean()) {
                    minimaxMove(gameLogic, aiPlayer)
                } else {
                    availableMoves.random()
                }
            }
            AIDifficulty.HARD -> {
                // Always optimal move using minimax
                minimaxMove(gameLogic, aiPlayer)
            }
        }
    }

    /**
     * Get the best move using minimax algorithm
     */
    private fun minimaxMove(gameLogic: GameLogic, aiPlayer: CellState): Pair<Int, Int> {
        val availableMoves = gameLogic.getAvailableMoves()
        var bestMove = availableMoves[0]
        var bestScore = Int.MIN_VALUE

        for (move in availableMoves) {
            // Create a copy of the game state
            val testGame = gameLogic.copy()
            
            // Make the move
            val row = move.first
            val col = move.second
            testGame.makeMove(row, col)
            
            // Evaluate the move
            val score = minimax(testGame, 0, false, aiPlayer)
            
            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return bestMove
    }

    /**
     * Minimax algorithm implementation
     * @param gameLogic Current game state
     * @param depth Current depth in the game tree
     * @param isMaximizing Whether we're maximizing (AI) or minimizing (human)
     * @param aiPlayer The cell state the AI is playing as
     * @return Score of the position
     */
    private fun minimax(
        gameLogic: GameLogic,
        depth: Int,
        isMaximizing: Boolean,
        aiPlayer: CellState
    ): Int {
        val result = gameLogic.checkGameResult()

        when (result) {
            is GameResult.Win -> {
                return if (result.winner == aiPlayer) {
                    10 - depth  // AI wins (prefer shorter paths)
                } else {
                    depth - 10  // Human wins (prefer longer paths)
                }
            }
            is GameResult.Draw -> {
                return 0
            }
            is GameResult.Ongoing -> {
                // Continue searching
            }
        }

        val availableMoves = gameLogic.getAvailableMoves()
        
        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (move in availableMoves) {
                val testGame = gameLogic.copy()
                testGame.makeMove(move.first, move.second)
                val score = minimax(testGame, depth + 1, false, aiPlayer)
                bestScore = maxOf(bestScore, score)
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (move in availableMoves) {
                val testGame = gameLogic.copy()
                testGame.makeMove(move.first, move.second)
                val score = minimax(testGame, depth + 1, true, aiPlayer)
                bestScore = minOf(bestScore, score)
            }
            return bestScore
        }
    }
}

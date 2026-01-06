package com.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tictactoe.databinding.ActivityGameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameActivity : AppCompatActivity() {
    
    companion object {
        const val EXTRA_GAME_MODE = "game_mode"
        const val GAME_MODE_TWO_PLAYER = "two_player"
        const val GAME_MODE_AI = "ai"
    }
    
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameLogic: GameLogic
    private var adManager: AdManager? = null
    private var aiPlayer: AIPlayer? = null
    private var isAIMode = false
    private var isAITurn = false
    
    private val activityScope = CoroutineScope(Dispatchers.Main + Job())
    private val cellButtons = mutableListOf<Button>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        
        // Initialize game
        gameLogic = GameLogic()
        try {
            adManager = AdManager(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Get game mode
        val gameMode = intent.getStringExtra(EXTRA_GAME_MODE) ?: GAME_MODE_TWO_PLAYER
        isAIMode = gameMode == GAME_MODE_AI
        
        if (isAIMode) {
            // AI plays as O, human plays as X
            aiPlayer = AIPlayer(AIDifficulty.HARD)
        }
        
        setupBoard()
        updateUI()
        
        binding.resetButton.setOnClickListener {
            resetGame()
        }
        
        binding.playAgainButton.setOnClickListener {
            resetGame()
        }
        
        binding.backToMenuButton.setOnClickListener {
            finish()
        }
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        
        // Update toolbar title based on game mode
        val gameMode = intent.getStringExtra(EXTRA_GAME_MODE) ?: GAME_MODE_TWO_PLAYER
        val title = if (gameMode == GAME_MODE_AI) {
            "Play vs AI"
        } else {
            "Two Player"
        }
        supportActionBar?.title = title
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupBoard() {
        cellButtons.clear()
        cellButtons.add(binding.cell00)
        cellButtons.add(binding.cell01)
        cellButtons.add(binding.cell02)
        cellButtons.add(binding.cell10)
        cellButtons.add(binding.cell11)
        cellButtons.add(binding.cell12)
        cellButtons.add(binding.cell20)
        cellButtons.add(binding.cell21)
        cellButtons.add(binding.cell22)
        
        for (i in cellButtons.indices) {
            val row = i / 3
            val col = i % 3
            cellButtons[i].setOnClickListener {
                if (!isAITurn && !gameLogic.isGameOver()) {
                    makeMove(row, col)
                }
            }
        }
    }
    
    private fun makeMove(row: Int, col: Int) {
        if (gameLogic.makeMove(row, col)) {
            // Add visual feedback animation
            val buttonIndex = row * 3 + col
            if (buttonIndex < cellButtons.size) {
                val button = cellButtons[buttonIndex]
                button.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(100)
                    .withEndAction {
                        button.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .start()
                    }
                    .start()
            }
            
            updateUI()
            if (!checkGameResult()) {
                // If AI mode and game not over, let AI make a move
                if (isAIMode && gameLogic.getCurrentPlayer() == CellState.O) {
                    isAITurn = true
                    disableBoard()
                    updateStatusText("AI is thinking...")
                    
                    // Add a small delay for better UX
                    Handler(Looper.getMainLooper()).postDelayed({
                        makeAIMove()
                    }, 500)
                }
            }
        }
    }
    
    private fun makeAIMove() {
        if (gameLogic.isGameOver()) {
            isAITurn = false
            enableBoard()
            return
        }
        
        // Run AI calculation in background
        activityScope.launch {
            val aiMove = withContext(Dispatchers.Default) {
                aiPlayer?.getBestMove(gameLogic, CellState.O)
            }
            
            aiMove?.let { (row, col) ->
                if (gameLogic.makeMove(row, col)) {
                    // Add visual feedback for AI move
                    val buttonIndex = row * 3 + col
                    if (buttonIndex < cellButtons.size) {
                        val button = cellButtons[buttonIndex]
                        button.animate()
                            .scaleX(0.8f)
                            .scaleY(0.8f)
                            .setDuration(100)
                            .withEndAction {
                                button.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(100)
                                    .start()
                            }
                            .start()
                    }
                }
            }
            isAITurn = false
            updateUI()
            checkGameResult()
            enableBoard()
        }
    }
    
    private fun updateUI() {
        val board = gameLogic.getBoard()
        
        for (i in cellButtons.indices) {
            val row = i / 3
            val col = i % 3
            val cellState = board[row][col]
            val button = cellButtons[i]
            
            // Update content description for accessibility
            when (cellState) {
                CellState.EMPTY -> {
                    button.text = ""
                    button.isEnabled = !gameLogic.isGameOver() && !isAITurn
                    button.alpha = if (button.isEnabled) 1.0f else 0.6f
                    button.contentDescription = getString(R.string.cell_empty, row + 1, col + 1)
                }
                CellState.X -> {
                    button.text = "X"
                    button.setTextColor(ContextCompat.getColor(this, R.color.player_x_color))
                    button.isEnabled = false
                    button.alpha = 1.0f
                    button.contentDescription = getString(R.string.cell_x, row + 1, col + 1)
                }
                CellState.O -> {
                    button.text = "O"
                    button.setTextColor(ContextCompat.getColor(this, R.color.player_o_color))
                    button.isEnabled = false
                    button.alpha = 1.0f
                    button.contentDescription = getString(R.string.cell_o, row + 1, col + 1)
                }
            }
        }
        
        // Update current player text
        if (!gameLogic.isGameOver() && !isAITurn) {
            val currentPlayer = gameLogic.getCurrentPlayer()
            val playerText = if (currentPlayer == CellState.X) {
                getString(R.string.player_x)
            } else {
                getString(R.string.player_o)
            }
            updateStatusText(getString(R.string.current_player, playerText))
        }
    }
    
    private fun updateStatusText(text: String) {
        binding.currentPlayerText.text = text
    }
    
    private fun checkGameResult(): Boolean {
        val result = gameLogic.checkGameResult()
        
        return when (result) {
            is GameResult.Win -> {
                disableBoard()
                
                // Highlight winning cells
                for ((row, col) in result.winningCells) {
                    val index = row * 3 + col
                    if (index < cellButtons.size) {
                        cellButtons[index].animate()
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .setDuration(300)
                            .withEndAction {
                                cellButtons[index].animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(300)
                                    .start()
                            }
                            .start()
                    }
                }
                
                Handler(Looper.getMainLooper()).postDelayed({
                    val winnerText = if (result.winner == CellState.X) {
                        getString(R.string.player_x_wins)
                    } else {
                        getString(R.string.player_o_wins)
                    }
                    val title = if (isAIMode) {
                        if (result.winner == CellState.X) getString(R.string.victory_title) else getString(R.string.defeat_title)
                    } else {
                        getString(R.string.victory_title)
                    }
                    
                    val isDefeat = isAIMode && result.winner == CellState.O
                    
                    showGameResult(title, winnerText, isVictory = !isDefeat, isDraw = false, isDefeat = isDefeat)
                    
                    // Show ad after game ends
                    adManager?.showInterstitialAd()
                }, 1000)
                
                true
            }
            is GameResult.Draw -> {
                disableBoard()
                showGameResult(getString(R.string.draw_title), getString(R.string.draw), isVictory = false, isDraw = true)
                // Show ad after game ends
                adManager?.showInterstitialAd()
                true
            }
            is GameResult.Ongoing -> {
                false
            }
        }
    }
    
    private fun showGameResult(title: String, message: String, isVictory: Boolean, isDraw: Boolean, isDefeat: Boolean = false) {
        // Set text
        binding.resultTitle.text = title
        binding.resultMessage.text = message
        
        // Set colors
        val colorRes = when {
            isDraw -> R.color.color_draw
            isDefeat -> R.color.color_defeat
            else -> R.color.color_victory
        }
        val color = ContextCompat.getColor(this, colorRes)
        binding.resultTitle.setTextColor(color)
        
        // Show overlay
        binding.resultOverlay.visibility = View.VISIBLE
        binding.resultOverlay.alpha = 0f
        binding.resultOverlay.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
            
        // Animate Title (Scale up/Overshoot)
        binding.resultTitle.scaleX = 0f
        binding.resultTitle.scaleY = 0f
        binding.resultTitle.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .setDuration(500)
            .setStartDelay(200)
            .withEndAction {
                 binding.resultTitle.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
            
        // Animate Message
        binding.resultMessage.alpha = 0f
        binding.resultMessage.translationY = 50f
        binding.resultMessage.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(400)
            .setStartDelay(300)
            .start()
            
        // Animate Buttons
        binding.playAgainButton.translationY = 100f
        binding.playAgainButton.alpha = 0f
        binding.playAgainButton.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(400)
            .setStartDelay(500)
            .start()
            
        binding.backToMenuButton.translationY = 100f
        binding.backToMenuButton.alpha = 0f
        binding.backToMenuButton.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(400)
            .setStartDelay(600)
            .start()
    }
    
    private fun resetGame() {
        gameLogic.reset()
        isAITurn = false
        updateUI()
        enableBoard()
        
        // Hide overlay
        binding.resultOverlay.visibility = View.GONE
        binding.resultOverlay.alpha = 0f
    }
    
    private fun disableBoard() {
        for (button in cellButtons) {
            button.isEnabled = false
        }
    }
    
    private fun enableBoard() {
        if (!gameLogic.isGameOver() && !isAITurn) {
            for (button in cellButtons) {
                val index = cellButtons.indexOf(button)
                val row = index / 3
                val col = index % 3
                val board = gameLogic.getBoard()
                button.isEnabled = board[row][col] == CellState.EMPTY
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }
}

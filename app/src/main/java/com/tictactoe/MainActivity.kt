package com.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupClickListeners()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }
    
    private fun setupClickListeners() {
        binding.twoPlayerButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameActivity.EXTRA_GAME_MODE, GameActivity.GAME_MODE_TWO_PLAYER)
            startActivity(intent)
        }
        
        binding.playVsAIButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameActivity.EXTRA_GAME_MODE, GameActivity.GAME_MODE_AI)
            startActivity(intent)
        }
    }
}


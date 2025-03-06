package app.skilldiscounts

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class Game : AppCompatActivity() {
    private lateinit var bird: ImageView
    private lateinit var scoreText: TextView
    private lateinit var gameOverText: TextView
    private lateinit var pointsMessage: TextView
    private lateinit var backButton: Button

    private var birdY = 0f
    private var velocity = 0f
    private val gravity = 3f
    private var score = 0
    private var isGameOver = false

    private lateinit var pipeTop: Pipe
    private lateinit var pipeBottom: Pipe

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize pipes
        pipeTop = Pipe(this, true)
        pipeBottom = Pipe(this, false)

        // Get the main layout and add pipes
        val layout = findViewById<ConstraintLayout>(R.id.gameLayout)
        layout.addView(pipeTop)
        layout.addView(pipeBottom)

        pipeTop.z = 0f
        pipeBottom.z = 0f

        bird = findViewById(R.id.bird)
        scoreText = findViewById(R.id.points)
        gameOverText = findViewById(R.id.game_over_message)
        pointsMessage = findViewById(R.id.points_message)
        backButton = findViewById(R.id.back_button)

        // Set Z-order for UI elements
        bird.z = 10f
        scoreText.z = 20f
        gameOverText.z = 20f
        pointsMessage.z = 20f
        backButton.z = 20f

        // Bring UI elements to front
        scoreText.bringToFront()
        gameOverText.bringToFront()
        pointsMessage.bringToFront()
        backButton.bringToFront()

        // Start game loop
        startGameLoop()

        // Tap to jump
        bird.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && !isGameOver) {
                velocity = -25f // Jump effect
                bird.performClick()
            }
            true
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, BusinessList::class.java))
        }
    }

    private fun startGameLoop() {
        handler.postDelayed({
            handler.post(object : Runnable {
                override fun run() {
                    if (!isGameOver) {
                        updateBird()
                        pipeTop.movePipe()
                        pipeBottom.movePipe()
                        checkCollision()
                        handler.postDelayed(this, 30)
                    }
                }
            })
        }, 2000)
    }

    private fun updateBird() {
        velocity += gravity
        birdY += velocity
        bird.translationY = birdY

        if (birdY > 1500 || birdY < 0) {
            gameOver()
        }
    }

    private fun checkCollision() {
        val birdRect = Rect()
        bird.getHitRect(birdRect)

        val pipeTopRect = pipeTop.getBounds()
        val pipeBottomRect = pipeBottom.getBounds()

        // Collision detection for top and bottom pipes
        if (Rect.intersects(birdRect, pipeTopRect) || Rect.intersects(birdRect, pipeBottomRect)) {
            gameOver()
        }

        val birdTopY = birdY  // Bird's top position
        val bottomPipeBottomY = pipeBottom.height + 300f  // Bottom-most Y position of the bottom pipe

        // If the bird's top is below the bottom of the bottom pipe, end the game
        if (birdTopY > bottomPipeBottomY) {
            gameOver()
        }

        // Scoring: When the bird passes through the pipes
        if (pipeTop.x + pipeTop.width < bird.x && !pipeTop.hasScored) {
            score++
            pipeTop.hasScored = true
            scoreText.text = "Points: $score"
        }
    }


    private fun gameOver() {
        isGameOver = true
        gameOverText.visibility = TextView.VISIBLE
        pointsMessage.visibility = TextView.VISIBLE
        pointsMessage.text = "You got $score points!"
    }
}

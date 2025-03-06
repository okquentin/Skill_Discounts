package app.skilldiscounts

import android.content.Intent
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

    private var velocity = 0f
    private val gravity = 3f
    private var score = 0
    private var isGameOver = false
    private val gap = 400f

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

        // Coordinate pipe positions
        coordinatePipes()

        // Start game loop
        startGameLoop()

        layout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && !isGameOver) {
                velocity = -25f // Jump effect
            }
            true
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, BusinessList::class.java))
        }
    }

    private fun coordinatePipes() {
        // Ensure pipes have same x-position
        pipeBottom.pipeX = pipeTop.pipeX
        resetPipes()
    }

    private fun startGameLoop() {
        handler.postDelayed({
            handler.post(object : Runnable {
                override fun run() {
                    if (!isGameOver) {
                        updateBird()

                        // Move pipes and coordinate them
                        pipeTop.movePipe()

                        // Ensure bottom pipe moves with top pipe
                        pipeBottom.pipeX = pipeTop.pipeX

                        // If pipes need to be reset (moved past screen)
                        if (pipeTop.pipeX + pipeTop.width < 0) {
                            resetPipes()
                        }

                        pipeBottom.invalidate()

                        checkCollision()
                        handler.postDelayed(this, 30)
                    }
                }
            })
        }, 2000)
    }

    private fun resetPipes() {
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()

        // Reset pipes to their starting positions
        pipeTop.pipeX = 1000f
        pipeBottom.pipeX = 1000f

        // Set random height for top pipe
        val minPipeSize = 100f
        val maxTopHeight = screenHeight - gap - minPipeSize
        val topHeight = (minPipeSize.toInt()..maxTopHeight.toInt()).random().toFloat()
        pipeTop.height = topHeight

        // Set bottom pipe height based on top pipe and gap
        pipeBottom.height = screenHeight - topHeight - gap

        // Reset scoring
        pipeTop.hasScored = false
        pipeBottom.hasScored = false
    }

    private fun updateBird() {
        // Apply gravity to velocity
        velocity += gravity

        bird.y += velocity

        // Check bottom boundary - game over if bird falls below screen
        if (bird.y + bird.height > resources.displayMetrics.heightPixels) {
            gameOver()
        }

        // Check top boundary - prevent bird from going off top of screen
        if (bird.y < 0) {
            bird.y = 0f
            velocity = 0f
        }
    }

    private fun checkCollision() {
        // Bird's position (center of the image)
        val birdCenterX = bird.x + bird.width / 2
        val birdCenterY = bird.y + bird.height / 2

        // Bird's radius
        val birdRadius = bird.width / 2.5f

        // Get pipe rectangles
        val pipeTopRect = pipeTop.getBounds()
        val pipeBottomRect = pipeBottom.getBounds()

        // Check collision with top pipe
        if (checkCircleRectCollision(birdCenterX, birdCenterY, birdRadius, pipeTopRect)) {
            gameOver()
            return
        }

        // Check collision with bottom pipe
        if (checkCircleRectCollision(birdCenterX, birdCenterY, birdRadius, pipeBottomRect)) {
            gameOver()
            return
        }

        // Scoring: When the bird passes through the pipes
        if (pipeTop.pipeX + pipeTop.width < birdCenterX - birdRadius && !pipeTop.hasScored) {
            score++
            pipeTop.hasScored = true
            scoreText.text = "Points: $score"
        }
    }

    // Helper function for circle-rectangle collision
    private fun checkCircleRectCollision(
        circleX: Float,
        circleY: Float,
        radius: Float,
        rect: android.graphics.Rect
    ): Boolean {
        // Find closest point in rectangle to circle center
        val closestX = circleX.coerceIn(rect.left.toFloat(), rect.right.toFloat())
        val closestY = circleY.coerceIn(rect.top.toFloat(), rect.bottom.toFloat())

        // Calculate distance between closest point and circle center
        val distanceX = circleX - closestX
        val distanceY = circleY - closestY

        // If distance is less than radius, collision detected
        val distanceSquared = (distanceX * distanceX) + (distanceY * distanceY)
        return distanceSquared <= (radius * radius)
    }

    private fun gameOver() {
        isGameOver = true
        gameOverText.visibility = TextView.VISIBLE
        pointsMessage.visibility = TextView.VISIBLE
        pointsMessage.text = "You got $score points!"
    }
}
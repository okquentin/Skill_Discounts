package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

private var userId: Int = -1 // Declare userId as a class property
private var businessId: Int = -1 // Declare businessId as a class property

class Leaderboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)

        // UI elements needed for leaderboard
        val cancel = findViewById<Button>(R.id.cancelButton)
        val play = findViewById<Button>(R.id.playButton)

        // Get userId and businessId from intent
        val userId = intent.getIntExtra("userId", -1)
        val businessId = intent.getIntExtra("businessId", -1)

        // Cancel button brings user back to page they were just at
        cancel.setOnClickListener {
            val intent = Intent(this, BusinessList::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // Play button brings users to the game page
        play.setOnClickListener {
            val intent = Intent(this, Game::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("businessId", businessId) // Add businessId to the intent
            Log.d("Leaderboard", "Starting Game with userId: $userId and businessId: $businessId")
            startActivity(intent)
        }

    }
}
package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Game : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)

        // UI elements needed for code
        val backButton = findViewById<Button>(R.id.back_button)
        val birdCharacter = findViewById<ImageView>(R.id.bird)

        // Sets up image view with correct picture
        birdCharacter.setImageResource(R.drawable.game_bird)

        // Back button which appears when the game ends and brings users back to the business list page
        backButton.setOnClickListener {
            val intent = Intent(this, BusinessList::class.java)
            startActivity(intent)
        }

    }
}
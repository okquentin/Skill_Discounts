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

        val backButton = findViewById<Button>(R.id.back_button)
        val birdCharacter = findViewById<ImageView>(R.id.bird)

        birdCharacter.setImageResource(R.drawable.game_bird)

        backButton.setOnClickListener {
            val intent = Intent(this, BusinessList::class.java)
            startActivity(intent)
        }

    }
}
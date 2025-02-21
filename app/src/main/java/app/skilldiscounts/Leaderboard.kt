package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Leaderboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)

        val cancel = findViewById<Button>(R.id.cancelButton)
        val play = findViewById<Button>(R.id.playButton)

        cancel.setOnClickListener {
            val intent = Intent(this, BusinessList::class.java)
            startActivity(intent)
        }

        play.setOnClickListener {
            val intent = Intent(this, Game::class.java)
            startActivity(intent)
        }

    }
}
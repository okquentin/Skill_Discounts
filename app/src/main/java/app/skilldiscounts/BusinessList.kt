package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BusinessList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_business_list)

        val businessOne = findViewById<ImageView>(R.id.store1Image)
        val businessTwo = findViewById<ImageView>(R.id.store2Image)
        val businessThree = findViewById<ImageView>(R.id.store3Image)

        val businessOneGame = findViewById<Button>(R.id.store1Play)
        val businessTwoGame = findViewById<Button>(R.id.store2Play)
        val businessThreeGame = findViewById<Button>(R.id.store3Play)
        val rewardsIcon = findViewById<ImageButton>(R.id.toRewards)

        rewardsIcon.setImageResource(R.drawable.reward)
        businessOne.setImageResource(R.drawable.pizza)
        businessTwo.setImageResource(R.drawable.hardware)
        businessThree.setImageResource(R.drawable.antique)

        businessOneGame.setOnClickListener {
            val intent = Intent(this, Leaderboard::class.java)
            startActivity(intent)
        }

        businessTwoGame.setOnClickListener {
            val intent = Intent(this, Leaderboard::class.java)
            startActivity(intent)
        }

        businessThreeGame.setOnClickListener {
            val intent = Intent(this, Leaderboard::class.java)
            startActivity(intent)
        }

        rewardsIcon.setOnClickListener {
            val intent = Intent(this, Rewards::class.java)
            startActivity(intent)
        }

    }
}
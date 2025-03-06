package app.skilldiscounts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BusinessList : AppCompatActivity() {

    // Variables for the point totals in the view
    private var points1 = 768
    private var points2 = 125
    private var points3 = 459

    // Variables to store the values for the money in the Rewards class
    private var wallet1 = 3
    private var wallet2 = 2
    private var wallet3 = 4

    // TextView Variables
    private lateinit var pointsOne: TextView
    private lateinit var pointsTwo: TextView
    private lateinit var pointsThree: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_business_list)

        // Image Views in use on page
        val businessOne = findViewById<ImageView>(R.id.store1Image)
        val businessTwo = findViewById<ImageView>(R.id.store2Image)
        val businessThree = findViewById<ImageView>(R.id.store3Image)

        // Text Views in use on page
        pointsOne = findViewById<TextView>(R.id.store1Rewards)
        pointsTwo = findViewById<TextView>(R.id.store2Rewards)
        pointsThree = findViewById<TextView>(R.id.store3Rewards)

        // Update to reflect variable values
        stringUpdate()

        // Buttons in use on page
        val businessOneGame = findViewById<Button>(R.id.store1Play)
        val businessTwoGame = findViewById<Button>(R.id.store2Play)
        val businessThreeGame = findViewById<Button>(R.id.store3Play)
        val rewardsIcon = findViewById<ImageButton>(R.id.toRewards)

        // Set the image views to the picture they need to be
        rewardsIcon.setImageResource(R.drawable.reward)
        businessOne.setImageResource(R.drawable.pizza)
        businessTwo.setImageResource(R.drawable.hardware)
        businessThree.setImageResource(R.drawable.antique)

        // These three buttons take users to the leaderboard page
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

        // Rewards button brings users to rewards page
        rewardsIcon.setOnClickListener {
            val intent = Intent(this, Rewards::class.java)
            intent.putExtra("wallet1", wallet1)
            intent.putExtra("wallet2", wallet2)
            intent.putExtra("wallet3", wallet3)
            intent.putExtra("points1", points1)
            intent.putExtra("points2", points2)
            intent.putExtra("points3", points3)
            rewardsLauncher.launch(intent)
        }

    }

    // Updates the Rewards view to ensure the view corresponds to the values in the Rewards View
    fun stringUpdate(){
        pointsOne.text = getString(R.string.store_1_Rewards, points1)
        pointsTwo.text = getString(R.string.store_2_Rewards, points2)
        pointsThree.text = getString(R.string.store_3_Rewards, points3)
    }

    val rewardsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            wallet1 = result.data!!.getIntExtra("wallet1", wallet1)
            wallet2 = result.data!!.getIntExtra("wallet2", wallet2)
            wallet3 = result.data!!.getIntExtra("wallet3", wallet3)
            points1 = result.data!!.getIntExtra("points1", points1)
            points2 = result.data!!.getIntExtra("points2", points2)
            points3 = result.data!!.getIntExtra("points3", points3)
        }
        stringUpdate()
    }
}
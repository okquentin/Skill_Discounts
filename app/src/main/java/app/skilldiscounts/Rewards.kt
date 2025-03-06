package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Rewards : AppCompatActivity() {
    // Variables for the points in the view
    private var points1 = -1
    private var points2 = -1
    private var points3 = -1

    // Variables for the money in the view
    private var wallet1 = -1
    private var wallet2 = -1
    private var wallet3 = -1

    // TextView Variables
    private lateinit var pointsOne: TextView
    private lateinit var pointsTwo: TextView
    private lateinit var pointsThree: TextView
    private lateinit var walletOne: TextView
    private lateinit var walletTwo: TextView
    private lateinit var walletThree: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rewards)

        // back button to return to business list
        val backButton = findViewById<Button>(R.id.back)
        val redemption1 = findViewById<Button>(R.id.redemption1)
        val redemption2 = findViewById<Button>(R.id.redemption2)
        val redemption3 = findViewById<Button>(R.id.redemption3)

        // Text Views in use on page
        pointsOne = findViewById<TextView>(R.id.points1)
        pointsTwo = findViewById<TextView>(R.id.points2)
        pointsThree = findViewById<TextView>(R.id.points3)
        walletOne = findViewById<TextView>(R.id.wallet1)
        walletTwo = findViewById<TextView>(R.id.wallet2)
        walletThree = findViewById<TextView>(R.id.wallet3)

        // Ensures ints are the same across views
        wallet1 = intent.getIntExtra("wallet1", wallet1)
        wallet2 = intent.getIntExtra("wallet2", wallet2)
        wallet3 = intent.getIntExtra("wallet3", wallet3)
        points1 = intent.getIntExtra("points1", points1)
        points2 = intent.getIntExtra("points2", points2)
        points3 = intent.getIntExtra("points3", points3)

        // Make sure everything is the same
        stringUpdate()


        // If back button pressed, return to business list
        backButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("wallet1", wallet1)
            intent.putExtra("wallet2", wallet2)
            intent.putExtra("wallet3", wallet3)
            intent.putExtra("points1", points1)
            intent.putExtra("points2", points2)
            intent.putExtra("points3", points3)
            setResult(RESULT_OK, intent)
            finish()
        }

        // If redemption1 button pressed, redeem points for business 1 if possible
        redemption1.setOnClickListener {
            if (points1 >= 250){
                points1 -= 250
                wallet1 += 1
                stringUpdate()
            }
        }

        // If redemption2 button pressed, redeem points for business 2 if possible
        redemption2.setOnClickListener {
            if (points2 >= 250){
                points2 -= 250
                wallet2 += 1
                stringUpdate()
            }
        }

        // If redemption3 button pressed, redeem points for business 3 if possible
        redemption3.setOnClickListener {
            if (points3 >= 250){
                points3 -= 250
                wallet3 += 1
                stringUpdate()
            }
        }

    }

    // Updates the Rewards view to ensure the view corresponds to the executed actions
    fun stringUpdate(){
        pointsOne.text = getString(R.string.rewards_points_1, points1)
        pointsTwo.text = getString(R.string.rewards_points_2, points2)
        pointsThree.text = getString(R.string.rewards_points_3, points3)
        walletOne.text = getString(R.string.current_wallet_balance_1, wallet1)
        walletTwo.text = getString(R.string.current_wallet_balance_2, wallet2)
        walletThree.text = getString(R.string.wallet_3, wallet3)
    }
}
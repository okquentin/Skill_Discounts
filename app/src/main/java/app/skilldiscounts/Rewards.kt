package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Rewards : AppCompatActivity() {
    // Variables for the points in the view
    var points1 = 0
    var points2 = 0
    var points3 = 0

    // Variables for the money in the view
    var wallet1 = 0
    var wallet2 = 0
    var wallet3 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rewards)

        // back button to return to business list
        val backButton = findViewById<Button>(R.id.back)
        val redemption1 = findViewById<Button>(R.id.redemption1)
        val redemption2 = findViewById<Button>(R.id.redemption2)
        val redemption3 = findViewById<Button>(R.id.redemption3)


        // If back button pressed, return to business list
        backButton.setOnClickListener {
            val intent = Intent(this, BusinessList::class.java)
            intent.putExtra("points1", points1)
            intent.putExtra("points2", points2)
            intent.putExtra("points3", points3)
            startActivity(intent)
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

    }
}
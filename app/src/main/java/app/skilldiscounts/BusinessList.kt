package app.skilldiscounts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessList : AppCompatActivity() {

    private var points1 = 0
    private var points2 = 0
    private var points3 = 0

    private var wallet1 = 0
    private var wallet2 = 0
    private var wallet3 = 0

    private var userId: Int = -1 // Declare userId as a class property

    // UI Elements
    private lateinit var pointsOne: TextView
    private lateinit var pointsTwo: TextView
    private lateinit var pointsThree: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_business_list)

        // Retrieve userId from intent
        userId = intent.getIntExtra("userId", -1)
        Log.i("BusinessList", "Received User ID: $userId")

        // Image Views
        val businessOne = findViewById<ImageView>(R.id.store1Image)
        val businessTwo = findViewById<ImageView>(R.id.store2Image)
        val businessThree = findViewById<ImageView>(R.id.store3Image)

        // Text Views
        pointsOne = findViewById(R.id.store1Rewards)
        pointsTwo = findViewById(R.id.store2Rewards)
        pointsThree = findViewById(R.id.store3Rewards)

        // Buttons
        val businessOneGame = findViewById<Button>(R.id.store1Play)
        val businessTwoGame = findViewById<Button>(R.id.store2Play)
        val businessThreeGame = findViewById<Button>(R.id.store3Play)
        val rewardsIcon = findViewById<ImageButton>(R.id.toRewards)

        // Set images
        rewardsIcon.setImageResource(R.drawable.reward)
        businessOne.setImageResource(R.drawable.pizza)
        businessTwo.setImageResource(R.drawable.hardware)
        businessThree.setImageResource(R.drawable.antique)

        Log.d("BusinessList", "Fetching user rewards for userId: $userId")
        // Fetch user rewards from API
        fetchUserRewards(userId)

        // Navigate to Leaderboard (Gameplay)
        businessOneGame.setOnClickListener { openLeaderboard(1) }
        businessTwoGame.setOnClickListener { openLeaderboard(2) }
        businessThreeGame.setOnClickListener { openLeaderboard(3) }

        // Navigate to Rewards page
        rewardsIcon.setOnClickListener {
            val intent = Intent(this, Rewards::class.java)
            intent.putExtra("wallet1", wallet1)
            intent.putExtra("wallet2", wallet2)
            intent.putExtra("wallet3", wallet3)
            intent.putExtra("points1", points1)
            intent.putExtra("points2", points2)
            intent.putExtra("points3", points3)
            intent.putExtra("userId", userId) // Pass userId to Rewards activity
            rewardsLauncher.launch(intent)
        }
    }

    private fun openLeaderboard(businessId: Int) {
        Log.i("BusinessList", "Opening leaderboard")
        val intent = Intent(this, Leaderboard::class.java) // Ensure correct activity is opened
        intent.putExtra("userId", userId)
        Log.d("BusinessList", "BusinessId: $businessId")
        intent.putExtra("businessId", businessId)
        startActivity(intent)
    }

    private fun fetchUserRewards(userId: Int) {
        val call = RetrofitClient.instance.getUserRewards(userId)
        Log.d("BusinessList", "Requesting URL: ${call.request()}")  // Log the full request
        call.enqueue(object : Callback<List<Reward>> {
            override fun onResponse(call: Call<List<Reward>>, response: Response<List<Reward>>) {
                if (response.isSuccessful) {
                    Log.d("BusinessList", "Successfully fetched user rewards")
                    response.body()?.let { rewards ->
                        rewards.forEach {
                            when (it.business_id) {
                                1 -> {
                                    points1 = it.points
                                    wallet1 = it.wallet_balance
                                }
                                2 -> {
                                    points2 = it.points
                                    wallet2 = it.wallet_balance
                                }
                                3 -> {
                                    points3 = it.points
                                    wallet3 = it.wallet_balance
                                }
                            }
                        }
                        updateUI()
                    }
                } else {
                    Log.e("BusinessList", "Failed to load rewards: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Reward>>, t: Throwable) {
                Log.e("BusinessList", "API Error: ${t.message}")
            }
        })
    }

    private fun updateUI() {
        pointsOne.text = getString(R.string.store_1_Rewards, points1)
        pointsTwo.text = getString(R.string.store_2_Rewards, points2)
        pointsThree.text = getString(R.string.store_3_Rewards, points3)
    }

    // Receive updated data from Rewards activity
    private val rewardsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK && result.data != null) {
        wallet1 = result.data!!.getIntExtra("wallet1", wallet1)
        wallet2 = result.data!!.getIntExtra("wallet2", wallet2)
        wallet3 = result.data!!.getIntExtra("wallet3", wallet3)
        points1 = result.data!!.getIntExtra("points1", points1)
        points2 = result.data!!.getIntExtra("points2", points2)
        points3 = result.data!!.getIntExtra("points3", points3)
        updateUI()
    }
}
}

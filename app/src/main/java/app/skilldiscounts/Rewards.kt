package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Rewards : AppCompatActivity() {
    // Variables for user data
    private var userId = 1  // This should be dynamically set based on logged-in user

    private var points1 = 0
    private var points2 = 0
    private var points3 = 0

    private var wallet1 = 0
    private var wallet2 = 0
    private var wallet3 = 0

    // UI Elements
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

        // UI Elements
        val backButton = findViewById<Button>(R.id.back)
        val redemption1 = findViewById<Button>(R.id.redemption1)
        val redemption2 = findViewById<Button>(R.id.redemption2)
        val redemption3 = findViewById<Button>(R.id.redemption3)

        pointsOne = findViewById(R.id.points1)
        pointsTwo = findViewById(R.id.points2)
        pointsThree = findViewById(R.id.points3)
        walletOne = findViewById(R.id.wallet1)
        walletTwo = findViewById(R.id.wallet2)
        walletThree = findViewById(R.id.wallet3)

        Log.d("Rewards", "Fetching user rewards for userId: $userId")
        // Fetch user rewards from API
        fetchUserRewards(userId)

        // Back button listener
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

        // Redemption button listeners
        redemption1.setOnClickListener { redeemPoints(userId, 1) }
        redemption2.setOnClickListener { redeemPoints(userId, 2) }
        redemption3.setOnClickListener { redeemPoints(userId, 3) }
    }

    private fun fetchUserRewards(userId: Int) {
        RetrofitClient.instance.getUserRewards(userId).enqueue(object : Callback<List<Reward>> {
            override fun onResponse(call: Call<List<Reward>>, response: Response<List<Reward>>) {
                if (response.isSuccessful) {
                    Log.d("Rewards", "Successfully fetched user rewards")
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
                    Log.e("Rewards", "Failed to load rewards: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Reward>>, t: Throwable) {
                Log.e("Rewards", "API Error: ${t.message}")
            }
        })
    }

    private fun redeemPoints(userId: Int, businessId: Int) {
        Log.d("Rewards", "Attempting to redeem points for businessId: $businessId")
        if (getPointsForBusiness(businessId) < 250) {
            Log.e("Rewards", "Not enough points to redeem for businessId: $businessId")
            return
        }

        val request = RedeemRequest(userId, businessId)

        RetrofitClient.instance.redeemPoints(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    Log.d("Rewards", "Redemption successful for businessId: $businessId")
                    when (businessId) {
                        1 -> {
                            points1 -= 250
                            wallet1 += 1
                        }
                        2 -> {
                            points2 -= 250
                            wallet2 += 1
                        }
                        3 -> {
                            points3 -= 250
                            wallet3 += 1
                        }
                    }
                    updateUI()
                } else {
                    Log.e("Rewards", "Redemption failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Rewards", "API Error: ${t.message}")
            }
        })
    }

    private fun getPointsForBusiness(businessId: Int): Int {
        return when (businessId) {
            1 -> points1
            2 -> points2
            3 -> points3
            else -> 0
        }
    }

    private fun updateUI() {
        pointsOne.text = getString(R.string.rewards_points_1, points1)
        pointsTwo.text = getString(R.string.rewards_points_2, points2)
        pointsThree.text = getString(R.string.rewards_points_3, points3)
        walletOne.text = getString(R.string.current_wallet_balance_1, wallet1)
        walletTwo.text = getString(R.string.current_wallet_balance_2, wallet2)
        walletThree.text = getString(R.string.wallet_3, wallet3)
    }
}

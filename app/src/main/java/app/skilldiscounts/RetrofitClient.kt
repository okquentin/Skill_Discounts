package app.skilldiscounts

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.*

// Retrofit Client
object RetrofitClient {
    private const val BASE_URL = "http://localhost:5000/"

    val instance: RewardsApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RewardsApi::class.java)
    }
}

// API Interface
interface RewardsApi {
    @GET("user_rewards/{user_id}")
    fun getUserRewards(@Path("user_id") userId: Int): Call<List<Reward>>

    @POST("redeem")
    fun redeemPoints(@Body request: RedeemRequest): Call<ApiResponse>
}

// Data Models
data class Reward(
    val business_id: Int,
    val name: String,
    val points: Int,
    val wallet_balance: Int
)

data class RedeemRequest(
    val user_id: Int,
    val business_id: Int
)

data class ApiResponse(
    val message: String
)

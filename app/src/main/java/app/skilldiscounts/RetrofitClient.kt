package app.skilldiscounts

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.*
import com.google.gson.annotations.SerializedName

// Retrofit Client
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5000/"

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

    @POST("add_user")
    fun createUser(@Body request: CreateUserRequest): Call<ApiResponse>

    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("update_points")
    fun updateUserPoints(@Body request: UpdatePointsRequest): Call<ApiResponse>
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

data class CreateUserRequest(
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val userId: Int,
    val message: String
)

data class ApiResponse(
    val message: String,
    @SerializedName("user_id") val userId: Int? = null // Ensure user_id is correctly mapped
)

data class UpdatePointsRequest(
    val user_id: Int,
    val business_id: Int,
    val points: Int
)

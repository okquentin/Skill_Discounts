package app.skilldiscounts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.login)
        loginButton.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
            val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValidEmail(email)) {
                loginUser(email, password)
            } else {
                Log.e("MainActivity", "Invalid email format")
            }
        }

        val signUpButton = findViewById<Button>(R.id.signUp)
        signUpButton.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
            val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValidEmail(email)) {
                createUser(email, password)
            } else {
                Log.e("MainActivity", "Invalid email format")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun createUser(email: String, password: String) {
        val request = CreateUserRequest(email, password)
        val call = RetrofitClient.instance.createUser(request)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.i("MainActivity", "User created successfully: ${apiResponse?.message}")
                    Log.i("MainActivity", "User ID: ${apiResponse?.userId}")
                    val intent = Intent(this@MainActivity, BusinessList::class.java)
                    intent.putExtra("userId", apiResponse?.userId) // Pass userId
                    startActivity(intent)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.e("MainActivity", "Failed to create user: $errorResponse")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    private fun loginUser(email: String, password: String) {
        val request = LoginRequest(email, password)
        val call = RetrofitClient.instance.loginUser(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val userId = loginResponse?.userId
                    Log.i("MainActivity", "Login successful: ${loginResponse?.message}")
                    Log.i("MainActivity", "Retrieved User ID: $userId")
                    val intent = Intent(this@MainActivity, BusinessList::class.java)
                    intent.putExtra("userId", userId) // Pass userId
                    startActivity(intent)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.e("MainActivity", "Failed to login: $errorResponse")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }
}
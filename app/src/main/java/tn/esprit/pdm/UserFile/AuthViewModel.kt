    package tn.esprit.pdm.UserFile

    import android.util.Log
    import androidx.compose.runtime.mutableStateListOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import okhttp3.OkHttpClient
    import org.json.JSONObject
    import tn.esprit.pdm.Models.User
    import java.util.concurrent.TimeUnit
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import tn.esprit.pdm.UserFile.ForgotPassword.ForgotPasswordRequest
    import tn.esprit.pdm.UserFile.ForgotPassword.ResetPasswordRequest
    import tn.esprit.pdm.UserFile.ForgotPassword.VerifyOtpRequest
    import tn.esprit.pdm.UserFile.Login.LoginRequest
    import tn.esprit.pdm.UserFile.SignUp.SignupRequest
    import tn.esprit.pdm.Models.AuthState
    import androidx.compose.runtime.State

    class AuthViewModel : ViewModel() {

        private val _authState = mutableStateOf(AuthState())
        val authState: State<AuthState> get() = _authState

        var token: String? = null
        var loginError: String? = null
        var userName: String? = null
        var forgotError: String? = null
        var isVerified = false
        var id: String? = null
        var errorPassword: String? = null
        var user: User? = null

        var userId1 = mutableStateOf<String?>(null)
        var userImage1 = mutableStateOf<String?>(null)
        var userName1 = mutableStateOf<String?>(null)
        var userEmail1 = mutableStateOf<String?>(null)
        var userPhone1 = mutableStateOf<String?>(null)
        var userRegion1 = mutableStateOf<String?>(null)
        var userRole1 = mutableStateOf<String?>(null)
        var userAdditionalImages1 = mutableStateListOf<String?>()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Increase write timeout
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authService = retrofit.create(AuthService::class.java)


        fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
            viewModelScope.launch {
                try {
                    val loginRequest = LoginRequest(email, password)
                    val response = authService.login(loginRequest)
                    if (response.accessToken.isNotEmpty() && response.userId.isNotEmpty()) {
                        this@AuthViewModel.token = response.accessToken
                        this@AuthViewModel.id = response.userId
                        userId1.value = response.userId
                        fetchUser(response.userId, response.accessToken) { success ->
                            if (success) {
                                _authState.value = AuthState(isAuthenticated = true)
                                loginError = null
                                onResult(true)
                            } else {
                                loginError = "Failed to fetch user in login"
                                onResult(false)
                            }
                        }
                    } else {
                        loginError = "Invalid credentials"
                        onResult(false)
                    }
                } catch (e: Exception) {
                    Log.e("aa", "Error11: ${e.localizedMessage}")
                    loginError = "Check your email and password"
                    onResult(false)
                }
            }
        }

        private fun fetchUser(userId: String, token: String, onResult: (Boolean) -> Unit) {
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        authService.fetchUser(userId, "Bearer $token").execute()
                    }

                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        Log.d("FetchUser", "Raw Response: $user")
                        user.id= id.toString()
                        if (user.id == null) {
                            Log.e("FetchUser", "User ID is missing from the response")
                            loginError = "User ID is not available"
                            onResult(false)
                        } else {
                            this@AuthViewModel.user = user // Ensure user is set
                            Log.d("FetchUser", "User fetched: $user") // Log user data

                            userName1.value = user.name // Correctly set MutableState value
                            userImage1.value = user.image // Correctly set MutableState value
                            userEmail1.value = user.email
                            userPhone1.value = user.phoneNumber
                            userRole1.value = user.role // User role
                            userRegion1.value = user.region // User region
                            // Update additional images
                            userAdditionalImages1.clear()
                            user.additionalImages?.let { additionalImages ->
                                userAdditionalImages1.addAll(additionalImages)
                            }
                            loginError = null
                            onResult(true)
                        }
                    } else {
                        loginError = "Failed to fetch user"
                        onResult(false)
                    }
                } catch (e: Exception) {
                    loginError = "Error: ${e.localizedMessage}"
                    onResult(false)
                }
            }
        }

        fun signup(
            imageUrl: String,
            email: String,
            password: String,
            name: String,
            phone: String,
            role: String,
            region: String,
            additionalImages: List<String> = emptyList(),
            onResult: (Boolean) -> Unit
        ) {
            viewModelScope.launch {
                try {
                    val signupRequest = SignupRequest(
                        email = email,
                        password = password,
                        name = name,
                        phoneNumber = phone,
                        region = region,
                        role = role,
                        image = imageUrl,
                        additionalImages = if (role == "Mechanical") additionalImages else null
                    )
                    Log.e("Signup", "Request: $signupRequest")
                    val response = authService.signup(signupRequest)
                    Log.e("aa", "aaaa")
                    if (response.success) {
                        Log.e("bb", "bbbb")
                        loginError = null
                        onResult(true) // Success
                    } else {
                        Log.e("cc", "cccc")
                        loginError = response.message
                        onResult(false)
                    }
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Signup Error: ${e.localizedMessage}")
                    loginError = "Signup failed. Please check your details and try again."
                    onResult(false)
                }
            }
        }


        fun forgotPassword(email: String, onResult: (Boolean) -> Unit) {
            viewModelScope.launch {
                try {
                    val response = authService.forgotPassword(ForgotPasswordRequest(email))
                    Log.e("cc", "Response: ${response.body()}") // Log the response body for inspection
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.success == true) {
                            id = responseBody.message // Assume 'message' contains user ID if successful
                            forgotError = null
                            onResult(true)
                        } else {
                            Log.e("dd", "User not found or request failed: ${responseBody?.message}")
                            forgotError = "User not found or request failed"
                            onResult(false)
                        }
                    } else {
                        Log.e("ee", "Unsuccessful response: ${response.errorBody()?.string()}")
                        forgotError = "Request failed with error: ${response.errorBody()?.string()}"
                        onResult(false)
                    }
                } catch (e: Exception) {
                    Log.e("gg", "Error during forgotPassword request: ${e.localizedMessage}")
                    forgotError = "Error: ${e.localizedMessage}"
                    onResult(false)
                }
            }
        }


        fun verifyOtp(otpCode: String, onResult: (Boolean) -> Unit) {
            viewModelScope.launch {
                try {
                    val response = authService.verifyOtp(VerifyOtpRequest(otpCode))
                    if (response.isSuccessful && response.body()?.success == true) {
                        isVerified = true
                        loginError = null
                        onResult(true)
                    } else {
                        loginError = "Invalid OTP code"
                        onResult(false)
                    }
                } catch (e: Exception) {
                    loginError = "Error: ${e.localizedMessage}"
                    onResult(false)
                }
            }
        }

        fun resetPassword(newPassword: String, onResult: (Boolean) -> Unit) {
            viewModelScope.launch {
                try {
                    val userId = id ?: return@launch
                    val response = authService.resetPassword(ResetPasswordRequest(userId, newPassword))
                    if (response.isSuccessful && response.body()?.success == true) {
                        errorPassword = null
                        onResult(true)
                    } else {
                        errorPassword = "Invalid password format"
                        onResult(false)
                    }
                } catch (e: Exception) {
                    errorPassword = "Error: ${e.localizedMessage}"
                    onResult(false)
                }
            }
        }

        // Adding the update function
        fun updateUser(
            userId: String, // User ID to update
            updateData: UpdateProfileDto, // Data to update
            onResult: (Boolean) -> Unit
        ) {
            viewModelScope.launch {
                try {
                    // Prepare the update data
                    val response = authService.updateUser(userId, updateData)

                    if (response.isSuccessful) {
                        // If successful, update user state or show success
                        userName1.value = updateData.name // Update the user data as needed
                        userEmail1.value = updateData.email
                        userPhone1.value = updateData.phoneNumber
                        userRegion1.value = updateData.region
                        userRole1.value = updateData.role
                        userImage1.value = updateData.image

                        loginError = null
                        onResult(true) // Success
                    } else {
                        loginError = "Update failed: ${response.message()}"
                        onResult(false) // Failure
                    }
                } catch (e: Exception) {
                    loginError = "Error: ${e.localizedMessage}"
                    onResult(false) // Failure
                }
            }
        }


        // Adding the delete function
        fun deleteUser(
            userId: String, // User ID to delete
            onResult: (Boolean) -> Unit
        ) {
            viewModelScope.launch {
                try {
                    // Call the delete API
                    val response = authService.deleteUser(userId)

                    if (response.isSuccessful) {
                        // Handle success (clear user data or show success)
                        logout { success ->
                            if (success) {
                                onResult(true)
                            } else {
                                onResult(false)
                            }
                        }
                    } else {
                        loginError = "Delete failed: ${response.message()}"
                        onResult(false) // Failure
                    }
                } catch (e: Exception) {
                    loginError = "Error: ${e.localizedMessage}"
                    onResult(false) // Failure
                }
            }
        }

        fun logout(onResult: (Boolean) -> Unit) {
            // Clear authentication-related data
            token = null
            id = null
            _authState.value = AuthState(isAuthenticated = false)
            loginError = null

            // Clear user-related data
            user = null
            userName = null
            userRole1.value = null
            userRegion1.value = null
            errorPassword = null

            // Clear MutableState values
            userId1.value = null
            userImage1.value = null
            userName1.value = null
            userEmail1.value = null
            userPhone1.value = null

            // Clear MutableStateList values
            userAdditionalImages1.clear()

            // Notify the result of logout
            onResult(true)
        }


        fun JSONObject.toUser(): User {
        return User(
            id = optString("id"),
            image = optString("image"),
            name = optString("name"),
            email = optString("email"),
            phoneNumber = optString("phoneNumber"),
            region = optString("region"),
            role = optString("role"),
            additionalImages = optJSONArray("additionalImages")?.let { jsonArray ->
                List(jsonArray.length()) { index ->
                    jsonArray.optString(index)
                }
            } ?: emptyList()
        )
    }
}
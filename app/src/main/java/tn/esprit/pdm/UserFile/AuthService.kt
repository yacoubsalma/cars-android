package tn.esprit.pdm.UserFile

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import tn.esprit.pdm.UserFile.ForgotPassword.ForgotPasswordRequest
import tn.esprit.pdm.UserFile.ForgotPassword.GenericResponse
import tn.esprit.pdm.UserFile.ForgotPassword.ResetPasswordRequest
import tn.esprit.pdm.UserFile.ForgotPassword.VerifyOtpRequest
import tn.esprit.pdm.UserFile.Login.LoginRequest
import tn.esprit.pdm.UserFile.Login.LoginResponse
import tn.esprit.pdm.UserFile.SignUp.SignupRequest
import tn.esprit.pdm.UserFile.SignUp.SignupResponse
import tn.esprit.pdm.Models.User

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("auth/connect/{userId}")
    fun fetchUser(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): Call<User>

    @POST("auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): SignupResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): Response<GenericResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body body: VerifyOtpRequest): Response<GenericResponse>

    @PUT("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response<GenericResponse>

    // Update user details
    @PUT("auth/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body updateData: UpdateProfileDto
    ): Response<User> // Response type depends on your API's response

    // Delete user
    @DELETE("auth/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: String
    ): Response<Void> // Use appropriate response type (Void or custom response)

}
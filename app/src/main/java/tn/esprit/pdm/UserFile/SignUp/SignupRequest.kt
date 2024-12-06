package tn.esprit.pdm.UserFile.SignUp

data class SignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String,
    val region: String,
    val role: String,
    val image: String, // Main profile image
    val additionalImages: List<String>? = null // Optional additional images
)

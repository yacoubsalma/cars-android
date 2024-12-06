package tn.esprit.pdm.UserFile.ForgotPassword

data class ResetPasswordRequest(
    val id: String,
    val newPassword: String
)

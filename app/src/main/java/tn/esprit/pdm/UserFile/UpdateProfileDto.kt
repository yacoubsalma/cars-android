package tn.esprit.pdm.UserFile

data class UpdateProfileDto(
    val email: String?,
    val password: String?,
    val name: String?,
    val phoneNumber: String?,
    val region: String?,
    val role: String?,
    val image: String?
)

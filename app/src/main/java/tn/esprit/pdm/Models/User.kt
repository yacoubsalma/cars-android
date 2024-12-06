package tn.esprit.pdm.Models

data class User(
    var id: String,
    val image: String,
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val region: String? = null,
    val role: String? = null,
    val additionalImages: List<String>? = null
)

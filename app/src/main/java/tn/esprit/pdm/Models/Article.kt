package tn.esprit.pdm.Models

data class Article(
    val title: String?,
    val urlToImage: String?,
    val source: Source?
)

data class Source(
    val name: String?
)

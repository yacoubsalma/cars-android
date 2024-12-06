package tn.esprit.pdm.News

import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse

class NewsRepository(private val apiKey: String) {

    private val newsApiClient = NewsApiClient(apiKey)

    fun fetchArticles(
        query: String? = null,
        onSuccess: (List<Article>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        newsApiClient.getEverything(
            EverythingRequest.Builder()
                .q(query ?: "cars OR automobiles OR automotive OR vehicles")
                .language("en")
                .build(),
            object : NewsApiClient.ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse?) {
                    val validArticles = response?.articles?.filter { isValidArticle(it) } ?: emptyList()
                    onSuccess(validArticles)
                }

                override fun onFailure(throwable: Throwable?) {
                    onError(throwable ?: Exception("Unknown error"))
                }
            }
        )
    }

    private fun isValidArticle(article: Article): Boolean {
        return !article.title.isNullOrBlank() && article.title != "[Removed]" &&
                !article.url.isNullOrBlank() && article.url != "[Removed]"
    }
}
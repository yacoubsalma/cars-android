package tn.esprit.pdm.News

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwabenaberko.newsapilib.models.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository("55c250f06e1144c29a3ec4d2530adbe5")

    private val _newsState = MutableStateFlow<List<Article>>(emptyList())
    val newsState: StateFlow<List<Article>> = _newsState

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    init {
        // Fetch default car-related news on initialization
        fetchNews("cars OR automobiles OR automotive OR vehicles")
    }

    fun fetchNews(query: String? = null) {
        _loadingState.value = true
        repository.fetchArticles(
            query,
            onSuccess = {
                _newsState.value = it
                _loadingState.value = false
                _errorState.value = null
            },
            onError = {
                _errorState.value = it.localizedMessage
                _loadingState.value = false
            }
        )
    }
}

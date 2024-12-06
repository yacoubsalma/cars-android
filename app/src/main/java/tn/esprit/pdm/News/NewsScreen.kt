package tn.esprit.pdm.News

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.kwabenaberko.newsapilib.models.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavController, viewModel: NewsViewModel) {
    val articles by viewModel.newsState.collectAsState()
    val isLoading by viewModel.loadingState.collectAsState()
    val errorMessage by viewModel.errorState.collectAsState()

    var query by remember { mutableStateOf(TextFieldValue("")) }

    val skyBlue = Color(0xFF87CEEB)

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                if (it.text.isNotBlank()) {
                    viewModel.fetchNews(it.text)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp), // Adds rounded corners
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = skyBlue,
                focusedLabelColor = skyBlue
            ),
            label = { Text("Search News") }
        )

        // Loading State
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        // Error State
        else if (!errorMessage.isNullOrBlank()) {
            Text(
                text = "Error: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        // News List
        else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(articles) { article ->
                    NewsItem(article) {
                        navController.navigate("news_full_screen?url=${article.url}")
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp) // Increased elevation for better visual hierarchy
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            article.urlToImage?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(125.dp) // Increased size for a larger image
                        .clip(RoundedCornerShape(12.dp)) // Slightly rounded corners
                )
            }
            Spacer(modifier = Modifier.width(16.dp)) // Increased space between image and text
            Column {
                Text(
                    text = article.title ?: "No Title",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp)) // Added space between title and description
                Text(
                    text = article.description ?: "No Description",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3
                )
            }
        }
    }
}

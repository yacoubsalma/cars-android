package tn.esprit.pdm.News

import android.webkit.WebView
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun NewsFullScreen(url: String, navController: NavController) {
    AndroidView(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxSize(),
        factory = { WebView(it).apply { loadUrl(url) } }
    )
}

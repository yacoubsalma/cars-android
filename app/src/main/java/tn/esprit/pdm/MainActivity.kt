package tn.esprit.pdm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import tn.esprit.pdm.UserFile.AuthViewModel
import tn.esprit.pdm.UserFile.LoginScreen
import tn.esprit.pdm.UserFile.SignUpScreen
import tn.esprit.pdm.UserFile.UserDetailsScreen
import tn.esprit.pdm.Chat.ChatScreen
import tn.esprit.pdm.Chat.ConversationsScreen
import tn.esprit.pdm.Map.MapScreen
import tn.esprit.pdm.Models.User
import tn.esprit.pdm.News.NewsFullScreen
import tn.esprit.pdm.News.NewsScreen
import tn.esprit.pdm.News.NewsViewModel
import tn.esprit.pdm.Shop.ProductDetailsScreen
import tn.esprit.pdm.Shop.SharedViewModel
import tn.esprit.pdm.Shop.ShopScreen
import tn.esprit.pdm.VinDecoder.VinDecoderScreen
import tn.esprit.pdm.VinDecoder.VinDecoderViewModel
import androidx.navigation.compose.rememberNavController as rememberNavController1

private const val TRANSITION_UP_DURATION = 350
private const val TRANSITION_DOWN_DURATION = 250

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val navController = rememberNavController1()
    val authViewModel: AuthViewModel = viewModel()
    val viewModel: VinDecoderViewModel = viewModel()
    val sharedViewModel: SharedViewModel = viewModel()
    val newsViewModel: NewsViewModel = viewModel()

    val authState by authViewModel.authState

    // MutableState for the connected user
    val connectedUser = remember { mutableStateOf<User?>(null) }

    // Observer to update the connectedUser state when login succeeds
    if (authState.isAuthenticated) {
        connectedUser.value = authViewModel.user
    } else {
        connectedUser.value = null // Ensure the user is null if not authenticated
    }

    val vinDecoderViewModel: VinDecoderViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            if (authViewModel.authState.value.isAuthenticated) {
                BottomNavBar(navController = navController)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = "login_screen",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 55.dp)  // Adjust the padding
            ) {
                composable("login_screen") {
                    LoginScreen(
                        navController,
                        authViewModel
                    ) { user ->
                        if (user != null) {
                            connectedUser.value = user // Update on successful login
                            navController.navigate("home_screen") // Navigate to the home screen
                        } else {
                            Log.e("LoginScreen", "User is null after login")
                        }
                    }
                }
                composable("signup_screen") { SignUpScreen(navController, authViewModel) }
                composable("home_screen") { HomeScreen(navController, authViewModel, connectedUser.value) }
                composable("user_details_screen") { UserDetailsScreen(navController, authViewModel, connectedUser.value) }
                composable("conversations_screen") { ConversationsScreen(navController) }
                composable(
                    "chat/{channelId}",
                    arguments = listOf(
                        navArgument("channelId") {
                            type = NavType.StringType
                        }
                    )
                ) {
                    val channelId = it.arguments?.getString("channelId") ?: ""
                    ChatScreen(navController, channelId)
                }
                composable("vin_decoder_screen") { VinDecoderScreen(navController) }
                composable("settings_screen") { SettingsScreen(navController, authViewModel, connectedUser.value) }
                composable("map_screen") { MapScreen() }

                composable("shop_screen") { ShopScreen(viewModel = sharedViewModel, navController = navController) }
                composable("product_details_screen",
                    enterTransition = {
                        fadeIn(animationSpec = tween(TRANSITION_UP_DURATION)) + slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up, tween(TRANSITION_UP_DURATION)
                        )
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(TRANSITION_DOWN_DURATION)) + slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down, tween(TRANSITION_DOWN_DURATION)
                        )
                    }
                ) {
                    ProductDetailsScreen(viewModel = sharedViewModel, navController = navController)
                }

                composable("news_screen") {
                    NewsScreen(navController, newsViewModel)
                }
                composable("news_full_screen?url={url}",
                    arguments = listOf(navArgument("url") { type = NavType.StringType })
                ) { backStackEntry ->
                    NewsFullScreen(url = backStackEntry.arguments?.getString("url") ?: "", navController)
                }

            }
        }
    }
}
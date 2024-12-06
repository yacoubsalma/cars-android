package tn.esprit.pdm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)) // Add rounded corners
    ) {
        BottomAppBar(
            containerColor = Color.Black,
            contentColor = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // Distribute items evenly
            ) {
                // Icon in the center
                BottomNavItem(
                    icon = Icons.Filled.Chat,
                    contentDescription = "Conversations",
                    isSelected = currentRoute(navController) == "conversations_screen",
                    onClick = { navController.navigate("conversations_screen") }
                )

                // Icon in the center
                BottomNavItem(
                    icon = Icons.Filled.Newspaper,
                    contentDescription = "News",
                    isSelected = currentRoute(navController) == "news_screen",
                    onClick = { navController.navigate("news_screen") }
                )

                // Icon on the left
                BottomNavItem(
                    icon = Icons.Filled.Home,
                    contentDescription = "Home",
                    isSelected = currentRoute(navController) == "home_screen",
                    onClick = { navController.navigate("home_screen") }
                )

                // Icon in the center
                BottomNavItem(
                    icon = Icons.Filled.ShoppingCart,
                    contentDescription = "Shop",
                    isSelected = currentRoute(navController) == "shop_screen",
                    onClick = { navController.navigate("shop_screen") }
                )

                // Icon on the right
                BottomNavItem(
                    icon = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    isSelected = currentRoute(navController) == "settings_screen",
                    onClick = { navController.navigate("settings_screen") }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    contentDescription: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(60.dp)
                .padding(6.dp),
            tint = if (isSelected) Color.Cyan else Color.Gray
        )
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

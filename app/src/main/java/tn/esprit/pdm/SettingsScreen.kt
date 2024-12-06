package tn.esprit.pdm

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import tn.esprit.pdm.UserFile.AuthViewModel
import tn.esprit.pdm.Models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, viewModel: AuthViewModel, user: User?) {
    val userName = user?.name // Assuming userName is a MutableState<String?>
    val userImageUri = user?.image.let { Uri.parse(it) } // Safe parsing



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
                .background(Color(0xFFF5F5F5))
        ) {
            // Profile Section
            SettingSection(title = userName ?: "Guest", subtitle = "Settings") {
                Image(
                    painter = rememberImagePainter(
                        userImageUri ?: Uri.parse("android.resource://tn.esprit.pdm/drawable/placeholder_image") // Replace with your placeholder image
                    ),
                    contentDescription = "User Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }

            // Dark Mode Toggle
            SettingItem(
                icon = R.drawable.nightmode,
                title = "Dark Mode",
                isToggle = true,
                onToggleChange = { /* Handle toggle */ }
            )

            Text(
                text = "Profile",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.Gray),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Profile Options
            SettingItem(
                icon = R.drawable.user,
                title = "Edit Or Delete Profile",
                onClick = { navController.navigate("user_details_screen") }
            )

            SettingItem(
                icon = R.drawable.resetpassword ,
                title = "Change Password",
                onClick = { /* Handle click */ }
            )

            Text(
                text = "Notifications",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.Gray),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Notifications Toggle
            SettingItem(
                icon = R.drawable.notification,
                title = "Notifications",
                isToggle = true,
                onToggleChange = { /* Handle toggle */ }
            )

            Text(
                text = "Regional",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.Gray),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Regional Options
            SettingItem(
                icon = R.drawable.language,
                title = "Language",
                onClick = { /* Handle click */ }
            )

            SettingItem(
                icon = R.drawable.logout,
                title = "Logout",
                onClick = {
                    viewModel.logout {
                        navController.navigate("login_screen")
                    }
                }
            )
        }
}

@Composable
fun SettingSection(
    title: String,
    subtitle: String,
    iconContent: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconContent()
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.h6)
            Text(subtitle, style = MaterialTheme.typography.body2.copy(color = Color.Gray))
        }
    }
}

@Composable
fun SettingItem(
    icon: Any, // Accept both ImageVector and drawable Int
    title: String,
    onClick: (() -> Unit)? = null,
    isToggle: Boolean = false,
    onToggleChange: ((Boolean) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is ImageVector -> {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF4CAF50) // Custom color for icons
                )
            }
            is Int -> {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1
        )
        if (isToggle && onToggleChange != null) {
            var toggleState by remember { mutableStateOf(false) }
            Switch(
                checked = toggleState,
                onCheckedChange = {
                    toggleState = it
                    onToggleChange(it)
                },
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF4CAF50))
            )
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = Color.Gray
            )
        }
    }
}

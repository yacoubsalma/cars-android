package tn.esprit.pdm

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import tn.esprit.pdm.UserFile.AuthViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import tn.esprit.pdm.Models.User
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import tn.esprit.pdm.VinDecoder.VinDecoderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: AuthViewModel, user: User?) {
    val skyBlue = Color(0xFF87CEEB)
    val chathamsBlue = Color(0xFF233952)

    val userName = user?.name // Assuming userName is a MutableState<String?>
    val userImageUri = user?.image.let { Uri.parse(it) } // Safe parsing

    val vinDecoderViewModel: VinDecoderViewModel = viewModel() // Reference the VIN Decoder ViewModel
    val vinData by vinDecoderViewModel.vinData.collectAsState() // Collect vinData state

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // User profile image
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
                                .clickable { navController.navigate("user_details_screen") }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // User name with clickable action
                        Text(
                            text = userName ?: "Guest",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    // Navigate to UserDetailsScreen when clicked
                                    navController.navigate("user_details_screen")
                                }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = chathamsBlue)
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = chathamsBlue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item{
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = " Smart\nCar Scanner",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic, // Apply italic style
                        color =  Color(0xFF8A2BE2),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                    Image(
                        painter = painterResource(id = R.drawable.car3),
                        contentDescription = "Car"
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))

                    val isConnected = vinData != null // Determine if the VIN is connected
                    val connectionText = if (isConnected) "   Connected" else "   Please connect your car"
                    val connectionColor = if (isConnected) Color.Green else Color.Red

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(50.dp)
                            .background(
                                color = Color(0x80F5F5F5), // Set your desired background color here
                                shape = RoundedCornerShape(25.dp)
                            )
                            .clickable {
                                if (!isConnected) {
                                    navController.navigate("vin_decoder_screen")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = connectionText,
                                color = connectionColor,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward, // Replace with your desired icon
                                contentDescription = null,
                                modifier = Modifier.padding(end = 16.dp),
                                tint = Color.White // Icon color
                            )
                        }
                    }
                }


                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.95f) // Takes up most of the row's width
                            .height(150.dp) // Sets a fixed height for the rectangular shape
                            .background(
                                color = Color(0x80F5F5F5), // Desired color with transparency
                                shape = RoundedCornerShape(16.dp) // Rounded corners
                            )
                            .clickable { navController.navigate("news_screen") }, // Add click functionality if needed
                        contentAlignment = Alignment.TopStart // Ensures content alignment starts at the top
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize() // Fills the entire Box
                        ) {
                            // Image occupying the top 3/4 of the Box
                            Image(
                                painter = painterResource(id = R.drawable.car4), // Replace with your image resource
                                contentDescription = "Car image 4",
                                contentScale = ContentScale.Crop, // Adjusts the scaling
                                modifier = Modifier
                                    .fillMaxWidth() // Image width matches the box
                                    .weight(3f) // Gives it 3/4 space
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Clip corners for the image
                            )

                            // Text occupying the bottom 1/4 of the Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth() // Full width
                                    .weight(1f) // Gives it 1/4 space
                                    .background(
                                        color = Color(0x80F5F5F5), // A different background for text
                                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp) // Rounded corners
                                    ),
                                contentAlignment = Alignment.Center // Centers text
                            ) {
                                Text(
                                    text = "Details about your car",
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium, // Text styling
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }


                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.95f) // Takes up most of the row's width
                            .height(150.dp) // Sets a fixed height for the rectangular shape
                            .background(
                                color = Color(0x80F5F5F5), // Desired color with transparency
                                shape = RoundedCornerShape(16.dp) // Rounded corners
                            )
                            .clickable { navController.navigate("map_screen") }, // Add click functionality if needed
                        contentAlignment = Alignment.TopStart // Ensures content alignment starts at the top
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize() // Fills the entire Box
                        ) {
                            // Image occupying the top 3/4 of the Box
                            Image(
                                painter = painterResource(id = R.drawable.car4), // Replace with your image resource
                                contentDescription = "Car image 5",
                                contentScale = ContentScale.Crop, // Adjusts the scaling
                                modifier = Modifier
                                    .fillMaxWidth() // Image width matches the box
                                    .weight(3f) // Gives it 3/4 space
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Clip corners for the image
                            )

                            // Text occupying the bottom 1/4 of the Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth() // Full width
                                    .weight(1f) // Gives it 1/4 space
                                    .background(
                                        color = Color(0x80F5F5F5), // A different background for text
                                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp) // Rounded corners
                                    ),
                                contentAlignment = Alignment.Center // Centers text
                            ) {
                                Text(
                                    text = "Test",
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium, // Text styling
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    )
}
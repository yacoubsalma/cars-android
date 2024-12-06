package tn.esprit.pdm.UserFile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import tn.esprit.pdm.Models.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(navController: NavHostController, authviewModel: AuthViewModel, user: User?) {
    val skyBlue = Color(0xFF87CEEB)
    val darkerGreen = Color(0xFF1E7A3A)
    val lightGray = Color(0xFFD3D3D3) // Light Gray
    val chathamsBlue = Color(0xFF233952)

    // Get user details, including the userId
    val userId = user?.id

    // Current user details
    val userImageUri =user?.image.let { Uri.parse(it) }
    val userName = user?.name // User's name
    val userEmail = user?.email // User's email
    val userPhone = user?.phoneNumber // User's phone number
    val userRegion = user?.region // User's region
    val userRole = user?.role // User's role
    val userAdditionalImagesUris = user?.additionalImages?.map { Uri.parse(it) }

    // Editable states
    var image by remember { mutableStateOf(userImageUri) }
    var name by remember { mutableStateOf(userName) }
    var email by remember { mutableStateOf(userEmail) }
    var phone by remember { mutableStateOf(userPhone) }
    var region by remember { mutableStateOf(userRegion) }
    var role by remember { mutableStateOf(userRole) }

    val tunisianRegions = listOf(
        "Tunis", "Ariana", "Ben Arous", "Manouba", "Nabeul", "Zaghouan", "Bizerte", "Béja",
        "Jendouba", "Kef", "Siliana", "Sousse", "Monastir", "Mahdia", "Kairouan", "Kasserine",
        "Sidi Bouzid", "Sfax", "Gabès", "Médenine", "Tataouine", "Gafsa", "Tozeur", "Kebili"
    )
    var expandedRegion by remember { mutableStateOf(false) }

    val userRoles = listOf("Client", "Mechanical")
    var expandedRole by remember { mutableStateOf(false) }

    var imageUri1 by remember { mutableStateOf<Uri?>(null) }
    val context1 = LocalContext.current
    var bitmap1: Bitmap? by remember { mutableStateOf(null) }
    val launcher1 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri1 = it // Update the URI state
            bitmap1 = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context1.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context1.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    var imageUri2 = remember { mutableStateListOf<Uri>() }
    val context2 = LocalContext.current
    val bitmap2 = remember { mutableStateListOf<Bitmap?>() }
    val launcher2 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Handle the selected image URI here
                imageUri2.add(it)  // Add the URI to the list
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context2.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context2.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                bitmap2.add(bitmap)  // Add the decoded bitmap to the list
            }
        }

    // State for loading and error message
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Profile", color = Color.Black) // Customize the title as needed
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),  // Add padding around the whole column if needed
                verticalArrangement = Arrangement.spacedBy(16.dp),  // Add space between items
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (bitmap1 != null) {
                        Image(
                            bitmap = bitmap1!!.asImageBitmap(),
                            contentDescription = "Selected Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                    } else {
                        Image(
                            painter = rememberImagePainter(
                                data = image
                                    ?: Uri.parse("android.resource://tn.esprit.pdm/drawable/placeholder_image")
                            ),
                            contentDescription = "Default Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Button to pick a new image
                    Button(
                        onClick = { launcher1.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = skyBlue)
                    ) {
                        Text(text = "Pick Profile Image")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Editable text fields for user details
                    OutlinedTextField(
                        value = name ?: "",
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = skyBlue,
                            focusedLabelColor = skyBlue
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email ?: "",
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = skyBlue,
                            focusedLabelColor = skyBlue
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = phone ?: "",
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = skyBlue,
                            focusedLabelColor = skyBlue
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Add a dropdown menu for region selection here
                    ExposedDropdownMenuBox(
                        expanded = expandedRegion,
                        onExpandedChange = { expandedRegion = !expandedRegion }
                    ) {
                        OutlinedTextField(
                            value = region ?: "",
                            onValueChange = { region = it },
                            readOnly = true,
                            label = { Text("Select Region") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = skyBlue,
                                focusedLabelColor = skyBlue
                            ),
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedRegion,
                            onDismissRequest = { expandedRegion = false }
                        ) {
                            tunisianRegions.forEach { region1 ->
                                DropdownMenuItem(
                                    text = { Text(region1) },
                                    onClick = {
                                        region = region1
                                        expandedRegion = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedRole,
                        onExpandedChange = { expandedRole = !expandedRole }
                    ) {
                        OutlinedTextField(
                            value = role ?: "",
                            onValueChange = { role = it },
                            readOnly = true,
                            label = { Text("Select Role") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF87CEEB),
                                focusedLabelColor = Color(0xFF87CEEB)
                            ),
                            modifier = Modifier
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedRole,
                            onDismissRequest = { expandedRole = false }
                        ) {
                            userRoles.forEach { role1 ->
                                DropdownMenuItem(
                                    text = { Text(role1) },
                                    onClick = {
                                        role = role1
                                        expandedRole = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    if (role == "Mechanical") {
                        Spacer(modifier = Modifier.height(16.dp))
                        // Display selected images in a LazyRow
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(userAdditionalImagesUris.orEmpty()) { uri ->
                                Image(
                                    painter = rememberImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .padding(8.dp)
                                )
                            }

                            items(imageUri2.size) { index ->
                                val imageUri: Uri = imageUri2[index] // Fetch URI from the list
                                Image(
                                    painter = rememberImagePainter(
                                        data = imageUri.toString().takeIf { it.isNotEmpty() }
                                            ?: "android.resource://tn.esprit.pdm/drawable/placeholder_image" // Fallback placeholder
                                    ),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .padding(8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Button to pick images
                        Button(
                            onClick = { launcher2.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = skyBlue)
                        ) {
                            Text(text = "Pick More Images")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = chathamsBlue),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (isLoading) {
                            // Show "Deleting..." text when loading
                            Text(text = "Updating...")
                        } else {
                            // Default text when not loading
                            Text(text = "Update Profile")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Log.e("aa", "userid: $userId")
                    // Delete Account Button
                    Button(
                        onClick = {
                            if (userId != null) {  // Check if userId is available
                                isLoading = true
                                authviewModel.deleteUser(userId) { isSuccess ->
                                    isLoading = false
                                    if (isSuccess) {
                                        // Navigate to the signup screen after successful deletion
                                        navController.navigate("signup_screen")
                                    } else {
                                        // Show error message if deletion failed
                                        errorMessage = "Account deletion failed."
                                    }
                                }
                            } else {
                                // Handle the case where userId is null, e.g., show an error message
                                errorMessage = "User ID is missing."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = chathamsBlue),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (isLoading) {
                            // Show "Deleting..." text when loading
                            Text(text = "Deleting...")
                        } else {
                            // Default text when not loading
                            Text(text = "Delete Account")
                        }
                    }

                    // Display error message if exists
                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            /*Spacer(modifier = Modifier.height(24.dp))
        // Update Button
        Button(
            onClick = {
                isLoading = true
                authviewModel.updateUser(
                    name = name,
                    email = email,
                    phone = phone,
                    region = region,
                    role = role
                ) { isSuccess ->
                    isLoading = false
                    if (!isSuccess) {
                        errorMessage = "Update failed. Please try again."
                    } else {
                        errorMessage = "Profile updated successfully."
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = skyBlue)
        ) {
            if (isLoading) Text(text = "Updating...") else Text(text = "Update Profile")
        }

        // Show error message if update fails
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = Color.Red, fontSize = 14.sp)
        }*/
        }
    )
}

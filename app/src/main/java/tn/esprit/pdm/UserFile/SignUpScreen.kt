    package tn.esprit.pdm.UserFile

    import android.graphics.Bitmap
    import android.graphics.ImageDecoder
    import android.net.Uri
    import android.os.Build
    import android.provider.MediaStore
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.border
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.shape.CircleShape
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
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.PasswordVisualTransformation
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.hilt.navigation.compose.hiltViewModel
    import androidx.navigation.NavHostController
    import coil.compose.rememberImagePainter
    import tn.esprit.pdm.Chat.FireBaseViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SignUpScreen(navController: NavHostController, authViewModel: AuthViewModel) {

        //FireBase
        val firebaseviewModel: FireBaseViewModel = hiltViewModel()


        //Nest
        val skyBlue = Color(0xFF87CEEB)

        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }

        val tunisianRegions = listOf(
            "Tunis", "Ariana", "Ben Arous", "Manouba", "Nabeul", "Zaghouan", "Bizerte", "Béja",
            "Jendouba", "Kef", "Siliana", "Sousse", "Monastir", "Mahdia", "Kairouan", "Kasserine",
            "Sidi Bouzid", "Sfax", "Gabès", "Médenine", "Tataouine", "Gafsa", "Tozeur", "Kebili"
        )
        var expandedRegion by remember { mutableStateOf(false) }
        var selectedRegion by remember { mutableStateOf("") }

        val userRoles = listOf("Client", "Mechanical")
        var expandedRole by remember { mutableStateOf(false) }
        var selectedRole by remember { mutableStateOf("") }

        var imageUri1 by remember { mutableStateOf<Uri?>(null) }
        val context1 = LocalContext.current
        val launcher1 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri1= uri }

        var imageUri2 = remember { mutableStateListOf<Uri>() }
        val context2 = LocalContext.current
        val bitmap2 = remember { mutableStateListOf<Bitmap?>() }
        val launcher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
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

        // State for signup result
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val paddingValues = PaddingValues(top = 30.dp, bottom = 46.dp)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "SignUp", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Create Your Account")
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable { launcher1.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri1 != null) {
                        Image(
                            painter = rememberImagePainter(data = imageUri1),
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                    } else {
                        Text(text = "Tap to select ur image", color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            }

            item { // Add OutlinedTextField for name input
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Full Name") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = skyBlue,
                        focusedLabelColor = skyBlue
                    )
                )
            }

            item { // Add OutlinedTextField for email input
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email Address") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = skyBlue,
                        focusedLabelColor = skyBlue
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = password,
                    onValueChange = {password=it},
                    label = {Text(text="Password")},
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = skyBlue,
                        focusedLabelColor = skyBlue
                    ))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = confirmPassword,
                    onValueChange = {confirmPassword=it},
                    label = {Text(text="Confirm Password")},
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = skyBlue,
                        focusedLabelColor = skyBlue
                    ),
                    isError = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = phone,
                    onValueChange = {phone=it},
                    label = {Text(text="Phone Number")},
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = skyBlue,
                        focusedLabelColor = skyBlue
                    ))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Add a dropdown menu for region selection here
                ExposedDropdownMenuBox(
                    expanded = expandedRegion,
                    onExpandedChange = { expandedRegion = !expandedRegion }
                ) {
                    OutlinedTextField(
                        value = selectedRegion,
                        onValueChange = { },
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
                        tunisianRegions.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(region) },
                                onClick = {
                                    selectedRegion = region
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
                        value = selectedRole,
                        onValueChange = { },
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
                        userRoles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    selectedRole = role
                                    expandedRole = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                if (selectedRole == "Mechanical")
                {
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ){
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
                    Button(onClick = { launcher2.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = skyBlue)) { Text(text = "Pick More Images") }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                        } else {
                            isLoading = true
                            //fireBase
                            firebaseviewModel.signUpFireBase(imageUri1.toString(), name, email, password)

                            //nest
                            authViewModel.signup(
                                imageUrl = imageUri1.toString(),
                                name = name,
                                email = email,
                                password = password,
                                phone = phone,
                                role = selectedRole,
                                region = selectedRegion,
                                additionalImages = imageUri2.map { it.toString() }
                            ) { isSuccess ->
                                isLoading = false
                                if (!isSuccess) {
                                    navController.navigate("login_screen")
                                } else {
                                    errorMessage = "Signup failed"
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = skyBlue)
                ) {
                    if (isLoading) Text(text = "Signing Up...") else Text(text = "SignUp")
                }
            }

            // Show error message if signup fails
            errorMessage?.let {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = it, color = Color.Red, fontSize = 14.sp)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Already Have An Account! SignIn",
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
package tn.esprit.pdm.UserFile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import tn.esprit.pdm.Chat.FireBaseViewModel
import tn.esprit.pdm.Models.User
import tn.esprit.pdm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, authViewModel: AuthViewModel, onLoginSuccess: (User) -> Unit) {
    //FireBase
    val firebaseviewModel: FireBaseViewModel = hiltViewModel()


    //Nest
    val authViewModel1: AuthViewModel = viewModel()

    val skyBlue = Color(0xFF87CEEB)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPassword by remember { mutableStateOf(false) }
    var showOtp by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var showChangePassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Login Button Click Action
    val onLoginClick = {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            isLoading = true

            //FireBase
            firebaseviewModel.loginFireBase(email, password)

            //Nest
            authViewModel.login(email, password) { success ->
                isLoading = false
                if (success) {
                    Log.d("LoginScreen", "User: ${authViewModel.user}") // Log the user data
                    if (authViewModel.user != null) {
                        onLoginSuccess(authViewModel.user!!)
                        navController.navigate("home_screen")
                    } else {
                        errorMessage = "User data is not available. Please try again."
                    }
                } else {
                    errorMessage = authViewModel.loginError ?: "Login failed. Please try again."
                }
            }
        } else {
            errorMessage = "Email and Password are required!"
        }
    }

    val paddingValues = PaddingValues(top = 30.dp, bottom = 46.dp)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Image(painter = painterResource(id = R.drawable.car1), contentDescription = "Login Image", modifier = Modifier.size(300.dp))

            Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Login To Your Account")
        }

        item{
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email Address") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = skyBlue,
                    focusedLabelColor = skyBlue
                ))
        }

        item{
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = skyBlue,
                    focusedLabelColor = skyBlue
                ))
        }

        item{
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = skyBlue)) {
                Text(text = if (isLoading) "Logging in..." else "Login")
            }
        }

        item{
            Spacer(modifier = Modifier.height(16.dp))
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Forgot Password ?", modifier = Modifier.clickable { scope.launch { showForgotPassword = true } })
        }

        item{
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Or SignIn With")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(painter = painterResource(id = R.drawable.facebook1),
                    contentDescription = "Facebook Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {}
                )

                Image(painter = painterResource(id = R.drawable.google1),
                    contentDescription = "Google Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {}
                )
            }
        }

        item{
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Don't Have An Account! SignUp", modifier = Modifier.clickable { navController.navigate("signup_screen") })
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Forgot Password Modal
    if (showForgotPassword) {
        ForgotPasswordSheet(
            onDismiss = { showForgotPassword = false },
            onSubmit = {
                showForgotPassword = false
                showOtp = true
            },
            authViewModel1
        )
    }

    // OTP Confirmation Modal
    if (showOtp) {
        OtpConfirmationSheet(
            otpCode = otpCode,
            onOtpChange = { otpCode = it },
            onDismiss = { showOtp = false },
            onResendCode = { /* Handle resend code logic */ },
            onChangeEmail = { showForgotPassword = true; showOtp = false },
            onConfirm = { showOtp = false; showChangePassword = true },
            authViewModel1
        )
    }

    // Change Password Modal
    if (showChangePassword) {
        ChangePasswordSheet(
            onDismiss = { showChangePassword = false },
            onSubmit = { navController.navigate("login_screen") },
            authViewModel1
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordSheet(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    authViewModel: AuthViewModel
) {
    val skyBlue = Color(0xFF87CEEB)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var email2 by remember { mutableStateOf("") }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Reset Your Password", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Enter your email to reset your password")

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = email2,
                onValueChange = {email2=it},
                label = {Text(text="Email Adress")},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = skyBlue,
                    focusedLabelColor = skyBlue
                ))

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.forgotPassword(email2) { success ->
                    if (success) {
                        onSubmit()
                    } else {
                        Log.e("aa", "9999")
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = skyBlue)) {
                Text("Submit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpConfirmationSheet(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onResendCode: () -> Unit,
    onChangeEmail: () -> Unit,
    onConfirm: () -> Unit,
    authViewModel: AuthViewModel
) {
    val skyBlue = Color(0xFF87CEEB)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Enter OTP", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = otpCode,
                onValueChange = onOtpChange,
                label = { Text("OTP Code") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = skyBlue,
                    focusedLabelColor = skyBlue),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { authViewModel.verifyOtp(otpCode) { success ->
                if (success) {
                    onConfirm() // Switch to Change Password screen
                } else {
                    // Show error to the user
                }
            } },
                colors = ButtonDefaults.buttonColors(containerColor = skyBlue)) {
                Text("Confirm")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Resend Code",
                    modifier = Modifier.clickable { onResendCode() },
                    color = skyBlue
                )
                Text(
                    text = "Change Email",
                    modifier = Modifier.clickable { onChangeEmail() },
                    color = skyBlue
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordSheet(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    authViewModel: AuthViewModel
) {
    val skyBlue = Color(0xFF87CEEB)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Change Password", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text("Old Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = skyBlue,
                    focusedLabelColor = skyBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = skyBlue,
                    focusedLabelColor = skyBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm New Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = skyBlue,
                    focusedLabelColor = skyBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.resetPassword(newPassword) { success ->
                if (success) {
                    onSubmit() // Navigate to login screen
                } else {
                    // Show error to the user
                }
            } },
                colors = ButtonDefaults.buttonColors(containerColor = skyBlue)) {
                Text("Submit")
            }
        }
    }
}

/*@preview(showBackground=true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}*/
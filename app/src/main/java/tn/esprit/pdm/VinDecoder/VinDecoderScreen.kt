package tn.esprit.pdm.VinDecoder

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VinDecoderScreen(navController: NavHostController,  viewModel: VinDecoderViewModel = viewModel()) {
    val vinData by viewModel.vinData.collectAsState()
    val skyBlue = Color(0xFF87CEEB)
    val chathamsBlue = Color(0xFF233952)

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
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "VIN Decoder", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                var vinInput by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = vinInput,
                    onValueChange = { vinInput = it },
                    label = { Text("Enter VIN") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = skyBlue,
                        focusedLabelColor = skyBlue
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // MapButton to trigger VIN decoding
                Button(
                    onClick = { viewModel.fetchVinData(vinInput) },
                    colors = ButtonDefaults.buttonColors(containerColor = chathamsBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Connect Your Car")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = vinData ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    )
}

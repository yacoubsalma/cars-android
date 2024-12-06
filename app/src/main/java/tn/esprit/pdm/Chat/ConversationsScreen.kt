package tn.esprit.pdm.Chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(navController: NavController) {
    val viewModel = hiltViewModel<ConversationsViewModel>()
    val channels = viewModel.channels.collectAsState()
    val addChannel = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val skyBlue = Color(0xFF87CEEB)

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
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(skyBlue)
                    .clickable {
                        addChannel.value = true
                    }
            ) {
                Text(
                    text = " + ",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(channels.value) { channel ->
                    Column {
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = channel.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.LightGray)
                                .clickable {
                                    navController.navigate("chat/${channel.id}")
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }

    if (addChannel.value) {
        ModalBottomSheet(
            onDismissRequest = { addChannel.value = false },
            sheetState = sheetState
        ) {
            AddChannelDialog {
                viewModel.addChannel(it)
                addChannel.value = false
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val skyBlue = Color(0xFF87CEEB)
    val channelName = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Channel", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(16.dp))
        OutlinedTextField(value = channelName.value,
            onValueChange = { channelName.value = it },
            label = { Text(text = "Channel Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = skyBlue,
                focusedLabelColor = skyBlue
            ))
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = { onAddChannel(channelName.value) },
            colors = ButtonDefaults.buttonColors(containerColor = skyBlue)) {
            Text(text = "Add")
        }
    }
}
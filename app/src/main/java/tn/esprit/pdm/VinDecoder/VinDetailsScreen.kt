package tn.esprit.pdm.VinDecoder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tn.esprit.pdm.Models.VinDetails

@Composable
fun VinDetailsScreen(vinDetails: VinDetails?) {
    // Show a loading or error message if vinDetails is null
    if (vinDetails == null) {
        Text(text = "No VIN data available.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Heading
        Text(
            text = "VIN Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Create a Table-like Layout using Column and Row
        Column {
            // First row with Make and Model
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Make", style = MaterialTheme.typography.bodyMedium)
                Text(vinDetails.make ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Model", style = MaterialTheme.typography.bodyMedium)
                Text(vinDetails.model ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Year", style = MaterialTheme.typography.bodyMedium)
                Text(vinDetails.modelYear?.toString() ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Body Style", style = MaterialTheme.typography.bodyMedium)
                Text(vinDetails.bodyStyle ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Engine Type", style = MaterialTheme.typography.bodyMedium)
                Text(vinDetails.engineType ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Fuel Type", style = MaterialTheme.typography.bodyMedium)
                Text(vinDetails.fuelType ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Vehicle Class", style = MaterialTheme.typography.bodyMedium)
                Text(vinDetails.vehicleClass ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Add more fields in a similar way...
        }
    }
}

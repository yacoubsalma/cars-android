package tn.esprit.pdm.VinDecoder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class VinDecoderViewModel : ViewModel() {
    var isConnected = false
    private val client = OkHttpClient()

    private val _ocrText = MutableStateFlow<String?>(null)
    val ocrText: StateFlow<String?> get() = _ocrText

    private val _vinData = MutableStateFlow<String?>(null)
    val vinData: StateFlow<String?> get() = _vinData

    // Function to decode VIN
    fun fetchVinData(vin: String) {
        val request = Request.Builder()
            .url("https://vin-decoder-europe2.p.rapidapi.com/vin_decoder?vin=$vin")
            .get()
            .addHeader("x-rapidapi-key", "49d17ddcb9msh2a1c963651246e7p1224c6jsn6941dadf0f2b")
            .addHeader("x-rapidapi-host", "vin-decoder-europe2.p.rapidapi.com")
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    isConnected=true
                    val jsonResponse = response.body?.string()
                    val parsedData = JSONObject(jsonResponse ?: "{}").toString(4) // Pretty JSON
                    _vinData.value = parsedData
                } else {
                    _vinData.value = "VIN Error: ${response.message}"
                }
            } catch (e: Exception) {
                _vinData.value = "VIN Exception: ${e.message}"
            }
        }
    }
}

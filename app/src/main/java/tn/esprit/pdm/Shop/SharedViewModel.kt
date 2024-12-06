package tn.esprit.pdm.Shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tn.esprit.pdm.Models.ProductUiModel

class SharedViewModel : ViewModel() {
    private val _selectedProduct = MutableStateFlow<ProductUiModel?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    fun selectProduct(product: ProductUiModel) {
        viewModelScope.launch {
            _selectedProduct.value = product
        }
    }
}
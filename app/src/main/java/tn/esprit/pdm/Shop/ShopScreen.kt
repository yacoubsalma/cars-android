package tn.esprit.pdm.Shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import tn.esprit.pdm.Models.ProductUiModel
import tn.esprit.pdm.Shop.component.CategoriesList
import tn.esprit.pdm.Shop.component.ProductHorizontalList
import tn.esprit.pdm.Shop.component.ProductSmallCard
import tn.esprit.pdm.Shop.mock.generateProducts
import tn.esprit.pdm.ui.theme.Accent
import tn.esprit.pdm.ui.theme.DarkText
import tn.esprit.pdm.ui.theme.MediumText

@Composable
fun ShopScreen(
    viewModel: SharedViewModel,
    navController: NavController
){
    HomeContent(
        onProductClick = { product ->
            viewModel.selectProduct(product)
            navController.navigate("product_details_screen")
        }
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onProductClick: (ProductUiModel) -> Unit
) {
    val productList by remember { mutableStateOf(generateProducts()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .padding(start = 22.dp),
                text = "Auto Parts Store",
                color = MediumText,
                fontSize = 42.sp,
                fontWeight = FontWeight.Medium
            )
        }

        item {
            CategoriesList(
                modifier = Modifier
                    .padding(top = 28.dp)
            )
        }

        item {
            ProductHorizontalList(
                modifier = Modifier
                    .padding(top = 22.dp),
                productList = productList,
                onProductClick = { product ->
                    onProductClick(product)
                }
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 34.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Popular",
                    color = DarkText,
                    fontSize = 12.sp
                )

                Text(
                    text = "See All",
                    color = Accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(productList.reversed()) { product ->
            ProductSmallCard(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .padding(bottom = 16.dp),
                product = product,
                onProductClick = { product ->
                    onProductClick(product)
                },
                onAddToCartClick = {

                }
            )
        }

        item { Spacer(modifier = Modifier.height(50.dp)) }
    }
}
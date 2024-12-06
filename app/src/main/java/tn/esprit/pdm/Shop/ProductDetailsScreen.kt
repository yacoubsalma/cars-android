package tn.esprit.pdm.Shop

import android.graphics.drawable.Icon
import android.hardware.lights.Light
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import tn.esprit.pdm.Models.ProductUiModel
import tn.esprit.pdm.ui.theme.Accent
import tn.esprit.pdm.ui.theme.Alternative_1
import tn.esprit.pdm.ui.theme.Alternative_2
import tn.esprit.pdm.ui.theme.Background
import tn.esprit.pdm.ui.theme.Border
import tn.esprit.pdm.ui.theme.DarkText
import tn.esprit.pdm.ui.theme.Favorite
import tn.esprit.pdm.ui.theme.IconTint
import tn.esprit.pdm.ui.theme.LightText
import tn.esprit.pdm.ui.theme.MediumText
import tn.esprit.pdm.ui.theme.Primary
import tn.esprit.pdm.ui.theme.RegularText
import tn.esprit.pdm.ui.theme.Shadow
import tn.esprit.pdm.ui.theme.Star

private const val DURATION = 600

@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel,
    navController: NavController
) {
    val product by viewModel.selectedProduct.collectAsState()

    product?.let {
        ProductDetailsContent(
            modifier = modifier,
            product = it,
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun ProductDetailsContent(
    modifier: Modifier = Modifier,
    product: ProductUiModel,
    onBackClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    var xOffset by remember { mutableStateOf(800.dp) }
    var yOffset by remember { mutableStateOf(800.dp) }
    var buttonScale by remember { mutableFloatStateOf(0f) }
    var iconScale by remember { mutableFloatStateOf(0f) }
    var sneakerScale by remember { mutableFloatStateOf(0.6f) }
    var sneakerRotate by remember { mutableFloatStateOf(-60f) }

    val animatedXOffset = animateDpAsState(
        targetValue = xOffset,
        label = "",
        animationSpec = tween(durationMillis = DURATION, easing = FastOutLinearInEasing)
    )

    val animatedYOffset = animateDpAsState(
        targetValue = yOffset,
        label = "",
        animationSpec = tween(durationMillis = DURATION, easing = FastOutLinearInEasing)
    )

    val animatedButtonScale = animateFloatAsState(
        targetValue = buttonScale,
        label = "",
        animationSpec = tween(easing = FastOutLinearInEasing)
    )

    val animatedIconScale = animateFloatAsState(
        targetValue = iconScale,
        label = "",
        animationSpec = tween(easing = FastOutLinearInEasing)
    )

    val animatedSneakerScale = animateFloatAsState(
        targetValue = sneakerScale,
        label = "",
        animationSpec = tween(durationMillis = DURATION, easing = FastOutLinearInEasing)
    )

    val animatedSneakerRotate = animateFloatAsState(
        targetValue = sneakerRotate,
        label = "",
        animationSpec = tween(durationMillis = DURATION, easing = FastOutLinearInEasing)
    )

    LaunchedEffect(true) {
        delay(150)
        xOffset = 140.dp
        yOffset = -130.dp
        sneakerScale = 1f
        sneakerRotate = -25f
        delay(400)
        iconScale = 1f
        delay(100)
        buttonScale = 1f
    }

    Box(
        modifier = modifier
            .background(Background)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .offset(x = animatedXOffset.value, y = animatedYOffset.value)
                .alpha(0.3f)
                .size(400.dp)
                .background(product.color, shape = CircleShape)
        )

        IconButton(
            modifier = Modifier
                .padding(start = 22.dp)
                .padding(top = 42.dp)
                .shadow(elevation = 24.dp, spotColor = Shadow, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .size(36.dp),
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Back Icon",
                tint = IconTint
            )
        }

        Column {
            Image(
                modifier = Modifier
                    .scale(animatedSneakerScale.value)
                    .rotate(animatedSneakerRotate.value)
                    .padding(end = 48.dp)
                    .padding(top = 30.dp)
                    .size(320.dp),
                painter = painterResource(id = product.imageResource),
                contentDescription = "Product Image"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .padding(top = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Auto Part",
                        color = LightText,
                        fontSize = 10.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 2.dp),
                        text = "Product ${product.name}",
                        color = DarkText,
                        fontSize = 22.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(18.dp),
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Rating Icon",
                            tint = Star
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            textAlign = TextAlign.Center,
                            text = product.rating.toString(),
                            color = MediumText,
                            fontSize = 12.sp,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    text = "$${product.price}",
                    color = Accent,
                    fontSize = 36.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .padding(top = 24.dp),
                text = "Introducing the \"TurboEdge Pro,\" a groundbreaking auto part engineered to redefine vehicle performance and reliability. Crafted with precision from premium-grade, durable materials, this component ensures unmatched efficiency and longevity. Its innovative design seamlessly integrates with modern automotive systems, enhancing engine responsiveness and fuel efficiency. The TurboEdge Pro is built to endure extreme conditions, making it the ultimate choice for drivers who demand top-tier performance and dependability on every journey.",
                color = LightText,
                lineHeight = 20.sp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Justify,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Spacer(modifier = Modifier.weight(1.0f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            ) {
                IconButton(
                    modifier = Modifier
                        .scale(animatedIconScale.value),
                    onClick = { isFavorite = !isFavorite }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite Icon",
                        tint = if (isFavorite) Favorite else IconTint
                    )
                }

                Button(
                    modifier = Modifier
                        .scale(animatedButtonScale.value)
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent
                    ),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ShoppingCart,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Add to cart")
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

        }
    }
}
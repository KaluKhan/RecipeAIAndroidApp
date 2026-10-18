package com.sankhlatraders.recipeai.ui.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sankhlatraders.recipeai.ui.theme.BrandGold
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.DarkBackground
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val inifiniteTransition = rememberInfiniteTransition(label = "splash_pulse")

    //pulsing glow effect for emoji
    val emojiScale by inifiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emoji_pulse"
    )

    //fade-in animation
    var contentAlpha by remember { mutableStateOf(0f) }
    val alphaAnim by animateFloatAsState(
        targetValue = contentAlpha,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    var scale by remember { mutableStateOf(0.6f) }
    val scaleAnim by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        contentAlpha = 1f
        scale = 1f
        delay(2200.milliseconds)
        onNavigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A0A00),
                        DarkBackground
                    ), radius = 900f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .alpha(alphaAnim)
                .scale(scaleAnim)
        ) {
            //App icon emoji with pulse
            Text(text = "Splash", fontSize = 88.sp, modifier = Modifier.scale(emojiScale))
            Spacer(modifier = Modifier.height(8.dp))
            //App Name
            Text(
                text = "RecipeAI", fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                style = androidx.compose.material3.MaterialTheme.typography.displaySmall.copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(BrandOrange, BrandGold)
                    )
                )
            )

            //Tagline
            Text(
                text = "Discover. Cook. Delight.",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Light,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            //AI badge
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "AI", fontSize = 14.sp)
                Text(
                    text = "Powered by Gemini AI",
                    fontSize = 13.sp,
                    color = BrandGold.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Text(text = "AI", fontSize = 14.sp)
            }
        }
        //Bottom loading indicator
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .alpha(alphaAnim)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = BrandOrange,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Warming up the kitchen...",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}
package com.sankhlatraders.recipeai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sankhlatraders.recipeai.ui.navigation.AppNavigation
import com.sankhlatraders.recipeai.ui.screens.splash.SplashScreen
import com.sankhlatraders.recipeai.ui.theme.DarkBackground
import com.sankhlatraders.recipeai.ui.theme.RecipeAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAITheme(darkTheme = true) {
                var showSplash by remember { mutableStateOf(true) }
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBackground) {
                    AnimatedContent(targetState = showSplash, transitionSpec = {
                        fadeIn(tween(500)) togetherWith fadeOut(tween(400))
                    }, label = "splash_to_main") { isSplash ->
                        if (isSplash) {
                            SplashScreen(onNavigateToMain = { showSplash = false })
                        } else {
                            AppNavigation()
                        }
                    }
                }
            }
        }
    }
}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipeAITheme {
        Greeting("Android")
    }
}*/

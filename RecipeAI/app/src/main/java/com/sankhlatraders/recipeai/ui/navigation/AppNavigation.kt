package com.sankhlatraders.recipeai.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sankhlatraders.recipeai.ui.screens.ai.AiAgentScreen
import com.sankhlatraders.recipeai.ui.screens.contact.ContactUsScreen
import com.sankhlatraders.recipeai.ui.screens.detail.DetailScreen
import com.sankhlatraders.recipeai.ui.screens.main.MainScreen
import com.sankhlatraders.recipeai.ui.theme.BrandGold
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.DarkBackground
import com.sankhlatraders.recipeai.ui.theme.DarkSurface
import com.sankhlatraders.recipeai.ui.theme.Divider
import com.sankhlatraders.recipeai.ui.theme.TextHint
import com.sankhlatraders.recipeai.ui.theme.TextSecondary
import com.sankhlatraders.recipeai.viewmodel.AiUiState
import com.sankhlatraders.recipeai.viewmodel.AiViewModel
import com.sankhlatraders.recipeai.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val mainViewModel: MainViewModel = hiltViewModel()
    val aiViewModel: AiViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Main.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                currentRoute = currentRoute,
                onNavigate = { rout ->
                    scope.launch { drawerState.close() }
                    navController.navigate(rout) {
                        popUpTo(Screen.Main.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        },
        scrimColor = Color(0x99000000)
    ) {
        Scaffold(
            containerColor = DarkBackground,
            floatingActionButton = {
                AnimatedVisibility(
                    visible = currentRoute == Screen.Main.route,
                    enter = scaleIn(tween(200)) + fadeIn(tween(200)),
                    exit = scaleOut(tween(200)) + fadeOut(tween(200))
                ) {
                    AiFab(onclick = { navController.navigate(Screen.AiAgent.route) })
                }
            }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Main.route,
                modifier = Modifier.padding(innerPadding),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    ) + fadeIn(tween(300))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(300)
                    ) + fadeOut(tween(300))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(300)
                    ) + fadeIn(tween(300))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    ) + fadeOut(tween(300))
                }) {
                composable(Screen.Main.route) {
                    MainScreen(onRecipeClick = { recipeId ->
                        navController.navigate(Screen.Details.createRoute(recipeId))
                    }, drawerState = drawerState, viewModel = mainViewModel)
                }
                composable(Screen.Details.route) { backStackEntry ->
                    val recipeId =
                        backStackEntry.arguments?.getString("recipeId") ?: return@composable
                    //try main VM first then AI VM
                    val recipe = mainViewModel.getRecipeById(recipeId) ?: run {
                        val aiState = aiViewModel.uiState.value
                        if (aiState is AiUiState.Success) {
                            aiState.recipes.find { it.id == recipeId }
                        } else null
                    }
                    if (recipe == null) {
                        LaunchedEffect(Unit) { navController.popBackStack() }
                        return@composable
                    }
                    DetailScreen(recipe = recipe, onBackClick = { navController.popBackStack() })
                }
                composable(Screen.AiAgent.route) {
                    AiAgentScreen(onRecipeClick = { recipeId ->
                        navController.navigate(
                            Screen.Details.createRoute(
                                recipeId
                            )
                        )
                    }, onBackClick = { navController.popBackStack() }, viewModel = aiViewModel)
                }
                composable(Screen.ContactUs.route) {
                    ContactUsScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }
    }
}

@Composable
private fun AiFab(onclick: () -> Unit) {
    FloatingActionButton(
        onClick = onclick, containerColor = Color.Transparent,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(BrandOrange, BrandGold)))

    ) {
        Icon(
            Icons.Rounded.AutoAwesome,
            contentDescription = "AI Chef",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun NavigationDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(DarkSurface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color(0xFF1A0800), DarkSurface)))
                    .statusBarsPadding()
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(BrandOrange, BrandGold))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = ":)", fontSize = 32.sp)
                        }
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = TextSecondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "RecipeAI",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Discover. Cook. Delight",
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            HorizontalDivider(color = Divider)
            Spacer(modifier = Modifier.height(8.dp))

            DrawerNavItem(
                icon = Icons.Rounded.Home,
                label = "Home",
                isSelected = currentRoute == Screen.Main.route,
                onClick = { onNavigate(Screen.Main.route) })

            DrawerNavItem(
                icon = Icons.Rounded.AutoAwesome,
                label = "AI Chef Assistant",
                isSelected = currentRoute == Screen.AiAgent.route,
                onClick = { onNavigate(Screen.AiAgent.route) },
                badge = "AI"
            )

            DrawerNavItem(
                icon = Icons.Rounded.ContactMail,
                label = "Contact Us",
                isSelected = currentRoute == Screen.ContactUs.route,
                onClick = { onNavigate(Screen.ContactUs.route) })
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(color = Divider)
            Box(modifier = Modifier.padding(20.dp)) {
                Column {
                    Text(
                        text = "RecipeAI v1.0",
                        color = TextHint,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "AI_Icon Powered by Gemini AI",
                        color = BrandGold.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badge: String? = null
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                icon,
                contentDescription = label,
                tint = if (isSelected) BrandOrange else TextSecondary
            )
        },
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
                badge?.let {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = BrandGold.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = BrandGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = BrandOrange.copy(alpha = 0.12f),
            unselectedContainerColor = Color.Transparent
        )
    )
}
package com.sankhlatraders.recipeai.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.data.model.RecipeCategory
import com.sankhlatraders.recipeai.ui.components.RecipeCard
import com.sankhlatraders.recipeai.ui.components.RecipeSearchBar
import com.sankhlatraders.recipeai.ui.theme.BrandGold
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.DarkBackground
import com.sankhlatraders.recipeai.ui.theme.TextSecondary
import com.sankhlatraders.recipeai.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onRecipeClick: (String) -> Unit,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val recipes by viewModel.filteredRecipes.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        //Hero header
        HeroHeader(onMenuClick = { scope.launch { drawerState.open() } })

        //Search bar
        RecipeSearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            placeholder = "Search recipes,cuisines...",
            modifier = Modifier.padding(top = 12.dp)
        )

        //Category chips
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (searchQuery.isBlank() && selectedCategory == RecipeCategory.ALL) "Featured Recipes" else "Results",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${recipes.size} recipes",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Staggered recipe grid
        if (recipes.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            RecipeStaggeredGrid(
                recipes = recipes,
                onRecipeClick = onRecipeClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HeroHeader(onMenuClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF1A0800),
                        DarkBackground
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "RecipeAI",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        brush = Brush.horizontalGradient(
                            listOf(
                                BrandOrange, BrandGold
                            )
                        )
                    )
                )
                Text(
                    text = "Discover. Cook. Delight",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 1.5.sp
                )
            }

            //Placeholder for symmetry
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun RecipeStaggeredGrid(
    recipes: List<Recipe>,
    onRecipeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp
    ) {
        itemsIndexed(items = recipes, key = { _, recipe -> recipe.id }) { index, recipe ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(300, delayMillis = index * 50)) +
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(300, delayMillis = index * 50)
                        )
            )
            {
                RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Try a different search or category. \nOr ask the AI Chef for suggestions!",
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
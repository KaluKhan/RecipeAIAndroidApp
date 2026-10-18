package com.sankhlatraders.recipeai.ui.screens.ai

import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.GpsFixed
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.ui.components.FlowRow
import com.sankhlatraders.recipeai.ui.components.RecipeCard
import com.sankhlatraders.recipeai.ui.theme.AccentRed
import com.sankhlatraders.recipeai.ui.theme.BrandGold
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.DarkBackground
import com.sankhlatraders.recipeai.ui.theme.DarkSurfaceVariant
import com.sankhlatraders.recipeai.ui.theme.TextHint
import com.sankhlatraders.recipeai.ui.theme.TextSecondary
import com.sankhlatraders.recipeai.viewmodel.AiUiState
import com.sankhlatraders.recipeai.viewmodel.AiViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AiAgentScreen(
    onRecipeClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: AiViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locationInput by viewModel.locationInput.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isLocationLoading by viewModel.isLocationLoading.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    // Location permission
    val locationPermission = rememberPermissionState(ACCESS_FINE_LOCATION)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        //Top bar
        AiTopBar(onBackClick = onBackClick)

        // Scrollable content
        when (val state = uiState) {
            is AiUiState.Idle, is AiUiState.Error -> {
                // Input form
                AiInputSection(
                    locationInput = locationInput,
                    searchQuery = searchQuery,
                    isLocationLoading = isLocationLoading,
                    onLocationChange = viewModel::onLocationInputChange,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onGetLocation = {
                        if (locationPermission.status.isGranted) {
                            viewModel.fetchGpsLocation()
                        } else {
                            locationPermission.launchPermissionRequest()
                        }
                    },
                    onSuggest = {
                        focusManager.clearFocus()
                        viewModel.getSuggestions()
                    },
                    modifier = Modifier.weight(1f)
                )
                if (state is AiUiState.Error) {
                    ErrorBanner(
                        message = state.message,
                        onRetry = { viewModel.getSuggestions() })
                }
            }

            is AiUiState.Loading -> {
                // Loading indicator
                AiLoadingState(modifier = Modifier.weight(1f))
            }

            is AiUiState.Success -> {
                Column(modifier = Modifier.weight(1f)) {
                    // Context bar
                    AiResultsContextBar(
                        locationContex = state.locationContext,
                        recipeCount = state.recipes.size,
                        onNewSearch = viewModel::reset
                    )
                    //Staggered grid
                    AiRecipesGrid(recipes = state.recipes, onRecipeClick = onRecipeClick)
                }
            }
        }
    }
}

@Composable
private fun AiTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Color(0xFF1A0800), DarkBackground)))
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(4.dp))

            //AI icon with animated gradient
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                BrandOrange, BrandGold
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "AI Chef Assistant",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Powered by Gemini",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun AiInputSection(
    locationInput: String,
    searchQuery: String,
    isLocationLoading: Boolean,
    onLocationChange: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onGetLocation: () -> Unit,
    onSuggest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Hero illustration
        AiHeroIllustration()
        Text(
            text = "What would you like to cook?",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Text(
            text = "Tell me your location for regional recipes, or search by meal type - breakfast," +
                    " lunch, dinner, coolers, veg, and more.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Location field
        OutlinedTextField(
            value = locationInput,
            onValueChange = onLocationChange,
            label = { Text("Location (e.g. Mumbai, India)", color = TextHint) },
            placeholder = { Text("Enter city / country", color = TextHint) },
            leadingIcon = {
                Icon(
                    Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = BrandOrange
                )
            },
            trailingIcon = {
                if (isLocationLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = BrandOrange,
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onGetLocation) {
                        Icon(
                            Icons.Rounded.GpsFixed,
                            contentDescription = "Use GPS",
                            tint = BrandOrange
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = Color(0xFF333333),
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = BrandOrange
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        // Search field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Recipe type / category", color = TextHint) },
            placeholder = { Text("e.g. breakfast, vegan lunch, Italian dinner", color = TextHint) },
            leadingIcon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    tint = BrandOrange
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = Color(0xFF333333),
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = BrandOrange
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSuggest() })
        )

        //Quick suggestion chips
        QuickSuggestionChips(onChipClick = onSearchQueryChange)

        //CTA Button
        Button(
            onClick = onSuggest,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.horizontalGradient(listOf(BrandOrange, BrandGold))),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Gen AI Recipe Suggestions",
                        color = Color.White,
                        fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickSuggestionChips(onChipClick: (String) -> Unit) {
    val suggestions = listOf(
        "breakfast emoji Breakfast",
        "lunch emoji Lunch",
        "dinner emoji Dinner",
        "Cooler emoji Coolers",
        "Vegetarian emoji Vegetarian",
        "non-veg emoji Non-Veg",
        "desserts emoji Desserts"
    )

    Column {
        Text(
            text = "Quick picks:",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
            suggestions.forEach { suggestion ->
                SuggestionChip(
                    onClick = {
                        onChipClick(
                            suggestion.removePrefix("breakfast emoji ")
                                .removePrefix("lunch emoji ")
                                .removePrefix("dinner emoji ")
                                .removePrefix("Cooler emoji ")
                                .removePrefix("Vegetarian emoji ")
                                .removePrefix("non-veg emoji ")
                                .removePrefix("desserts emoji ")
                        )
                    },
                    label = {
                        Text(
                            suggestion,
                            style = MaterialTheme.typography.labelMedium,
                            color = BrandOrange
                        )
                    },
                    shape = RoundedCornerShape(50.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = BrandOrange.copy(
                            alpha = 0.1f
                        )
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = BrandOrange.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }
}

@Composable
private fun AiHeroIllustration() {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_hero")
    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        ),
        label = "rotate"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), contentAlignment = Alignment.Center
    ) {
        //Outer ring
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(
                            BrandOrange.copy(alpha = 0.3f),
                            BrandGold.copy(alpha = 0.3f),
                            BrandOrange.copy(alpha = 0.3f)
                        )
                    )
                )
                .rotate(rotateAnim)
        )
        // Inner circle
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(DarkSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "AI bot emoji", fontSize = 36.sp)
        }
    }
}

@Composable
private fun AiLoadingState(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(900), repeatMode = RepeatMode.Reverse),
        label = "loading_alpha"
    )
    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(1500, easing = LinearEasing)),
        label = "loading_rotate"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(BrandOrange, BrandGold, BrandOrange)
                    )
                )
                .rotate(rotateAnim),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(DarkBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "AI emoji", fontSize = 32.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Chef AI is thinking...",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = alpha),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Crafting personalized recipes just for you",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AiResultsContextBar(
    locationContext: String,
    resultCount: Int,
    onNewSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurfaceVariant)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$resultCount AI-curated recipes",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "location pin emoji $locationContext",
                style = MaterialTheme.typography.labelSmall,
                color = BrandOrange
            )
        }

        TextButton(onClick = onNewSearch) {
            Icon(
                Icons.Rounded.Refresh,
                contentDescription = null,
                tint = BrandOrange,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "New search",
                style = MaterialTheme.typography.labelMedium,
                color = BrandOrange
            )
        }
    }
}

@Composable
private fun AiRecipesGrid(recipes: List<Recipe>, onRecipeClick: (String) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 12.dp, end = 12.dp, top = 12.dp, bottom = 80.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp
    ) {
        itemsIndexed(item = recipes, key = { _, recipe -> recipe.id }) { index, recipe ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(300, delayMillis = index * 60)) +
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(300, delayMillis = index * 60)
                        )
            ) {
                RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
            }
        }
    }
}

@Composable
private fun ErrorBanner(message: String, onRetry: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = AccentRed.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, AccentRed.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Rounded.Warning,
                    contentDescription = null,
                    tint = AccentRed,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = AccentRed,
                    modifier = Modifier.weight(1f)
                )
            }
            TextButton(onClick = onRetry) {
                Text(text = "Retry", color = AccentRed)
            }
        }
    }
}
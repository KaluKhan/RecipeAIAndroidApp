package com.sankhlatraders.recipeai.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sankhlatraders.recipeai.data.model.Difficulty
import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.data.model.RecipeCategory
import com.sankhlatraders.recipeai.ui.theme.AccentGreen
import com.sankhlatraders.recipeai.ui.theme.AccentRed
import com.sankhlatraders.recipeai.ui.theme.BrandGold
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.ChipBreakFast
import com.sankhlatraders.recipeai.ui.theme.ChipCoolers
import com.sankhlatraders.recipeai.ui.theme.ChipDesserts
import com.sankhlatraders.recipeai.ui.theme.ChipDinner
import com.sankhlatraders.recipeai.ui.theme.ChipLunch
import com.sankhlatraders.recipeai.ui.theme.ChipNonVeg
import com.sankhlatraders.recipeai.ui.theme.ChipSnacks
import com.sankhlatraders.recipeai.ui.theme.ChipVeg
import com.sankhlatraders.recipeai.ui.theme.DarkCardBg
import com.sankhlatraders.recipeai.ui.theme.TextSecondary

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "card_scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Thumbnail with gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
            ) {
                AsyncImage(
                    model = recipe.thumbnailUrl,
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x80000000),
                                    Color(0xCC000000)
                                ),
                                startY = 0.3f * Float.MAX_VALUE
                            )
                        )
                )

                //Category chip
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = getCategoryColor(recipe.category).copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "${recipe.category.emoji} ${recipe.category.displayName}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                // Rating badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Surface(shape = RoundedCornerShape(50.dp), color = Color(0xCC000000)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = null,
                                tint = BrandGold,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "%.1f".format(recipe.rating),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                //Title overlay at bottom
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
            // Info row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cook time
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        tint = BrandGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${recipe.cookTimeMinutes + recipe.cookTimeMinutes}m",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                //Difficulty badge
                DifficultyBadge(difficulty = recipe.difficulty)

                //Diet tag
                Text(
                    text = if (recipe.isVegan) "leaf emoji Vegan"
                    else if (recipe.isVegetarian) "leaf emoji Veg"
                    else "non-veg emoji Non-Veg",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (recipe.isVegetarian) AccentGreen else AccentRed
                )
            }
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: Difficulty) {
    val (color, label) = when (difficulty) {
        Difficulty.EASY -> AccentGreen to "Easy"
        Difficulty.MEDIUM -> BrandGold to "Medium"
        Difficulty.HARD -> AccentRed to "Hard"
    }
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun getCategoryColor(category: RecipeCategory): Color = when (category) {
    RecipeCategory.BREAKFAST -> ChipBreakFast
    RecipeCategory.LUNCH -> ChipLunch
    RecipeCategory.DINNER -> ChipDinner
    RecipeCategory.COOLERS -> ChipCoolers
    RecipeCategory.DESSERTS -> ChipDesserts
    RecipeCategory.VEGETARIAN -> ChipVeg
    RecipeCategory.NON_VEGETARIAN -> ChipNonVeg
    RecipeCategory.SNACKS -> ChipSnacks
    RecipeCategory.ALL -> BrandOrange
}
package com.sankhlatraders.recipeai.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sankhlatraders.recipeai.data.model.Ingredient
import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.data.model.RecipeStep
import com.sankhlatraders.recipeai.ui.components.getCategoryColor
import com.sankhlatraders.recipeai.ui.theme.BrandGold
import com.sankhlatraders.recipeai.ui.theme.BrandGoldLight
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.DarkBackground
import com.sankhlatraders.recipeai.ui.theme.DarkSurfaceVariant
import com.sankhlatraders.recipeai.ui.theme.Divider
import com.sankhlatraders.recipeai.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(recipe: Recipe, onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()
    val imageHeight = 320.dp
    //val scrollOffset = scrollState.value.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) { // Hero image selection
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(imageHeight)
            ) {
                AsyncImage(
                    model = recipe.thumbnailUrl,
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                //Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    DarkBackground.copy(alpha = 0.4f),
                                    DarkBackground
                                )
                            )
                        )
                )

                //Category badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = getCategoryColor(recipe.category)
                    ) {
                        Text(
                            text = "${recipe.category.emoji} ${recipe.category.displayName}",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            //Content Selection
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                //Title
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                //Cuisine
                Text(
                    text = "globe emoji ${recipe.cuisine} Cuisine",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandOrange
                )
                Spacer(modifier = Modifier.height(12.dp))

                //Stats row
                RecipesStatsRow(recipe = recipe)

                Spacer(modifier = Modifier.height(16.dp))

                //Description
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                //Tags
                if (recipe.tags.isNotEmpty()) {
                    TagsRow(tags = recipe.tags)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Divider
                HorizontalDivider(color = Divider, thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))

                // Ingredients
                SectionHeader(icon = "ingredients emoji", title = "Ingredients")
                Spacer(modifier = Modifier.height(12.dp))
                IngredientsSection(ingredients = recipe.ingredients, serving = recipe.servings)

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Divider, thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))

                // Steps
                SectionHeader(icon = "cook emoji", title = "Cooking Steps")
                Spacer(modifier = Modifier.height(12.dp))
                StepsSection(steps = recipe.steps)

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        //Back button - floated
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(12.dp)
                .align(Alignment.TopStart)
        ) {
            FilledIconButton(
                onClick = onBackClick,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xCC000000)),
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun RecipesStatsRow(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                DarkSurfaceVariant
            )
            .padding(16.dp), horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(
            icon = "timer emoji",
            value = "${recipe.prepTimeMinutes + recipe.cookTimeMinutes}",
            label = "min total"
        )
        VerticalDivider(modifier = Modifier.height(40.dp), color = Divider)
        StatItem(icon = "serving emoji", value = "${recipe.servings}", label = "serving")
        VerticalDivider(modifier = Modifier.height(40.dp), color = Divider)
        StatItem(icon = "fire emoji", value = "${recipe.calories}", label = "kcal")
        VerticalDivider(modifier = Modifier.height(40.dp), color = Divider)
        StatItem(icon = "star emoji", value = "%.1f".format(recipe.rating), label = "rating")
    }
}

@Composable
private fun StatItem(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}

@Composable
private fun SectionHeader(icon: String, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = icon, fontSize = 20.sp)
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TagsRow(tags: List<String>) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = BrandOrange.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, BrandOrange.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "#$tag",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandOrange
                )
            }
        }
    }
}

@Composable
private fun IngredientsSection(ingredients: List<Ingredient>, serving: Int) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "For $serving Serving${if (serving > 1) "s" else ""}",
                style = MaterialTheme.typography.titleMedium,
                color = BrandOrange,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            ingredients.forEachIndexed { index, ingredient ->
                IngredientRow(ingredient = ingredient)
                if (index < ingredients.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Divider)
                }
            }
        }
    }
}

@Composable
private fun IngredientRow(ingredient: Ingredient) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(BrandOrange)
            )
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        Text(
            text = "${ingredient.quantity} ${ingredient.unit}".trim(),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StepsSection(steps: List<RecipeStep>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        steps.forEachIndexed { index, step ->
            StepCard(step = step, isLast = index == steps.size - 1)
        }
    }
}

@Composable
private fun StepCard(step: RecipeStep, isLast: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Step number column with connecting line
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                BrandOrange,
                                BrandGold
                            )
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.stepNumber.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    BrandOrange.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
        //Step content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (!isLast) 0.dp else 0.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                color = DarkSurfaceVariant
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = step.instruction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                    if (step.duration != null || step.tip != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    step.duration?.let { duration ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Timer,
                                contentDescription = null,
                                tint = BrandOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = duration,
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandOrange
                            )
                        }
                    }

                    step.tip?.let { tip ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandGold.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "bulb emoji", fontSize = 13.sp)
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BrandGoldLight
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.sankhlatraders.recipeai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sankhlatraders.recipeai.data.model.RecipeCategory

@Composable
fun CategoryChipRow(
    selectedCategory: RecipeCategory,
    onCategorySelected: (RecipeCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = RecipeCategory.values().toList()

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories.size) { index ->
            val category: RecipeCategory = categories[index]
            CategoryChip(
                category = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: RecipeCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val chipColor = getCategoryColor(category)

    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = "${category.emoji} ${category.displayName}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        shape = RoundedCornerShape(50.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = chipColor,
            selectedLabelColor = Color.White,
            containerColor = chipColor.copy(alpha = 0.12f),
            labelColor = chipColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = chipColor.copy(alpha = 0.3f),
            selectedBorderColor = Color.Transparent
        )
    )
}
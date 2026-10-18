package com.sankhlatraders.recipeai.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: Dp = 8.dp,
    crossAxisSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val mainSpacing = mainAxisSpacing.roundToPx()
        val crossSpacing = crossAxisSpacing.roundToPx()

        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        val rowWidths = mutableListOf<Int>()
        val rowHeights = mutableListOf<Int>()

        var currentRow = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentRowWidth = 0
        var currentRowHeight = 0

        val placeables =
            measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        for (placeable in placeables) {
            if (currentRowWidth + placeable.width
                + (if (currentRow.isEmpty()) 0 else mainSpacing) > constraints.maxWidth && currentRow.isNotEmpty()
            ) {
                rows.add(currentRow)
                rowWidths.add(currentRowWidth)
                rowHeights.add(currentRowHeight)
                currentRow = mutableListOf()
                currentRowWidth = 0
                currentRowHeight = 0
            }
            currentRow.add(placeable)
            currentRowWidth += placeable.width + (if (currentRow.size == 1) 0 else mainSpacing)
            if (placeable.height > currentRowHeight) currentRowHeight = placeable.height
        }

        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
            rowWidths.add(currentRowWidth)
            rowHeights.add(currentRowHeight)
        }

        val totalHeight = rowHeights.sum() + (rows.size - 1).coerceAtLeast(0) * crossSpacing

        layout(width = constraints.maxWidth, height = totalHeight) {
            var yPos = 0
            rows.forEachIndexed { rowIndex, row ->
                var xPos = 0
                row.forEachIndexed { itemIndex, placeable ->
                    placeable.placeRelative(xPos, yPos)
                    xPos += placeable.width + mainSpacing
                }
                yPos += rowHeights[rowIndex] + crossSpacing
            }
        }
    }
}
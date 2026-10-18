package com.sankhlatraders.recipeai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.DarkSurfaceVariant
import com.sankhlatraders.recipeai.ui.theme.TextHint

@Composable
fun RecipeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search recipes, cuisines...",
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = TextHint
            )
        },
        leadingIcon = {
            Icon(
                Icons.Rounded.Search,
                contentDescription = "Search",
                tint = BrandOrange
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = {
                    onQueryChange("")
                    focusManager.clearFocus()
                }) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Clear",
                        tint = TextHint
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandOrange,
            unfocusedBorderColor = Color(0xFF333333),
            focusedContainerColor = DarkSurfaceVariant,
            unfocusedContainerColor = DarkSurfaceVariant,
            cursorColor = BrandOrange,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
    )
}
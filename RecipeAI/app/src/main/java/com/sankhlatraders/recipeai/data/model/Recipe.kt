package com.sankhlatraders.recipeai.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val category: RecipeCategory,
    val cuisine: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: Difficulty,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val ingredients: List<Ingredient>,
    val steps: List<RecipeStep>,
    val calories: Int,
    val rating: Float,
    val tags: List<String> = emptyList()
)

@Serializable
data class Ingredient(val name: String, val quantity: String, val unit: String)

@Serializable
data class RecipeStep(
    val stepNumber: Int, val instruction: String, val duration: String? = null,
    val tip: String? = null
)

@Serializable
enum class RecipeCategory(val displayName: String, val emoji: String) {
    BREAKFAST("Breakfast", ""),
    LUNCH("Lunch", ""),
    DINNER("Dinner", ""),
    COOLERS("Coolers & Drinks", ""),
    SNACKS("Snacks", ""),
    DESSERTS("Desserts", ""),
    VEGETARIAN("Vegetarian", ""),
    NON_VEGETARIAN("Non-Vegetarian", ""),
    ALL("All Recipes", "")
}

@Serializable
enum class Difficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

data class AiRecipeSuggestion(
    val recipes: List<Recipe>,
    val locationContext: String,
    val suggestedCategories: List<String>
)

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val cityName: String = "",
    val countryName: String = ""
)

package com.sankhlatraders.recipeai.data.repository

import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.data.model.RecipeCategory
import com.sankhlatraders.recipeai.data.model.SampleRecipes
import com.sankhlatraders.recipeai.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(private val geminiService: GeminiService) {
    private val _allRecipes = MutableStateFlow(SampleRecipes.recipes)

    fun getAllRecipes(): Flow<List<Recipe>> = _allRecipes.asStateFlow()

    fun getRecipesByCategory(category: RecipeCategory): Flow<List<Recipe>> =
        _allRecipes.map { recipes ->
            if (category == RecipeCategory.ALL) recipes
            else recipes.filter { it.category == category }
        }

    fun getRecipeById(id: String): Recipe? = _allRecipes.value.find { it.id == id }

    fun searchRecipes(query: String): Flow<List<Recipe>> = _allRecipes.map { recipes ->
        if (query.isBlank()) {
            recipes
        } else recipes.filter { recipe ->
            recipe.title.contains(query, ignoreCase = true) ||
                    recipe.description.contains(query, ignoreCase = true) ||
                    recipe.cuisine.contains(query, ignoreCase = true) ||
                    recipe.tags.any { it.contains(query, ignoreCase = true) } ||
                    recipe.category.displayName.contains(query, ignoreCase = true)
        }
    }

    suspend fun getAISuggestedRecipes(
        location: String,
        searchQuery: String = ""
    ): Result<List<Recipe>> {
        return geminiService.suggestRecipes(
            location = location,
            searchQuery = searchQuery
        )
    }
}
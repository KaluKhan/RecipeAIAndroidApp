package com.sankhlatraders.recipeai.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.data.model.RecipeCategory
import com.sankhlatraders.recipeai.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecipeRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _selectedCategory = MutableStateFlow(RecipeCategory.ALL)
    val selectedCategory: StateFlow<RecipeCategory> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val filteredRecipes: StateFlow<List<Recipe>> = combine(
        repository.getAllRecipes(), _searchQuery, _selectedCategory
    ) { recipes, query, category ->
        val categoryFiltered = if (category == RecipeCategory.ALL) recipes
        else recipes.filter { it.category == category }

        if (query.isBlank()) categoryFiltered
        else categoryFiltered.filter { recipe ->
            recipe.title.contains(query, ignoreCase = true) ||
                    recipe.description.contains(query, ignoreCase = true) ||
                    recipe.cuisine.contains(query, ignoreCase = true) ||
                    recipe.tags.any { it.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        SampleRecipes.recipes)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: RecipeCategory) {
        _selectedCategory.value = category
    }

    fun getRecipeById(id: String): Recipe? = repository.getRecipeById(id)
}

private val SampleRecipes = com.sankhlatraders.recipeai.data.model.SampleRecipes
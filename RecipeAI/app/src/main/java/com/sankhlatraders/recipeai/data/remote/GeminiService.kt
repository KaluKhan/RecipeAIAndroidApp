package com.sankhlatraders.recipeai.data.remote

import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.sankhlatraders.recipeai.data.model.Difficulty
import com.sankhlatraders.recipeai.data.model.Ingredient
import com.sankhlatraders.recipeai.data.model.Recipe
import com.sankhlatraders.recipeai.data.model.RecipeCategory
import com.sankhlatraders.recipeai.data.model.RecipeStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.8f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 4096
        }
    )

    /**
     * Generates recipes suggestions using Gemini AI based on location and/or a search query.
     */
    suspend fun suggestRecipes(
        location: String,
        searchQuery: String = "",
        existingRecipeIds: List<String> = emptyList()
    ): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            val prompt = buildPrompt(location, searchQuery)
            val response = model.generateContent(prompt)
            val text = response.text
                ?: return@withContext Result.failure(Exception("Empty response from Gemini AI"))
            val recipes = parseGeminiResponse(text)
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildPrompt(location: String, searchQuery: String): String {
        val locationContext = if (location.isNotBlank()) {
            "The user is location in or interested in cuisine from: $location"
        } else {
            "Suggest globally popular recipes"
        }

        val searchContext = if (searchQuery.isNotBlank()) {
            "The user is specifically searching for: $searchQuery"
        } else {
            ""
        }

        return """
            You are world-class culinary expert and AI chef assistant. $locationContext. $searchContext
            Generate exact 6 unique and authentic recipe suggestions. For each recipe, provide the 
            details in this EXACT JSON array format (no markdown, no extra text, just valid JSON):
            
            [
             {
              "id": "ai_1",
              "title": "Recipe Name",
              "description": "A mouthwatering 2-sentence description of the dish.",
              "thumbnailUrl": "https://images.unsplash.com/photo-VALID_PHOTO_ID?w=600",
              "category":"BREAKFAST",
              "cuisine":"Cuisine Type",
              "prepTimeMinutes":15,
              "cookTimeMinutes":30,
              "servings":4,
              "difficulty":"EASY",
              "isVegetarian": true,
              "isVegan":false,
              "calories":350,
              "rating":"4.5",
              "tags":"["tag1", "tag2"]",
              "ingredients":"[
                {"name":"Ingredient", "quantity":"2", "unit":"cups"}
              ]",
              "steps":[
              {"stepNumber":1, "instruction":"Step instruction here.", "duration":"5 min", "tip":"Optional helpful tip."}
              ]
             }
            ]
            
            Rules:
            - category must be one of: BREAKFAST, LUNCH DINNER, COOLERS, SNACKS, DESSERTS, VEGETARIAN, NON-VEGETARIAN
            - difficulty must be one of: EASY, MEDIUM, HARD
            - Use REAL Unsplash photo URLs relevant to food (use photo IDs from unsplash)
            - Include 4-8 ingredients per recipe
            - Include 3-7 detailed steps per recipe
            - Make recipes authentic to the location/region if specified
            - Return ONLY the JSON array, no other text
        """.trimIndent()
    }

    private fun parseGeminiResponse(text: String): List<Recipe> {
        return try {
            // Clean the text - extract JSON array
            val jsonText = extractJsonArray(text)
            val gson = com.google.gson.GsonBuilder()
                .setLenient()
                .create()
            val type = object : com.google.gson.reflect.TypeToken<List<GeminiRecipeDto>>() {}.type
            val dtos: List<GeminiRecipeDto> = gson.fromJson(jsonText, type)
            dtos.mapIndexed { index, dto -> dto.toRecipe(index) }
        } catch (e: Exception) {
            //Fallback: return empty list - UI will show error state
            emptyList()
        }
    }

    private fun extractJsonArray(text: String): String {
        val start = text.indexOf('[')
        val end = text.lastIndexOf(']')
        return if (start != -1 && end != -1 && end > start) {
            text.substring(start, end + 1)
        } else {
            text.trim()
        }
    }
}

/**
 * DTO that matches the JSON shape returned by Gemini.
 */
data class GeminiRecipeDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val category: String = "DINNER",
    val cuisine: String = "",
    val prepTimeMinutes: Int = 10,
    val cookTimeMinutes: Int = 20,
    val servings: Int = 2,
    val difficulty: String = "EASY",
    val isVegetarian: Boolean = false,
    val isVegan: Boolean = false,
    val calories: Int = 300,
    val rating: Float = 4.0f,
    val tags: List<String> = emptyList(),
    val ingredients: List<GeminiIngredientDto> = emptyList(),
    val steps: List<GeminiStepDto> = emptyList()
) {
    fun toRecipe(fallbackIndex: Int): Recipe {
        val recipeId = if (id.isNotBlank()) id else "ai_${fallbackIndex + 1}"
        return Recipe(
            id = recipeId,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl.ifBlank { "http://images.unsplash.com/photo-1504674900247-0877df9cc836?w=600" },
            category = try {
                RecipeCategory.valueOf(category.uppercase())
            } catch (e: Exception) {
                RecipeCategory.DINNER
            },
            cuisine = cuisine,
            prepTimeMinutes = prepTimeMinutes,
            cookTimeMinutes = cookTimeMinutes,
            servings = servings,
            difficulty = try {
                Difficulty.valueOf(difficulty.uppercase())
            } catch (e: Exception) {
                Difficulty.EASY
            },
            isVegetarian = isVegetarian,
            isVegan = isVegan,
            ingredients = ingredients.map { Ingredient(it.name, it.quantity, it.unit) },
            steps = steps.mapIndexed { i, s ->
                RecipeStep(
                    stepNumber = if (s.stepNumber > 0) s.stepNumber else i + 1,
                    instruction = s.instruction,
                    duration = s.duration,
                    tip = s.tip
                )
            },
            calories = calories,
            rating = rating,
            tags = tags
        )
    }
}

data class GeminiIngredientDto(
    val name: String = "",
    val quantity: String = "",
    val unit: String = ""
)

data class GeminiStepDto(
    val stepNumber: Int = 0,
    val instruction: String = "",
    val duration: String? = null,
    val tip: String? = null
)
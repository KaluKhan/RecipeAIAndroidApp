package com.sankhlatraders.recipeai.data.model

object SampleRecipes {
    val recipes: List<Recipe> = listOf(
        Recipe(
            id = "1",
            title = "Classic Avocado Toast",
            description = "Creamy avocado spread on perfectly toasted sourdough with poached eggs and chilli flacks.",
            thumbnailUrl = "http://images.unsplash.com/photo-1525351484163-7529414344d8?w=600",
            category = RecipeCategory.BREAKFAST,
            cuisine = "American",
            prepTimeMinutes = 5,
            cookTimeMinutes = 10,
            servings = 2,
            difficulty = Difficulty.EASY,
            isVegetarian = true,
            isVegan = false,
            ingredients = listOf(
                Ingredient("Sourdough bread", "2", "slices"),
                Ingredient("Ripe avocado", "1", "whole"),
                Ingredient("Eggs", "2", "whole"),
                Ingredient("Lemon juice", "1", "tsp"),
                Ingredient("Red chilli flakes", "1/2", "tsp"),
                Ingredient("Salt & pepper", "to test", "slices")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    "Toast the sourdough slices until golden and crisp.",
                    "3 min"
                ),
                RecipeStep(
                    stepNumber = 2,
                    "Mash the avocado with lemon juice, salt and pepper.",
                    null,
                    tip = "Use a fork for a chunky texture."
                ),
                RecipeStep(
                    stepNumber = 3,
                    "Poach eggs in simmering water with a dash of vinegar for 3 minutes.",
                    "3 min"
                ),
                RecipeStep(
                    stepNumber = 4,
                    "Spread avocado on toast, place poached egg on top.",
                    null
                ),
                RecipeStep(
                    stepNumber = 5,
                    "Sprinkle chilli flakes and serve immediately",
                    null
                )
            ),
            calories = 320,
            rating = 4.7f,
            tags = listOf("healthy", "quick", "brunch")
        ),
        Recipe(
            id = "2",
            title = "Classic Avocado Toast",
            description = "Creamy avocado spread on perfectly toasted sourdough with poached eggs and chilli flacks.",
            thumbnailUrl = "http://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=600",
            category = RecipeCategory.DINNER,
            cuisine = "Indian",
            prepTimeMinutes = 20,
            cookTimeMinutes = 40,
            servings = 4,
            difficulty = Difficulty.MEDIUM,
            isVegetarian = false,
            isVegan = false,
            ingredients = listOf(
                Ingredient("Chicken breast", "600", "g"),
                Ingredient("Tomatoes", "400", "g"),
                Ingredient("Heavy cream", "150", "ml"),
                Ingredient("Butter", "3", "tbsp"),
                Ingredient("Garam masala", "2", "tsp"),
                Ingredient("Ginger-garlic paste", "2", "tbsp")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    "Toast the sourdough slices until golden and crisp.",
                    "3 min"
                ),
                RecipeStep(
                    stepNumber = 1,
                    "Toast the sourdough slices until golden and crisp.",
                    "3 min"
                ),
                RecipeStep(
                    stepNumber = 1,
                    "Toast the sourdough slices until golden and crisp.",
                    "3 min"
                ),
                RecipeStep(
                    stepNumber = 1,
                    "Toast the sourdough slices until golden and crisp.",
                    "3 min"
                ),
                RecipeStep(
                    stepNumber = 1,
                    "Toast the sourdough slices until golden and crisp.",
                    "3 min"
                )
            ),
            calories = 320,
            rating = 4.7f,
            tags = listOf("healthy", "quick", "brunch")
        )
    )
}
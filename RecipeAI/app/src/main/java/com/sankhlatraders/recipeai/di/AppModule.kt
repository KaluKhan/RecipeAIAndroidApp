package com.sankhlatraders.recipeai.di

import com.sankhlatraders.recipeai.data.remote.GeminiService
import com.sankhlatraders.recipeai.data.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGeminiService(): GeminiService = GeminiService()

    @Provides
    @Singleton
    fun provideRecipeRepository(geminiService: GeminiService): RecipeRepository =
        RecipeRepository(geminiService)
}
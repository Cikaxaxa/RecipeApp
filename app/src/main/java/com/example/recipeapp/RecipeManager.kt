package com.example.recipeapp

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import java.util.UUID

object RecipeManager {
    private const val RECIPE_KEY_PREFIX = "recipe_"

    fun saveRecipe(recipe: Recipe, context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val jsonRecipe = Gson().toJson(recipe)
        val key = "$RECIPE_KEY_PREFIX${UUID.randomUUID()}"
        editor.putString(key, jsonRecipe)
        editor.apply()
    }

    fun getAllRecipes(context: Context): List<Recipe> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val recipes = mutableListOf<Recipe>()
        val keys = sharedPreferences.all.keys.filter { it.startsWith(RECIPE_KEY_PREFIX) }

        for (key in keys) {
            val jsonRecipe = sharedPreferences.getString(key, null)
            val recipe = Gson().fromJson(jsonRecipe, Recipe::class.java)
            recipe?.let { recipes.add(it) }
        }

        return recipes
    }
}
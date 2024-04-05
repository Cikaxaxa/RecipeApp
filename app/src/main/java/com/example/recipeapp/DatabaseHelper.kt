package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_ID
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_IMAGE
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_INGREDIENTS
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_NAME
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_STEP
import com.example.recipeapp.RecipeContract.TABLE_RECIPE

object RecipeContract {
    const val TABLE_RECIPE = "recipe"
    const val COLUMN_RECIPE_ID = "recipe_id"
    const val COLUMN_RECIPE_NAME = "recipe_name"
    const val COLUMN_RECIPE_INGREDIENTS = "recipe_ingredients"
    const val COLUMN_RECIPE_STEP = "recipe_step"
    const val COLUMN_RECIPE_IMAGE = "recipe_image"
}




class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "recipe_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USER = "user"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"


    }


    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableQuery = ("CREATE TABLE $TABLE_USER ("
                + "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_USERNAME TEXT,"
                + "$COLUMN_PASSWORD TEXT)")

        val createRecipeTableQuery = ("CREATE TABLE $TABLE_RECIPE ("
                + "$COLUMN_RECIPE_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_RECIPE_NAME TEXT,"
                + "$COLUMN_RECIPE_INGREDIENTS TEXT,"
                + "$COLUMN_RECIPE_STEP TEXT,"
                + "$COLUMN_RECIPE_IMAGE TEXT)")

        db.execSQL(createUserTableQuery)
        db.execSQL(createRecipeTableQuery)


        addSampleRecipe(db, "Pasta", "Pasta, Tomato Sauce, Cheese", "1. Boil the pasta. 2. Add sauce. 3. Add cheese.","android.resource://com.example.recipeapp/2131230957")
        addSampleRecipe(db, "Pizza", "Dough, Tomato Sauce, Cheese, Toppings", "1. Roll out the dough. 2. Add sauce and toppings. 3. Bake.","android.resource://com.example.recipeapp/2131230957")
        addSampleRecipe(db, "Salad", "Lettuce, Tomatoes, Cucumbers, Dressing", "1. Chop vegetables. 2. Toss with dressing.","android.resource://com.example.recipeapp/2131230957")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPE")
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_PASSWORD, password)
        return db.insert(TABLE_USER, null, contentValues)
    }

    fun insertRecipe(name: String, ingredients: String, step: String, image: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_RECIPE_NAME, name)
        contentValues.put(COLUMN_RECIPE_INGREDIENTS, ingredients)
        contentValues.put(COLUMN_RECIPE_STEP, step)
        contentValues.put(COLUMN_RECIPE_IMAGE, image)
        return db.insert(TABLE_RECIPE, null, contentValues)
    }



    @SuppressLint("Range")
    fun getAllRecipeNames(): List<String> {
        val recipeNames = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_RECIPE_NAME FROM $TABLE_RECIPE"
        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPE_NAME))
                recipeNames.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return recipeNames
    }

    @SuppressLint("Range")
    fun getRecipeByName(name: String): Recipe {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_RECIPE WHERE $COLUMN_RECIPE_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(name))
        var recipe = Recipe("", "", "", "")
        cursor.use {
            if (it.moveToFirst()) {
                val recipeName = it.getString(it.getColumnIndex(COLUMN_RECIPE_NAME))
                val ingredients = it.getString(it.getColumnIndex(COLUMN_RECIPE_INGREDIENTS))
                val steps = it.getString(it.getColumnIndex(COLUMN_RECIPE_STEP))
                val imageUri = it.getString(it.getColumnIndex(COLUMN_RECIPE_IMAGE))
                recipe = Recipe(recipeName ?: "", ingredients ?: "", steps ?: "", imageUri ?: "")
            }
        }
        db.close()
        return recipe
    }

    fun updateRecipe(name: String, newIngredients: String, newSteps: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_RECIPE_INGREDIENTS, newIngredients)
        contentValues.put(COLUMN_RECIPE_STEP, newSteps)
        val updatedRows = db.update(TABLE_RECIPE, contentValues, "$COLUMN_RECIPE_NAME = ?", arrayOf(name))
        db.close()
        return updatedRows
    }

    fun deleteRecipe(name: String): Int {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_RECIPE, "$COLUMN_RECIPE_NAME = ?", arrayOf(name))
        db.close()
        return deletedRows
    }

    fun addSampleRecipe(db: SQLiteDatabase, name: String, ingredients: String, steps: String, imageUri : String) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_RECIPE_NAME, name)
        contentValues.put(COLUMN_RECIPE_INGREDIENTS, ingredients)
        contentValues.put(COLUMN_RECIPE_STEP, steps)
        contentValues.put(COLUMN_RECIPE_IMAGE, imageUri)


        // Insert or replace the row if COL_NAME already exists
        db.insertWithOnConflict(TABLE_RECIPE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)

        // Delete other rows with the same COL_NAME
        db.delete(TABLE_RECIPE, "$COLUMN_RECIPE_NAME <> ? AND $COLUMN_RECIPE_NAME = ?", arrayOf(name, name))
    }

    fun addUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_PASSWORD, password)
        val result = db.insert(TABLE_USER, null, contentValues)
        db.close()
        return result
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val count = cursor.count
        cursor.close()
        return count > 0
    }


}
package com.example.recipeapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_ID
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_IMAGE
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_INGREDIENTS
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_NAME
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_STEP
import com.example.recipeapp.RecipeContract.COLUMN_RECIPE_TYPE
import com.example.recipeapp.RecipeContract.TABLE_RECIPE
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object RecipeContract {
    const val TABLE_RECIPE = "recipe"
    const val COLUMN_RECIPE_ID = "recipe_id"
    const val COLUMN_RECIPE_TYPE = "recipe_type"
    const val COLUMN_RECIPE_NAME = "recipe_name"
    const val COLUMN_RECIPE_INGREDIENTS = "recipe_ingredients"
    const val COLUMN_RECIPE_STEP = "recipe_step"
    const val COLUMN_RECIPE_IMAGE = "recipe_image"
}








class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val resources: Resources? = null

    private val mContext: Context = context

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
                + "$COLUMN_RECIPE_TYPE TEXT,"
                + "$COLUMN_RECIPE_INGREDIENTS TEXT,"
                + "$COLUMN_RECIPE_STEP TEXT,"
                + "$COLUMN_RECIPE_IMAGE BYTE)")

        db.execSQL(createUserTableQuery)
        db.execSQL(createRecipeTableQuery)


        val imageCompressor = ImageCompressor2()
        val quality = 50
        val pasta = R.drawable.pasta
        val pasta2 = imageCompressor.compressImageFromDrawable(mContext, pasta, quality)
        val pasta3 = R.drawable.pasta2
        val pasta4 = imageCompressor.compressImageFromDrawable(mContext, pasta3, quality)
        val burger = R.drawable.burger
        val burger2 = imageCompressor.compressImageFromDrawable(mContext, burger, quality)
        val piz = R.drawable.piz
        val piz2 = imageCompressor.compressImageFromDrawable(mContext, piz, quality)
        val salad = R.drawable.salad
        val salad2 = imageCompressor.compressImageFromDrawable(mContext, salad, quality)







        addSampleRecipe(db, "Pasta Cheese","Main-course", "Pasta, Tomato Sauce, Cheese", "1. Boil the pasta. 2. Add sauce. 3. Add cheese.",pasta2)
        addSampleRecipe(db, "Creamy Garlic Pastaa","Main-course", "Pasta, Tomato Sauce, Cheese", "1. Add garlic and stir until fragrant, 1 to 2 minutes. 2. Add spaghetti and cook, stirring occasionally, until tender yet firm to the bite, about 12 minutes. 3. Add Parmesan cheese, cream, and parsley and mix until thoroughly combined.",pasta4)
        addSampleRecipe(db, "Pizza","Dinner", "Dough, Tomato Sauce, Cheese, Toppings", "1. Roll out the dough. 2. Add sauce and toppings. 3. Bake.",piz2)
        addSampleRecipe(db, "Green Salad","Salad", "Lettuce, Tomatoes, Cucumbers, Dressing", "1. Chop vegetables. 2. Toss with dressing.",salad2)
        addSampleRecipe(db, "Beef Burger","Lunch", "Bun, Lettuce, Tomatoes, Cheese, Ground Beef, Onion.", "1. Chop vegetables. 2. Toss with dressing. 3. Preheat an outdoor grill for high heat and lightly oil grate. 4. Add ground beef and bread crumbs and mix with your hands or a fork until well blended. 5. Place patties on the preheated grill.",burger2)
    }

    class ImageCompressor2 {
        fun compressImageFromDrawable(context: Context, drawableId: Int, quality: Int): ByteArray {
            // Load the drawable resource into a Bitmap
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)

            // Create a ByteArrayOutputStream to hold the compressed image data
            val outputStream = ByteArrayOutputStream()

            // Compress the Bitmap to the output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            // Convert the compressed image data to a byte array
            return outputStream.toByteArray()
        }
    }


    class ImageCompressor {
        fun compressImageFromImageView(imageView: ImageView, quality: Int): ByteArray {
            // Get the Bitmap from the ImageView
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap

            // Create a ByteArrayOutputStream to hold the compressed image data
            val outputStream = ByteArrayOutputStream()

            // Compress the Bitmap to the output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            // Convert the compressed image data to a byte array
            return outputStream.toByteArray()
        }
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

    fun insertRecipe(name: String,type: String, ingredients: String, step: String, image: ByteArray): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_RECIPE_NAME, name)
        contentValues.put(COLUMN_RECIPE_TYPE, type)
        contentValues.put(COLUMN_RECIPE_INGREDIENTS, ingredients)
        contentValues.put(COLUMN_RECIPE_STEP, step)
        contentValues.put(COLUMN_RECIPE_IMAGE, image)
        return db.insert(TABLE_RECIPE, null, contentValues)
    }

    @SuppressLint("Range")
    fun getRecipeTypes(): List<String> {
        val recipeTypes = mutableListOf<String>()
        val query = "SELECT DISTINCT $COLUMN_RECIPE_TYPE FROM $TABLE_RECIPE"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val recipeType = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPE_TYPE))
                recipeTypes.add(recipeType)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return recipeTypes
    }



    @SuppressLint("Range")
    fun getAllRecipeType(): List<String> {
        val recipeType = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_RECIPE_TYPE FROM $TABLE_RECIPE"
        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val type = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPE_TYPE))
                recipeType.add(type)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return recipeType
    }

    @SuppressLint("Range")
    fun getRecipeNames(recipeType: String): List<String> {
        val recipeNames = mutableListOf<String>()
        val query = "SELECT $COLUMN_RECIPE_NAME FROM $TABLE_RECIPE WHERE $COLUMN_RECIPE_TYPE = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(recipeType))
        if (cursor.moveToFirst()) {
            do {
                val recipeName = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPE_NAME))
                recipeNames.add(recipeName)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return recipeNames
    }

    @SuppressLint("Range")
    fun getRecipeByName(type: String): Recipe {
        val db = this.readableDatabase
        var blobData: ByteArray? = null
        val query = "SELECT * FROM $TABLE_RECIPE WHERE $COLUMN_RECIPE_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(type))
        var recipetype = Recipe("","", "", "", ByteArray(0))
        cursor.use {
            if (it.moveToFirst()) {
                val recipeType = it.getString(it.getColumnIndex(COLUMN_RECIPE_TYPE))
                val recipeName = it.getString(it.getColumnIndex(COLUMN_RECIPE_NAME))
                val ingredients = it.getString(it.getColumnIndex(COLUMN_RECIPE_INGREDIENTS))
                val steps = it.getString(it.getColumnIndex(COLUMN_RECIPE_STEP))
                val imageUri = it.getBlob(it.getColumnIndex(COLUMN_RECIPE_IMAGE))
                blobData = readBlobData(imageUri)
                recipetype = Recipe(recipeType ?: "",recipeName ?: "", ingredients ?: "", steps ?: "",
                    (blobData ?: ByteArray(0)) as ByteArray
                )
            }
        }
        db.close()
        return recipetype
    }
    fun readBlobData(blob: ByteArray): ByteArray {
        val bufferSize = 1024
        val outputStream = ByteArrayOutputStream()
        val inputStream = ByteArrayInputStream(blob)

        val buffer = ByteArray(bufferSize)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        inputStream.close()
        return outputStream.toByteArray()
    }

    @SuppressLint("Range")
    fun getRecipeByType(type: String): Recipe {
        val db = this.readableDatabase
        var blobData: ByteArray? = null
        val query = "SELECT * FROM $TABLE_RECIPE WHERE $COLUMN_RECIPE_TYPE = ?"
        val cursor = db.rawQuery(query, arrayOf(type))
        var recipe = Recipe("","", "", "", ByteArray(0))
        cursor.use {
            if (it.moveToFirst()) {
                val recipeType = it.getString(it.getColumnIndex(COLUMN_RECIPE_TYPE))
                val recipeName = it.getString(it.getColumnIndex(COLUMN_RECIPE_NAME))
                val ingredients = it.getString(it.getColumnIndex(COLUMN_RECIPE_INGREDIENTS))
                val steps = it.getString(it.getColumnIndex(COLUMN_RECIPE_STEP))
                val imageUri = it.getBlob(it.getColumnIndex(COLUMN_RECIPE_IMAGE))
                blobData = readBlobData(imageUri)
                recipe = Recipe(recipeType ?: "",recipeName ?: "", ingredients ?: "", steps ?: "",
                    (blobData ?: ByteArray(0)) as ByteArray
                )
            }
        }
        db.close()
        return recipe
    }

    fun updateRecipe(oldName: String, newName: String, newIngredients: String, newSteps: String, newImage: ByteArray): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_RECIPE_NAME, newName)
        contentValues.put(COLUMN_RECIPE_INGREDIENTS, newIngredients)
        contentValues.put(COLUMN_RECIPE_STEP, newSteps)
        contentValues.put(COLUMN_RECIPE_IMAGE, newImage)
        val whereClause = "$COLUMN_RECIPE_NAME = ?"
        val whereArgs = arrayOf(oldName)
        val updatedRows = db.update(TABLE_RECIPE, contentValues, whereClause, whereArgs)
        db.close()
        return updatedRows
    }

    fun deleteRecipe(name: String): Int {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_RECIPE, "$COLUMN_RECIPE_NAME = ?", arrayOf(name))
        db.close()
        return deletedRows
    }

    fun addSampleRecipe(db: SQLiteDatabase, name: String,type: String, ingredients: String, steps: String, imageUri : ByteArray?) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_RECIPE_NAME, name)
        contentValues.put(COLUMN_RECIPE_TYPE, type)
        contentValues.put(COLUMN_RECIPE_INGREDIENTS, ingredients)
        contentValues.put(COLUMN_RECIPE_STEP, steps)
        contentValues.put(COLUMN_RECIPE_IMAGE, imageUri)

        db.insertWithOnConflict(TABLE_RECIPE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)


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

class YourApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Create an instance of your DatabaseHelper class to trigger database creation
        val dbHelper = DatabaseHelper(this)
        // This will call the onCreate() method of your DatabaseHelper class
        val db = dbHelper.writableDatabase
    }
}
package com.example.recipeapp
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso


private lateinit var dbHelper: DatabaseHelper
@SuppressLint("StaticFieldLeak")
private lateinit var spinnerRecipes: Spinner
private lateinit var imageViewRecipe: ImageView
private lateinit var editTextName: TextView
private lateinit var editTextIngredients: TextView
private lateinit var editTextDetails: TextView
private lateinit var buttonHome: Button
private lateinit var selectedImageUri: Uri


class ListRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_recipe)

        dbHelper = DatabaseHelper(this)
        spinnerRecipes = findViewById(R.id.recipeSpinner)
        imageViewRecipe = findViewById(R.id.imageViewRecipe)
        editTextIngredients = findViewById(R.id.editTextIngredients)
        editTextDetails = findViewById(R.id.editTextDetails)
        buttonHome = findViewById(R.id.buttonHome)

        buttonHome.setOnClickListener {


            val intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        loadRecipeNames()
        spinnerRecipes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val recipeName = spinnerRecipes.selectedItem.toString()
                displayRecipeDetails(recipeName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadRecipeNames() {
        val recipeNames = dbHelper.getAllRecipeNames()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, recipeNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecipes.adapter = adapter
    }

    private fun displayRecipeDetails(recipeName: String) {
        val dbHelper = DatabaseHelper(this)
        val recipe = dbHelper.getRecipeByName(recipeName )
        editTextIngredients.text = recipe.ingredients
        editTextDetails.text = recipe.steps

        if (recipe.imageUri != null) {
            val imageUri = Uri.parse(recipe.imageUri)
            imageViewRecipe.setImageURI(imageUri)
        } else {
            // Set default drawable image if imageUri is null
            imageViewRecipe.setImageResource(R.drawable.baseline_image_24)
        }
    }

}
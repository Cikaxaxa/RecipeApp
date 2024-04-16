package com.example.recipeapp
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import android.Manifest


private lateinit var dbHelper: DatabaseHelper
@SuppressLint("StaticFieldLeak")
private lateinit var spinnerRecipes: Spinner
lateinit var recipeType: Spinner
private lateinit var imageViewRecipe: ImageView
private lateinit var editTextName: TextView
private lateinit var editTextIngredients: TextView
private lateinit var editTextDetails: TextView
private lateinit var buttonHome: Button
private lateinit var selectedImageUri: Uri
private val REQUEST_EXTERNAL_STORAGE_PERMISSION = 1001


class ListRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_recipe)

        dbHelper = DatabaseHelper(this)
        recipeType = findViewById(R.id.recipeType)
        spinnerRecipes = findViewById(R.id.recipeSpinner)
        imageViewRecipe = findViewById(R.id.imageViewRecipe)
        editTextIngredients = findViewById(R.id.editTextIngredients)
        editTextDetails = findViewById(R.id.editTextDetails)
        buttonHome = findViewById(R.id.buttonHome)



        buttonHome.setOnClickListener {


            val intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        val recipeTypes = dbHelper.getRecipeTypes()
        val recipeTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, recipeTypes)
        recipeType.adapter = recipeTypeAdapter

        recipeType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRecipeType = parent?.getItemAtPosition(position).toString()

                // Populate recipe names Spinner based on selected recipe type
                val recipeNames = dbHelper.getRecipeNames(selectedRecipeType)
                val recipeNameAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, recipeNames)
                spinnerRecipes.adapter = recipeNameAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }

//        loadRecipeType()
//        spinnerRecipes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val recipeType = spinnerRecipes.selectedItem.toString()
//                displayRecipeDetails(recipeType)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }

        spinnerRecipes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get selected recipe name
                val selectedRecipeName = parent?.getItemAtPosition(position).toString()

                // Get recipe detail based on selected recipe name
                val recipeDetail = dbHelper.getRecipeByName(selectedRecipeName)

                editTextIngredients.text = recipeDetail.name
                editTextDetails.text = recipeDetail.steps
//                val stringtobitmap =  recipeDetail.imageUri.toByteArray(Charsets.UTF_8)
                val imageBitmap = BitmapFactory.decodeByteArray(recipeDetail.imageUri, 0, recipeDetail.imageUri.size)
                imageViewRecipe.setImageBitmap(imageBitmap)

//                if (recipeDetail.imageUri != null) {
//                    val imageUri = Uri.parse(recipeDetail.imageUri)
//                    imageViewRecipe.setImageURI(imageUri)
//                } else {
//                    // Set default drawable image if imageUri is null
//                    imageViewRecipe.setImageResource(R.drawable.baseline_image_24)
//                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }
    }





    private fun loadRecipeType() {
        val recipeType = dbHelper.getAllRecipeType()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, recipeType)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecipes.adapter = adapter
    }



    private fun displayRecipeDetails(recipeType: String) {
        val dbHelper = DatabaseHelper(this)
        val recipe = dbHelper.getRecipeByType(recipeType )
        editTextIngredients.text = recipe.name
        editTextDetails.text = recipe.steps

//        if (recipe.imageUri != null) {
//            val imageUri = Uri.parse(recipe.imageUri)
//            imageViewRecipe.setImageURI(imageUri)
//        } else {
//            // Set default drawable image if imageUri is null
//            imageViewRecipe.setImageResource(R.drawable.baseline_image_24)
//        }
    }

}
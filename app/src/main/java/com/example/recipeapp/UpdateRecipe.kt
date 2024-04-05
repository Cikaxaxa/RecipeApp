package com.example.recipeapp
import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso



class UpdateRecipe : AppCompatActivity() {



    private lateinit var dbHelper: DatabaseHelper
    @SuppressLint("StaticFieldLeak")
    private lateinit var spinnerRecipes: Spinner
    private lateinit var imageViewRecipe: ImageView
    private lateinit var editTextName: TextView
    private lateinit var editTextIngredients: TextView
    private lateinit var editTextDetails: TextView
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var selectedImageUri: Uri
    private val PICK_IMAGE_REQUEST = 1


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_recipe)

        dbHelper = DatabaseHelper(this)
        spinnerRecipes = findViewById(R.id.recipeSpinner)
        imageViewRecipe = findViewById(R.id.imageViewRecipe)
        editTextIngredients = findViewById(R.id.editTextIngredients)
        editTextDetails = findViewById(R.id.editTextDetails)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonDelete = findViewById(R.id.buttonDelete)

        loadRecipeNames()
        spinnerRecipes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val recipeName = spinnerRecipes.selectedItem.toString()
                displayRecipeDetails(recipeName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        imageViewRecipe.setOnClickListener {
            selectImage()
        }

        buttonUpdate.setOnClickListener {

            updateRecipe()
        }

        buttonDelete.setOnClickListener {

            deleteRecipe()
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

        val imageUri = Uri.parse(recipe.imageUri)
        imageViewRecipe.setImageURI(imageUri)
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AddRecipe.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddRecipe.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
        }
    }

    private fun updateRecipe() {
        val name = spinnerRecipes.selectedItem.toString()
        val newIngredients = editTextIngredients.text.toString()
        val newSteps = editTextDetails.text.toString()
        val updatedRows = dbHelper.updateRecipe(name, newIngredients, newSteps)
        if (updatedRows > 0) {
            Toast.makeText(this, "Succesfully Update", Toast.LENGTH_LONG).show()
            val intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Fail Update", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteRecipe() {
        val name = spinnerRecipes.selectedItem.toString()
        val deletedRows = dbHelper.deleteRecipe(name)
        if (deletedRows > 0) {
            Toast.makeText(this, "Succesfully Delete", Toast.LENGTH_LONG).show()
            val intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Fail Delete", Toast.LENGTH_LONG).show()
        }
    }

}
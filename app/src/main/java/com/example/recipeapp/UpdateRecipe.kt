package com.example.recipeapp
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import java.io.ByteArrayOutputStream


class UpdateRecipe : AppCompatActivity() {



    private lateinit var dbHelper: DatabaseHelper
    @SuppressLint("StaticFieldLeak")
    private lateinit var spinnerRecipes: Spinner
    private lateinit var spinnerType: Spinner
    private lateinit var imageViewRecipe: ImageView
    private lateinit var editTextName: TextView
    private lateinit var editTextIngredients: EditText
    private lateinit var editTextDetails: EditText
    private lateinit var edittextname: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonHome: Button
    private lateinit var selectedImageUri: Uri
    private val PICK_IMAGE_REQUEST = 1


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_recipe)

        dbHelper = DatabaseHelper(this)
        spinnerRecipes = findViewById(R.id.recipeSpinner)
        spinnerType = findViewById(R.id.recipeType)
        imageViewRecipe = findViewById(R.id.imageViewRecipe)
        editTextIngredients = findViewById(R.id.editTextIngredients)
        editTextDetails = findViewById(R.id.editTextDetails)
        edittextname = findViewById(R.id.edittextname)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonHome = findViewById(R.id.buttonHome)

        val recipeTypes = dbHelper.getRecipeTypes()
        val recipeTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, recipeTypes)
        spinnerType.adapter = recipeTypeAdapter

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRecipeType = parent?.getItemAtPosition(position).toString()

                // Populate recipe names Spinner based on selected recipe type
                val recipeNames = dbHelper.getRecipeNames(selectedRecipeType)
                val recipeNameAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, recipeNames)
                spinnerRecipes.adapter = recipeNameAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinnerRecipes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get selected recipe name
                val selectedRecipeName = parent?.getItemAtPosition(position).toString()

                // Get recipe detail based on selected recipe name
                val recipeDetail = dbHelper.getRecipeByName(selectedRecipeName)
                edittextname.setText(recipeDetail.name)
                editTextIngredients.setText(recipeDetail.ingredients)
                editTextDetails.setText(recipeDetail.steps)

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

            }


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

        buttonHome.setOnClickListener {

            val intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadRecipeNames() {
        val recipeNames = dbHelper.getAllRecipeType()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, recipeNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecipes.adapter = adapter
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
            imageViewRecipe.setImageURI(selectedImageUri)
        }

    }

    private fun updateRecipe() {
        val name = spinnerRecipes.selectedItem.toString()
        val name2 = edittextname.text.toString()
        val newIngredients = editTextIngredients.text.toString()
        val newSteps = editTextDetails.text.toString()

        val imageCompressor = AddRecipe.ImageCompressor()
        val quality = 50


        val compressedImage = imageCompressor.compressImageFromImageView(imageViewRecipe, quality)


        val updatedRows = dbHelper.updateRecipe(name,name2, newIngredients, newSteps,compressedImage)
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
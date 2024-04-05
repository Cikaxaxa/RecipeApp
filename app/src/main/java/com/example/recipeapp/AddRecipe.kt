package com.example.recipeapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

class AddRecipe : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
        const val PICK_IMAGE_REQUEST = 1
    }

    private lateinit var imageViewRecipe: ImageView
    private lateinit var editTextName: EditText
    private lateinit var editTextIngredients: EditText
    private lateinit var editTextIngredients2: TextView
    private lateinit var editTextDetails: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonUpdate: Button
    private lateinit var imageBitmap: Bitmap
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var selectedImageUri: Uri


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        dbHelper = DatabaseHelper(this)
        imageViewRecipe = findViewById(R.id.imageViewRecipe)
        buttonSave = findViewById(R.id.buttonSave)
        editTextName = findViewById(R.id.editTextName)
        editTextIngredients = findViewById(R.id.editTextIngredients)
        editTextDetails = findViewById(R.id.editTextDetails)





        imageViewRecipe.setOnClickListener {
            selectImage()
        }



        buttonSave.setOnClickListener {

            addRecipe()
        }
    }



    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            imageViewRecipe.setImageURI(selectedImageUri)
        }

    }

    private fun addRecipe() {

        val name = editTextName.text.toString()
        val ingredients = editTextIngredients.text.toString()
        val step = editTextDetails.text.toString()
        val imageUri = selectedImageUri.toString()

        val addedrows = dbHelper.insertRecipe(name, ingredients, step, imageUri)

        if (addedrows > 0) {
            Toast.makeText(this, "Succesfully Added", Toast.LENGTH_LONG).show()
            val intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Fail Added", Toast.LENGTH_LONG).show()
        }
    }


}
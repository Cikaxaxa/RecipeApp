package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var buttonview: Button
    private lateinit var buttonUpdate: Button
    private lateinit var buttonAdd: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        buttonUpdate = findViewById(R.id.ButtonUpdate)
        buttonview = findViewById(R.id.buttonView)
        buttonAdd = findViewById(R.id.ButtonAdd)

        buttonUpdate.setOnClickListener {


            val intent= Intent(applicationContext, UpdateRecipe::class.java)
            startActivity(intent)
        }

        buttonview.setOnClickListener {



            val intent= Intent(applicationContext, ListRecipe::class.java)
            startActivity(intent)
        }

        buttonAdd.setOnClickListener {



            val intent= Intent(applicationContext, AddRecipe::class.java)
            startActivity(intent)
        }


    }


}
package com.example.recipeapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SearchRecipe : AppCompatActivity() {

    private lateinit var subPizzaArray: Array<String>
    private lateinit var subarry: Array<String>
    private lateinit var stepBurgerArray: Array<String>
    private lateinit var stepPizzaArray: Array<String>
    private lateinit var subCakeArray: Array<String>
    private lateinit var subBurgerArray: Array<String>
    private lateinit var editTextRecipeType: EditText
    private lateinit var buttonSearch: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewResult2: TextView
    private lateinit var textViewResult3: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        // Load data array from resources
//        subPizzaArray = resources.getStringArray(R.array.subpiza)
//        subarry = resources.getStringArray(R.array.sub_array)
//        subBurgerArray = resources.getStringArray(R.array.subburger)
//        subCakeArray = resources.getStringArray(R.array.subcake)
//        stepPizzaArray = resources.getStringArray(R.array.stepiza)
//        stepBurgerArray = resources.getStringArray(R.array.stepburger)

        // Initialize views
        editTextRecipeType = findViewById(R.id.editTextRecipeType)
        buttonSearch = findViewById(R.id.buttonSearch)
        textViewResult = findViewById(R.id.spinnertyperecipe)
        textViewResult2 = findViewById(R.id.txtingredient2)
        textViewResult3 = findViewById(R.id.textstep2)

        // Search button click listener
        buttonSearch.setOnClickListener {
            filterAndDisplaySubArray()
        }
    }

    private fun filterAndDisplaySubArray() {
        val userInput = editTextRecipeType.text.toString().trim()

        // Determine sub-array based on user input
        val subArray = when (userInput) {
            "Pizza" -> {
                val toppings = mutableListOf<String>()
                toppings.addAll(listOf("Pizza"))
                toppings.addAll(listOf("\nHere is The Ingredients:"))
//                toppings.addAll(resources.getStringArray(R.array.subpiza))
//                toppings.addAll(listOf("\nHow to Prepare:"))
//                toppings.addAll(resources.getStringArray(R.array.stepiza))
                toppings.toTypedArray()
            }
            "Burger" -> {
                val toppings = mutableListOf<String>()
//                toppings.addAll(resources.getStringArray(R.array.subburger))
//                toppings.addAll(resources.getStringArray(R.array.stepburger))
                toppings.toTypedArray()
            }
            else -> emptyArray()
        }

        // Display sub-array data
        if (subArray.isNotEmpty()) {
            textViewResult.text = subArray.joinToString("\n")
        } else {
            textViewResult.text = "No data found for $userInput"
        }
    }
}
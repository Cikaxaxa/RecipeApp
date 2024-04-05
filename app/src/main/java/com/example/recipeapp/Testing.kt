package com.example.recipeapp
import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Testing : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var submitButton: Button
    private lateinit var textView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        editText = findViewById(R.id.editText)
        submitButton = findViewById(R.id.submitButton)
        textView = findViewById(R.id.textView)

        val defaultImageUri = "android.resource://com.example.recipeapp/" + R.drawable.baseline_image_24


        submitButton.setOnClickListener {
            textView.text = defaultImageUri
        }
    }
}
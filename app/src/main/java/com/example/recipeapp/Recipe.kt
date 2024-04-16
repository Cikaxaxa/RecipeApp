package com.example.recipeapp

import java.io.Serializable

data class Recipe(
    var type: String,
    var name: String,
    var ingredients: String,
    var steps: String,
    var imageUri: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (!imageUri.contentEquals(other.imageUri)) return false

        return true
    }

    override fun hashCode(): Int {
        return imageUri.contentHashCode()
    }
}
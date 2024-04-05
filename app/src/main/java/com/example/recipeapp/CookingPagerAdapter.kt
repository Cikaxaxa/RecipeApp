package com.example.recipeapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CookingPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 3 // Assuming there are 3 steps
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Step1()
            1 -> Step2()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
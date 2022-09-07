package com.example.easycode

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupActionBar()
    }

    @SuppressLint("ResourceAsColor")
    private fun setupActionBar() {
        val colorDrawable = ColorDrawable(android.R.color.transparent)
        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable)
            actionBar.title = resources.getString(R.string.home_activity_title)
        }

    }
}
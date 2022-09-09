package com.example.easycode.extensionfunctions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.easycode.R

    const val CUSTOM_TOAST_DEFAULT = 1440

@SuppressLint("ResourceAsColor", "InflateParams")
fun AppCompatActivity.customToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT,
                                  backgroundColor: Int = Color.WHITE,
                                  textColor: Int = Color.BLACK, textSize: Float = 16f,
                                  gravity: Int = CUSTOM_TOAST_DEFAULT,
                                  xOffset: Int = 0, yOffset: Int = 0)
{
    val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val myView = inflater.inflate(R.layout.custom_toast_layout, null)

    val toast = Toast(applicationContext)

    toast.view = myView
    toast.duration = duration

    val background = myView.findViewById<CardView>(R.id.background)
    val text = myView.findViewById<TextView>(R.id.text)

    text.text = message
    text.textSize = textSize
    text.setTextColor(textColor)

    background.setCardBackgroundColor(backgroundColor)

    if (gravity != CUSTOM_TOAST_DEFAULT)
        toast.setGravity(gravity, xOffset, yOffset)

    toast.show()
}


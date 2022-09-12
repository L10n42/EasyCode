package com.example.easycode.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.easycode.tools.Generator
import com.example.easycode.R
import com.example.easycode.databinding.ActivityGeneratorBinding
import com.example.easycode.extensionfunctions.customToast
import com.example.easycode.tools.PhotoSaver
import com.google.zxing.BarcodeFormat
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

class GeneratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneratorBinding
    private lateinit var myBitmap: Bitmap
    private var type = 0
    private val nothing = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        type = intent.getIntExtra("type", 0)

        setupActionBar(type)

        binding.inputMess.addTextChangedListener {
            binding.btnSave.isVisible = false
            binding.image.setImageBitmap(nothing)
        }

        binding.btnSave.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE)
            } else {
                saveImage(myBitmap, type)
            }
        }

        binding.btnGenerate.setOnClickListener {
            if (type == 0){
                generate(BarcodeFormat.QR_CODE, binding.inputMess.text.toString().trim())
            } else
                generate(BarcodeFormat.CODE_128, binding.inputMess.text.toString().trim())
        }
    }

    private fun generate(type: BarcodeFormat, data: String) {
        val generator = Generator(applicationContext)

        val result = generator.generateCode(data, type)
        if (result is Bitmap) {
            myBitmap = result
            binding.image.setImageBitmap(result)
            binding.btnSave.isVisible = true

            customToast(
                resources.getString(R.string.msg_generated_successful),
                Toast.LENGTH_SHORT,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )
        }
    }

    private fun saveImage(bitmap: Bitmap, type: Int) {
        val photoSaver = PhotoSaver(applicationContext)

        photoSaver.saveImage(bitmap, type)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveImage(myBitmap, type)
        }
    }

    @SuppressLint("UseSupportActionBar")
    private fun setupActionBar(type: Int) {
        setActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener { returnToHome() }
        if (type == 0){
            actionBar!!.title = resources.getString(R.string.title_qrcode_generator)
        } else
            actionBar!!.title = resources.getString(R.string.title_barcode_generator)
    }

    private fun returnToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}
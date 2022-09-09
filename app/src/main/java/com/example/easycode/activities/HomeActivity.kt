package com.example.easycode.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.easycode.R
import com.example.easycode.databinding.ActivityHomeBinding

    const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 142

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE)

        }

        setActionBar(binding.toolBar)

        binding.generateQrCode.setOnClickListener { goToActivity(it.id) }
        binding.scanQrCode.setOnClickListener { goToActivity(it.id) }
        binding.generateBarCode.setOnClickListener { goToActivity(it.id) }
        binding.scanBarCode.setOnClickListener { goToActivity(it.id) }
    }

    private fun goToActivity(id: Int) {
        when (id) {
            R.id.generateQrCode -> startActivity(
                Intent(this, GenerateQrCodeActivity::class.java)
            )

            R.id.scanQrCode -> startActivity(
                Intent(this, ScanQrCodeActivity::class.java)
            )

            R.id.generateBarCode -> startActivity(
                Intent(this, GenerateBarCodeActivity::class.java)
            )

            R.id.scanBarCode -> startActivity(
                Intent(this, ScanBarCodeActivity::class.java)
            )
        }
    }

}
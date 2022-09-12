package com.example.easycode.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.easycode.R
import com.example.easycode.databinding.ActivityHomeBinding
import com.example.easycode.extensionfunctions.customToast

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
        binding.scanner.setOnClickListener { goToActivity(it.id) }
        binding.generateBarCode.setOnClickListener { goToActivity(it.id) }
    }

    private fun goToActivity(id: Int) {
        /** type: 0 == qrcode || type: 1 == barcode */
        when (id) {
            R.id.generateQrCode -> startActivity(
                Intent(this, GeneratorActivity::class.java).putExtra("type", 0)
            )

            R.id.scanner -> startActivity(
                Intent(this, ScannerActivity::class.java)
            )

            R.id.generateBarCode -> startActivity(
                Intent(this, GeneratorActivity::class.java).putExtra("type", 1)
            )
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        val permission2 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest(1)
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            makeRequest(2)
        }
    }

    private fun makeRequest(number: Int) {
        when (number) {
            1 -> {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
            2 -> {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                setupPermissions()
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
//                   setupPermissions()
//                }
            }
            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                setupPermissions()
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
//                    setupPermissions()
//                }
            }
        }
    }

}
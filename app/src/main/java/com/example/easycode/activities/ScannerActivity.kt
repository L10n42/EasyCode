package com.example.easycode.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.easycode.R
import com.example.easycode.databinding.ActivityScannerBinding
import com.example.easycode.extensionfunctions.customToast

class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()
        codeScanner()

        binding.btnBack.setOnClickListener {
            returnToHome()
        }
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.Scanner)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                startResultActivity(it.text)

            }

            errorCallback = ErrorCallback {
                Log.e("Scanner Activity", "Camera initialization error: ${it.message}")
                customToast(
                    resources.getString(R.string.error_cannot_initialize_the_camera),
                    Toast.LENGTH_SHORT,
                    resources.getColor(R.color.royal_purple),
                    resources.getColor(R.color.dodger_blue)
                )
            }
        }
    }

    private fun returnToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun startResultActivity(result: String){
        val intent = Intent(this, ScannerResultActivity::class.java).apply {
            putExtra("result", result)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        codeScanner.stopPreview()
        super.onPause()
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    customToast(
                        resources.getString(R.string.error_camera_permission_not_granted),
                        Toast.LENGTH_LONG,
                        resources.getColor(R.color.royal_purple),
                        resources.getColor(R.color.dodger_blue)
                    )
                }
            }
        }
    }
}
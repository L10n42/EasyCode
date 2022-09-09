package com.example.easycode.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.easycode.R
import com.example.easycode.databinding.ActivityGenerateQrCodeBinding
import com.example.easycode.extensionfunctions.customToast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path

class GenerateQrCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateQrCodeBinding
    private lateinit var qrCodeBitmap: Bitmap

    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener { returnToHome() }

        binding.inputMess.addTextChangedListener {
            binding.btnSave.isVisible = false
            val bitmap = null
            binding.ivQrCode.setImageBitmap(bitmap)
        }

        binding.btnSave.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE)
            } else {
                saveImage(qrCodeBitmap)
            }
        }

        binding.btnGenerate.setOnClickListener {
            generateQRCode()
        }
    }

    private fun generateQRCode() {
        if (binding.inputMess.text.toString() == ""){
            customToast(
                resources.getString(R.string.error_input_message),
                Toast.LENGTH_SHORT,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )
        } else {
            val multiFormatWriter = MultiFormatWriter()

            try {
                val bitMatrix: BitMatrix = multiFormatWriter.encode(binding.inputMess.text.toString(),
                    BarcodeFormat.QR_CODE, 800, 800)
                val barcodeEncoder = BarcodeEncoder()
                qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix)

                binding.ivQrCode.setImageBitmap(qrCodeBitmap)

                binding.btnSave.isVisible = true

                customToast(
                    resources.getString(R.string.msg_generated_successful),
                    Toast.LENGTH_SHORT,
                    resources.getColor(R.color.royal_purple),
                    resources.getColor(R.color.dodger_blue)
                )

            } catch (e: Exception) {
                Log.e("Generate QRCode error", e.stackTrace.toString())
                customToast(
                    resources.getString(R.string.error_generating_fail),
                    Toast.LENGTH_SHORT,
                    resources.getColor(R.color.royal_purple),
                    resources.getColor(R.color.dodger_blue)
                )
            }
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        if (bitmap != null) {
            try {
                val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .absolutePath //+ "/EasyCode"
                val dir = File(filePath)

                if (!dir.exists())
                    dir.mkdirs()

                val file = File(dir, "Qrcode_${getCurrentTime()}.png")
                val fileOutputStream = FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()

                customToast(
                    resources.getString(R.string.msg_saved_successfully),
                    Toast.LENGTH_SHORT,
                    resources.getColor(R.color.royal_purple),
                    resources.getColor(R.color.dodger_blue)
                )
            } catch (e: Exception) {
                customToast(
                    resources.getString(R.string.error_saving_fail),
                    Toast.LENGTH_SHORT,
                    resources.getColor(R.color.royal_purple),
                    resources.getColor(R.color.dodger_blue)
                )
            }
        } else {
            customToast(
                resources.getString(R.string.error_qrcode_is_empty),
                Toast.LENGTH_SHORT,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")

        return currentTime.format(formatter)
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
            saveImage(qrCodeBitmap)
        }
    }

    private fun returnToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}
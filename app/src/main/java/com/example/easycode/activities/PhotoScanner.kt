package com.example.easycode.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.easycode.R
import com.example.easycode.databinding.ActivityPhotoScannerBinding
import com.example.easycode.extensionfunctions.customToast
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class PhotoScanner : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoScannerBinding

    private var imageUri: Uri? = null

    private var barcodeScannerOptions: BarcodeScannerOptions? = null
    private var barcodeScanner: BarcodeScanner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUri = intent.getParcelableExtra("imgUri")

        binding.pickedImage.setImageURI(imageUri)

        barcodeScannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions!!)

        binding.btnScan.setOnClickListener {
            scanImage()
        }

        binding.btnChooseAnImg.setOnClickListener {
            pickImageGallery()
        }
    }

    private fun scanImage() {
        try {

            val inputImage = InputImage.fromFilePath(this, imageUri!!)

            val barcodeResult = barcodeScanner!!.process(inputImage)
                .addOnSuccessListener {barcodes ->

                    extractInfo(barcodes)

            }.addOnFailureListener {e ->
                Log.e("PhotoScanner", "scanImageError: ", e)
                    customToast(
                        "${resources.getString(R.string.error_failed_scanning)} ${e.message}",
                        Toast.LENGTH_SHORT,
                        resources.getColor(R.color.royal_purple),
                        resources.getColor(R.color.dodger_blue)
                    )
            }

        }catch (e: Exception) {
            Log.e("PhotoScanner", "scanImageError: ", e)
            customToast(
                "${resources.getString(R.string.error_failed_scanning)} ${e.message}",
                Toast.LENGTH_SHORT,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )
        }
    }

    private fun extractInfo(barcodes: List<Barcode>) {

        for (barcode in barcodes){
            val rawValue = barcode.rawValue
            startActivity(Intent(this, ScannerResultActivity::class.java)
                .putExtra("result", rawValue.toString()))
        }
    }

    private fun pickImageGallery() {

        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"

        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result ->
        if (result.resultCode == Activity.RESULT_OK){

            val data = result.data

            imageUri = data?.data

            binding.pickedImage.setImageURI(imageUri)

        } else {
            customToast(
                resources.getString(R.string.error_image_canceled),
                Toast.LENGTH_SHORT,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )
        }
    }

}
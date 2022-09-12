package com.example.easycode.tools

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.example.easycode.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.lang.Exception

class Generator(context: Context) {

    private val res = context.resources!!
    private val mContext = context

    fun generateCode(data: String, format: BarcodeFormat) : Any {
        val customToast = CustomToast(mContext)
        val error = 0
        val myBitmap: Bitmap

        if (data == ""){
            customToast.makeFullToast(
                res.getString(R.string.error_input_message),
                Toast.LENGTH_SHORT,
                res.getColor(R.color.royal_purple),
                res.getColor(R.color.dodger_blue)
            )
            return error
        } else {
            val multiFormatWriter = MultiFormatWriter()

            try {
                val bitMatrix: BitMatrix = multiFormatWriter.encode(data,
                    format, 800, 800)
                val barcodeEncoder = BarcodeEncoder()
                myBitmap = barcodeEncoder.createBitmap(bitMatrix)

                return myBitmap

            } catch (e: Exception) {
                Log.e("Generate error", e.stackTrace.toString())
                customToast.makeFullToast(
                    res.getString(R.string.error_generating_fail),
                    Toast.LENGTH_SHORT,
                    res.getColor(R.color.royal_purple),
                    res.getColor(R.color.dodger_blue)
                )
                return error
            }
        }
    }
}
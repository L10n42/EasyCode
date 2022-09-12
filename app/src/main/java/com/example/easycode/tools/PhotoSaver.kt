package com.example.easycode.tools

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import com.example.easycode.R
import com.example.easycode.extensionfunctions.customToast
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PhotoSaver(context: Context) {

    private val myContext = context
    private val res = myContext.resources

    fun saveImage(bitmap: Bitmap, type: Int) {
        val customToast = CustomToast(myContext)
        val fileName = listOf(
            "Qrcode_${getCurrentTime()}.png",
            "Barcode_${getCurrentTime()}.png"
        )

        try {
            val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath //+ "/EasyCode"
            val dir = File(filePath)

            if (!dir.exists())
                dir.mkdirs()

            val file = File(dir, fileName[type])
            val fileOutputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            customToast.makeFullToast(
                res.getString(R.string.msg_saved_successfully),
                Toast.LENGTH_SHORT,
                res.getColor(R.color.royal_purple),
                res.getColor(R.color.dodger_blue)
            )

        } catch (e: Exception) {
            customToast.makeFullToast(
                res.getString(R.string.error_saving_fail),
                Toast.LENGTH_SHORT,
                res.getColor(R.color.royal_purple),
                res.getColor(R.color.dodger_blue)
            )
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")

        return currentTime.format(formatter)
    }
}
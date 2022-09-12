package com.example.easycode.activities

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.easycode.R
import com.example.easycode.databinding.ActivityScannerResultBinding
import com.example.easycode.extensionfunctions.customToast
import java.lang.Exception
import java.net.URI

class ScannerResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerResultBinding
    private var isLink: Boolean = false

    private val pattern1 = "http://"
    private val pattern2 = "https://"
    private val pattern3 = ".com"

    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener {
            startActivity(Intent(this, ScannerActivity::class.java))
        }

        val result: String = intent.getStringExtra("result").toString().trim()
        binding.tvInformation.text = result

        initTypeOfResult(result)

        binding.btnCopy.setOnClickListener {
            copyText(result)
        }
        binding.btnVisitLink.setOnClickListener {
            visitLink(result)
        }
    }

    private fun visitLink(link: String) {
        try {
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse(checkLink(link))
            startActivity(openUrl)
        } catch (e: Exception){
            customToast(
                resources.getString(R.string.error_wrong_link),
                Toast.LENGTH_LONG,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )
        }
    }

    private fun checkLink(link: String): String {
        return if (link.startsWith(pattern1, true) || link.startsWith(pattern2, true)){
            link
        } else
            pattern1 + link
    }

    private fun copyText(text: String) {
        if (text.isNotEmpty()){
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            if (isLink){
                val uri: Uri = Uri.parse(text)
                val clipData = ClipData.newUri(contentResolver, "URI", uri)
                clipboardManager.setPrimaryClip(clipData)
            } else {
                val clipData = ClipData.newPlainText("text", text)
                clipboardManager.setPrimaryClip(clipData)
            }

            customToast(
                resources.getString(R.string.msg_copied),
                Toast.LENGTH_SHORT,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )

        } else {
            customToast(
                resources.getString(R.string.error_text_is_empty),
                Toast.LENGTH_SHORT,
                resources.getColor(R.color.royal_purple),
                resources.getColor(R.color.dodger_blue)
            )
        }
    }

    private fun resultIsText() {
        binding.ivType.setImageResource(R.drawable.ic_text_fields_24)
        binding.tvType.text = resources.getString(R.string.tv_title_type_text)
        binding.btnVisitLink.visibility = View.GONE
    }

    private fun resultIsLink() {
        binding.ivType.setImageResource(R.drawable.ic_link_rotation_135)
        binding.tvType.text = resources.getString(R.string.tv_title_type_link)
        binding.btnVisitLink.visibility = View.VISIBLE
    }

    private fun initTypeOfResult(result: String) {

        if (result.startsWith(pattern1, true) || result.startsWith(pattern2, true)
            || result.contains(pattern3, true) )
        {
            isLink = true
            resultIsLink()
        } else {
            isLink = false
            resultIsText()
        }
    }
}
package com.example.idecabe2.ui.camera

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.idecabe2.BottomNavigationActivity
import com.example.idecabe2.R
import com.example.idecabe2.databinding.ActivityImagePreviewBinding

class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding
    private var objBundle: String ? = null
    private var uri: Uri? = null
    private val TAG = "ImagePreviewActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //validation
        validaton()
        // Button Pressed
        binding.btnCancel.setOnClickListener {
            onCancel()
        }

        binding.btnChecklist.setOnClickListener {
            onChecklist()
        }
    }

    fun getBundle(){
            objBundle = intent.getStringExtra("image_uri")
            uri = objBundle?.toUri()
            Log.d(TAG, "getBundle: ${objBundle}")
    }

    private fun validaton(){
        if (uri == null){
            getBundle()
        }else {
            binding.cameraPrev.setImageURI(uri)
        }
        binding.cameraPrev.setImageURI(uri)
    }

    private fun onChecklist(){
        val intentToBottomNav = Intent(this@ImagePreviewActivity, BottomNavigationActivity::class.java)
        startActivity(intentToBottomNav)
        finish()
    }

    private fun onCancel(){
        if (uri.toString().isNullOrEmpty()){
            Toast.makeText(this@ImagePreviewActivity, "Photo tidak tersedia", Toast.LENGTH_SHORT).show()
            val intentToBottomNavigationActivity = Intent(this@ImagePreviewActivity, BottomNavigationActivity::class.java)
            startActivity(intentToBottomNavigationActivity)
        }else {
            uri?.toFile()!!.delete()
            Toast.makeText(this@ImagePreviewActivity, "Photo is Canceled", Toast.LENGTH_SHORT).show()
            val intentToBottomNavigationActivity = Intent(this@ImagePreviewActivity, BottomNavigationActivity::class.java)
            startActivity(intentToBottomNavigationActivity)
        }

    }

}
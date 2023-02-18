package com.example.idecabe2.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.location.LocationRequestCompat
import com.example.idecabe2.R
import com.example.idecabe2.databinding.ActivityCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {
    //init binding
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var objId: String
    private lateinit var objUser_id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()
        startCamera()
        binding.buttonPhoto.setOnClickListener{takePhoto()}
    }


    companion object {
        private const val TAG = "CameraActivity"
        private const val FILE_NAME_FORMAT = "yyyy-MM-dd-ss-SSS"
        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            //Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPrev.surfaceProvider)
                }


            //Take Picture
            imageCapture = ImageCapture.Builder().build()



//            val imageAnalyzer = ImageAnalysis.Builder()
//                .build()
//                .also {
//                    it.setAnalyzer(cameraExcecutor, LuminosityAnalyzer { luma ->
//                        Log.d(TAG, "Average luminosity: $luma")
//                    })
//                }
            //        Set Default Back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
//                 Unbind All Camera
                cameraProvider.unbindAll()
//            Bind uses case Camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            }catch (e: Exception){
                Log.e(TAG, "Use Case Binding Failed ", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(){
        //return nilai image Capture
        val imageCapture = imageCapture ?: return
        //Create Name Format
        val name =SimpleDateFormat(FILE_NAME_FORMAT, Locale.US).format(System.currentTimeMillis()).toString()
        val photo_name = (name + objId + objUser_id)

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, photo_name)
            put(MediaStore.MediaColumns.MIME_TYPE, "images/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

//        Create options objectwhich contains file & Metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

//        Set up imagCaptureListener & show it on Image Preview
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object: ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo Captured : ${outputFileResults.savedUri}"
                    val uri =outputFileResults.savedUri
                    val name = uri.toString()
                    Toast.makeText(this@CameraActivity, msg, Toast.LENGTH_LONG).show()
                    val intentImagePreview = Intent(this@CameraActivity, ImagePreviewActivity::class.java)
                    intentImagePreview.putExtra("image_uri", name)
                    startActivity(intentImagePreview)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "onError: Photo Captured Error ",exception )
                }
            }
        )
    }

    private fun getBundle(){
        objId = intent.getStringExtra("bundle_project_image").toString()
        objUser_id = intent.getStringExtra("bundle_userId").toString()
    }

}
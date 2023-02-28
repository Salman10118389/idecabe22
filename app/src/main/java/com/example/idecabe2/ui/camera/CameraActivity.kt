package com.example.idecabe2.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Camera
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.databinding.ActivityCameraBinding
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CameraActivity: AppCompatActivity() {
    //init binding
    private val FOCUS_AREA_SIZE: Int = 300
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var objId: String
    private lateinit var objBundle: Bundle

    private lateinit var bundleProject : Bundle

    private lateinit var objProject: Project
    private var objUser_id: String? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getObject()
        startCamera()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startCamera()
        }
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

    private fun getObject(){
            intent.let {
                bundleProject = it.getBundleExtra("project")!!
            }
        //unwrap bundle
        objProject = bundleProject.getParcelable("project")!!
        Log.d(TAG, "getObject: ${objProject.images}")

//        bundleProject = this.objProject.getBundle("project")
//        Log.d(TAG, "getObject: ${objProject}")
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.R)
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
            val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            val cameraControl = camera.cameraControl

            // Use the CameraControl instance to enable the torch
            val enableTorchLF: ListenableFuture<Void> = cameraControl.enableTorch(true)
            enableTorchLF.addListener({
                try {
                    enableTorchLF.get()
                    // At this point, the torch has been successfully enabled
                } catch (exception: Exception) {
                    // Handle any potential errors
                }
            }, mainExecutor /* Executor where the runnable callback code is run */)
            binding.cameraPrev.setOnTouchListener(View.OnTouchListener { view: View, motionEvent: MotionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> return@OnTouchListener true
                    MotionEvent.ACTION_UP -> {
                        // Get the MeteringPointFactory from PreviewView
                        val factory = binding.cameraPrev.meteringPointFactory

                        // Create a MeteringPoint from the tap coordinates
                        val point = factory.createPoint(motionEvent.x, motionEvent.y)

                        // Create a MeteringAction from the MeteringPoint, you can configure it to specify the metering mode
                        val action = FocusMeteringAction.Builder(point).build()

                        // Trigger the focus and metering. The method returns a ListenableFuture since the operation
                        // is asynchronous. You can use it get notified when the focus is successful or if it fails.
                        cameraControl.startFocusAndMetering(action)

                        return@OnTouchListener true
                    }
                    else -> return@OnTouchListener false
                }
            })

//            val meteringPointfactory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                DisplayOrientedMeteringPointFactory(
//                    display!!,
//                    camera.cameraInfo,
//
//                )
//            } else {
//                TODO("VERSION.SDK_INT < R")
//            }
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
        //getAutoFocus Function
        val imageCapture = imageCapture ?: return
        //Create Name Format
        val name =SimpleDateFormat(FILE_NAME_FORMAT, Locale.US).format(System.currentTimeMillis()).toString()
        //nanti collaborator diganti dengan user.id dari getSession
        val photo_name = (name + objProject.id + objProject.user_id)

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
                    intentImagePreview.putExtra("project_bundle", bundleProject)
                    startActivity(intentImagePreview)
                    Log.d(TAG, "onImageSaved: ${name}")
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "onError: Photo Captured Error ",exception )
                }
            }
        )
        finish()
    }

}



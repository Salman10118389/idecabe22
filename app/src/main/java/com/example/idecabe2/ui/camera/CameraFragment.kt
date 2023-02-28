package com.example.idecabe2.ui.camera

import android.R
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.databinding.FragmentCameraBinding
import com.example.idecabe2.ui.auth.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService


@AndroidEntryPoint
class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private val cameraViewModel: CameraViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private var imageUris:MutableList<Uri> = arrayListOf()

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutorService: ExecutorService
    private lateinit var objProject: Project


    //connect to adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCamera()
        binding.buttonPhoto.setOnClickListener{
            takePhoto()
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun takePhoto(){
        // Get a stable reference of modifiable image Capture Use Case
        var imagaCapture: ImageCapture = imageCapture ?: return

//      Create Timestamp Name & MediaStore Directory
        val imageName = SimpleDateFormat(FILE_NAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (SDK_INT > Build.VERSION_CODES.M){
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/IdeCabe-Image")
            }
        }

//      Create options object contains file + metadata
        val outputOptions = activity?.contentResolver?.let {
            ImageCapture.OutputFileOptions
                .Builder(
                    it,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build()
        }
        if (outputOptions != null) {
            imageCapture!!.takePicture(
                outputOptions,
                activity?.let { ContextCompat.getMainExecutor(it.applicationContext) }!!,
                object: ImageCapture.OnImageSavedCallback{
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val msg ="Photo capture succeeded: ${outputFileResults.savedUri}"
                        val uri = outputFileResults.savedUri
                        val name = uri.toString()
//                        Send data to firebase
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e(TAG, "onError: ${exception.message}", exception)
                    }
                }
            )
        }
    }

    companion object {
        private const val FILE_NAME_FORMAT = "yyyy-MM-dd-SSS"
        private const val TAG = "Camera Fragment"
    }

//    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all{
//        activity?.let { it1 -> ContextCompat.checkSelfPermission(it1.baseContext, it) } == PackageManager.PERMISSION_GRANTED
//    }

//    companion object {
//
//        val PERMISSIONS = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//        private const val FILE_NAME_FORMAT = "yyyy-MM-dd-SSS"
////        private const val REQUEST_PERMISSION_CODE = 10
////
//////        Required Permission
////        private val REQUIRED_PERMISSIONS =
////            mutableListOf(
////                android.Manifest.permission.CAMERA
////            ).apply {
////                if (SDK_INT <= Build.VERSION_CODES.P){
////                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
////                }
////            }.toTypedArray()
//    }


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_PERMISSION_CODE){
//            if (allPermissionGranted())
//                startCamera()
//        }else {
//            toast("Permission not granted by the user")
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }


    

    private fun startCamera() {
        val cameraProvideFuture = activity?.let { ProcessCameraProvider.getInstance(it.applicationContext) }

        cameraProvideFuture?.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProvideFuture.get()
            //camera preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider { binding.cameraPrev.surfaceProvider }
                }

            //Take Picture
            imageCapture =ImageCapture.Builder().build()
            //Camera Selector
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this@CameraFragment, cameraSelector,preview , imageCapture)
            }catch (e: Exception){
                Log.e(TAG, "errorCamera: ", e)
            }
        }, activity?.let { ContextCompat.getMainExecutor(it.applicationContext) })
            objProject = arguments?.getParcelable("project")!!
            Log.d(TAG, "startCamera: $objProject")
        }
}
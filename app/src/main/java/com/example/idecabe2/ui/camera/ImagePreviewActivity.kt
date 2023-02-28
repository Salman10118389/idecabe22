package com.example.idecabe2.ui.camera

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.idecabe2.BottomNavigationActivity
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.data.reporitory.ProjectRepository
import com.example.idecabe2.databinding.ActivityImagePreviewBinding
import com.example.idecabe2.ui.auth.AuthViewModel
import com.example.idecabe2.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
@AndroidEntryPoint
class ImagePreviewActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityImagePreviewBinding
    private var objBundle: String ? = null
    private lateinit var projectBundle: Bundle
    private lateinit var objProject: Project
    private var imagesUri: MutableList<Uri> = arrayListOf()
    private var imagesString: MutableList<String> = arrayListOf()
    var fileUri: Uri? = null
    private val authViewModel: AuthViewModel by viewModels()
    private val cameraViewModel: CameraViewModel by viewModels()
    private val TAG = "ImagePreviewActivity"
    private lateinit var objUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //validation
        validaton()
        bundleWrapper()
        // Button Pressed
        binding.btnCancel.setOnClickListener {
            onCancel()
        }

        binding.btnChecklist.setOnClickListener {
            onChecklist()
            cameraViewModel.onUploadMultipleFiles(imagesUri){state ->
                when(state){
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        Toast.makeText(this@ImagePreviewActivity, "uri : " + imagesUri, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.failure -> {

                    }
                }
            }
        }
    }

    fun getBundle(){
            objBundle = intent.getStringExtra("image_uri")
            fileUri = objBundle?.toUri()
            projectBundle = intent.getBundleExtra("project_bundle")!!
        Log.d(TAG, "getBundle: ${objBundle}")
        }


    private fun validaton(){
        if (fileUri == null){
            getBundle()
        }else {
            binding.cameraPrev.setImageURI(fileUri)
        }
        binding.cameraPrev.setImageURI(fileUri)
    }

//    private fun getImagesUrls(): List<String> {
//        if (imagesUris.isNotEmpty()){
//            return imagesUris.map { it.toString()}
//        }else {
//            return objProject?.images ?: arrayListOf()
//        }
//    }

    private fun bundleWrapper(){
        getBundle()
        objProject = projectBundle.getParcelable<Project>("project")!!
        Log.d(TAG, "bundleWrapper: ${objProject}")
    }


    private fun getUserObject(){
        authViewModel.getSession {user ->
            if (objUser == null){
                if (user != null) {
                    objUser = user
                } else {
                    Toast.makeText(this@ImagePreviewActivity, "getSession() failed", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@ImagePreviewActivity, "user is set", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onChecklist(){
        Log.d(TAG, "onChecklist: ${imagesUri}")
        imagesString = objProject.images.toMutableList()
        imagesUri.add(fileUri!!)
        imagesUri.forEach{
            imagesString.add(it.toString())
        }
        if (imagesString != null){
            objProject.images = imagesString
            Log.d(TAG, "onChecklist: $imagesString")
        }
        objProject?.let { cameraViewModel.updateProject(it)}
        Log.d(TAG, "onChecklist: ${objProject}")
        val intentToBottomNav = Intent(this@ImagePreviewActivity, BottomNavigationActivity::class.java)
        startActivity(intentToBottomNav)
        finish()
    }

    private fun onCancel(){
        if (fileUri.toString().isNullOrEmpty()){
            Toast.makeText(this@ImagePreviewActivity, "Photo tidak tersedia", Toast.LENGTH_SHORT).show()
            val intentToCameraActivity = Intent(this@ImagePreviewActivity, CameraActivity::class.java)
            intentToCameraActivity.putExtra("project", objBundle)
            startActivity(intentToCameraActivity)
        }else {
            var file = fileUri?.toFile()
            file?.delete()
            Toast.makeText(this@ImagePreviewActivity, "Photo is Canceled", Toast.LENGTH_SHORT).show()
            val intentToCameraActivity = Intent(this@ImagePreviewActivity, CameraActivity::class.java)
            intentToCameraActivity.putExtra("project", projectBundle)
            Log.d(TAG, "onCancel: ${projectBundle}")
            startActivity(intentToCameraActivity)
        }
    }

}
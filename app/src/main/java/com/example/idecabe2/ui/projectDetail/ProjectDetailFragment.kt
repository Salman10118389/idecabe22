package com.example.idecabe2.ui.projectDetail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.R
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.databinding.FragmentHomeBinding
import com.example.idecabe2.databinding.FragmentProjectDetailBinding
import com.example.idecabe2.ui.camera.CameraActivity
import com.example.idecabe2.ui.home.HomeAdapter
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ProjectDetailFragment"
@AndroidEntryPoint
class ProjectDetailFragment : Fragment() {

    private var imageUri: MutableList<Uri> = arrayListOf()
    private lateinit var binding: FragmentProjectDetailBinding
    private lateinit var objProject: Project

    //connect to adapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectDetailBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return binding.root
    }

//    private val startForProfileImageResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            val resultCode = result.resultCode
//            val data = result.data
//
//            if (resultCode == Activity.RESULT_OK) {
//                //Image Uri will not be null for RESULT_OK
//                val fileUri = data?.data!!
//
//                imageUri.add(fileUri)
//                binding.image.setImageURI(fileUri)
//            } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//            }
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProjectBundle()
        binding.btnUploadImages.setOnClickListener {
            camera()
        }
    }
    fun getProjectBundle(){
        arguments.let {
            objProject = it?.getParcelable("project")!!
            Log.d(TAG, "getProjectBundle: $it")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    //Move to Fragment Camera

    private fun hasPermissions(context: Context, licenses: Array<String>): Boolean = licenses.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { licenses ->
            val granted = licenses.entries.all {
                it.value == true
            }
            if (granted) {

            }
        }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
//        private const val REQUEST_PERMISSION_CODE = 10
//
////        Required Permission
//        private val REQUIRED_PERMISSIONS =
//            mutableListOf(
//                android.Manifest.permission.CAMERA
//            ).apply {
//                if (SDK_INT <= Build.VERSION_CODES.P){
//                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                }
//            }.toTypedArray()
    }

    private fun camera(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            displayCameraFragment()
            return
        }else {
            activity?.let {
                if (hasPermissions(activity as Context, PERMISSIONS)){
                    displayCameraFragment()
                }else {
                    permReqLauncher.launch(
                        PERMISSIONS
                    )
                }
            }
        }
    }

    private fun displayCameraFragment(){
        val bundle = Bundle()
        bundle.putString("project_id", objProject.id)
        val bundle_userId = Bundle()
        bundle_userId.putString("user_id", objProject.user_id)
        Log.d(TAG, "onViewCreated: ${objProject.id}")
        val intentCamera = Intent(activity?.applicationContext, CameraActivity::class.java)
        intentCamera.putExtra("bundle_project_image", bundle)
        intentCamera.putExtra("bundle_userId", bundle_userId)
        startActivity(intentCamera)
    }
}
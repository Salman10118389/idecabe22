package com.example.idecabe2.ui.projectDetail

import android.Manifest
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.idecabe2.R
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.databinding.FragmentProjectDetailBinding
import com.example.idecabe2.ui.camera.CameraActivity
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ProjectDetailFragment"
@AndroidEntryPoint
class ProjectDetailFragment : Fragment() {

    private var imageUri: MutableList<Uri> = arrayListOf()
    private lateinit var binding: FragmentProjectDetailBinding
    private lateinit var objProject: Project
    private lateinit var imageListingAdapter: ImageListingAdapter
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
        imageListingAdapter = ImageListingAdapter(imageUri)
        binding.rvPhoto.adapter = imageListingAdapter
        binding.btnUploadImages.setOnClickListener {
            camera()
        }
    }
    fun getProjectBundle(){
        arguments.let {
            objProject = it?.getParcelable("project")!!
            Log.d(TAG, "getProjectBundle: $it")
        }
        processImageUri()
    }

    private fun processImageUri(){
        objProject.images.forEach{
            imageUri.add(it.toUri())
        }
        Log.d(TAG, "processImageUri: ${objProject.images}")
        Log.d(TAG, "processImageUriImageUri: ${imageUri}")
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
                it.value
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
        bundle.putParcelable("project", objProject)
        //Data Images.array, user_parent, id_project
            Log.d(TAG, "onViewCreated: ${objProject.id}")
        val intentCamera = Intent(activity?.applicationContext, CameraActivity::class.java)
        intentCamera.putExtra("project", bundle)
        startActivity(intentCamera)
//        findNavController().navigate(R.id.action_projec_detail_to_camera, bundle)
    }

    private fun displayAddCollaborator(){
        val bundle = Bundle()
        bundle.putParcelable("project", objProject)
        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_to_add_collaborator_fragment, bundle)
        }
    }
}
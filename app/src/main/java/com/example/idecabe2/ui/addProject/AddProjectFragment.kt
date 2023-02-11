package com.example.idecabe2.ui.addProject

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.idecabe2.R
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.databinding.FragmentAddProjectBinding
import com.example.idecabe2.databinding.FragmentNotificationsBinding
import com.example.idecabe2.ui.auth.AuthViewModel
import com.example.idecabe2.utils.UiState
import com.example.idecabe2.utils.hide
import com.example.idecabe2.utils.show
import com.example.idecabe2.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddProjectFragment : Fragment() {
    val TAG: String = "AddProjectFragment"


    val addProjectViewModel: AddProjectViewModel by viewModels()
    private lateinit var binding: FragmentAddProjectBinding
    val authViewModel: AuthViewModel by viewModels()
    private var objProject: Project? = null
    var imageUris: MutableList<Uri> = arrayListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProjectBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.setOnClickListener{
            if (validation()){
                onSavePressed()
                findNavController().navigate(R.id.navigation_home)
            }

        }
    }


    fun createProject(){
        if (validation()){
            if(objProject == null){
                addProjectViewModel.addProject(getProject())
            }else {

            }
        }
        addProjectViewModel.addProject.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    toast(state.data.second)
                    if (objProject == null){
                        addProjectViewModel.addProject(getProject())
                    }
                    objProject = state.data.first
                    Log.d(TAG, "createProject: " + objProject!!.user_id)
                    Log.d(TAG, "createProjectname: " + objProject!!.name_project)
                }
            }
        }
    }



    private fun validation(): Boolean {
        var isValid = true
        if (binding.etProjectName.text.isNullOrEmpty()){
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun getProject(): Project{
        return Project(
            id = objProject?.user_id ?: "",
            name_project = binding.etProjectName.text.toString(),
            label = binding.etLabel.text.toString(),
            mask = binding.etMaskingSelection.text.toString(),
            images = getImagesUri(),
            date = Date()
        ).apply { authViewModel.getSession { this.user_id = it?.user_id ?: "" } }

    }

    private fun getImagesUri(): List<String> {
        if (imageUris.isNotEmpty()){
            return imageUris.map {
                it.toString()
            }
        }else {
            return objProject?.images ?: arrayListOf()
        }
    }

    private fun onSavePressed(){
        if (imageUris.isNotEmpty())
        {
            addProjectViewModel.uploadSingleFile(imageUris.first()){state ->
                when(state){
                    is UiState.Loading -> {
                        binding.progressBar.show()
                    }
                    is UiState.Success -> {
                        binding.progressBar.hide()
                    }
                    is UiState.failure -> {
                        binding.progressBar.hide()
                    }
                }
            }
        }else {
            createProject()
        }
    }

}
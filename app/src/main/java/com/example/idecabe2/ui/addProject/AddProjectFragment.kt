package com.example.idecabe2.ui.addProject

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


    val addProjectViewModel: AddProjectViewModel by viewModels()
    private lateinit var binding: FragmentAddProjectBinding
    val authViewModel: AuthViewModel by viewModels()

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
            createProject()
        }
    }

    fun createProject(){
        if (validation()){
            addProjectViewModel.addProject(
                Project(
                    id = "",
                    name_project = binding.etProjectName.text.toString(),
                    label = binding.etLabel.text.toString(),
                    mask = binding.etMaskingSelection.text.toString(),
                    date = Date()
                ).apply { authViewModel.getSession { this.user_id = user_id } }
            )

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
                    toast(state.data)
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
}
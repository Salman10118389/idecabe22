package com.example.idecabe2.ui.add_collaborator

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.idecabe2.MainActivity
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.ProjectCollaboration
import com.example.idecabe2.databinding.FragmentAccountBinding
import com.example.idecabe2.databinding.FragmentAddCollaboratorBinding
import com.example.idecabe2.ui.auth.AuthViewModel
import com.example.idecabe2.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCollaboratorFragment : Fragment() {
    private lateinit var binding: FragmentAddCollaboratorBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var projectCollaboration: ProjectCollaboration? = null
    private var project: Project? = null
    companion object {
        fun newInstance() = AddCollaboratorFragment()
    }
   private val addCollaboratorViewModel: AddCollaboratorViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCollaboratorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserSession()
        binding.btnCollaborator.setOnClickListener{
            projectCollaboration?.let { it1 -> addCollaboratorViewModel.addCollab(projectCollab = it1) }
        }

        addCollaboratorViewModel.addCollab.observe(viewLifecycleOwner){state ->
            when(state) {
                is UiState.Loading -> {

                }
                is UiState.Success -> {

                }
                is UiState.failure -> {

                }
            }
        }
    }

    private fun getBUndleFromProjectDetail(){
        project = arguments?.getParcelable("project")
        projectCollaboration?.project_id = project!!.id
    }

    private fun getUserSession(){
        getBUndleFromProjectDetail()
        addCollaboratorViewModel.getSession {user ->
                if (user != null){
                    project?.user_id = user.user_id
                } else {
                Toast.makeText(activity?.applicationContext, "Project Collaboration Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
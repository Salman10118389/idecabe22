package com.example.idecabe2.ui.add_collaborator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.ProjectCollaboration
import com.example.idecabe2.data.model.User
import com.example.idecabe2.data.reporitory.AuthRepository
import com.example.idecabe2.data.reporitory.ProjectCollabRepo
import com.example.idecabe2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCollaboratorViewModel @Inject constructor(val projectCollaborationRepo: ProjectCollabRepo, val authRepo: AuthRepository) : ViewModel() {
    private val _addCollab = MutableLiveData<UiState<String>>()
    val addCollab: LiveData<UiState<String>>
    get() = _addCollab
    fun addCollab(
        projectCollab: ProjectCollaboration
    ){
        _addCollab.value = UiState.Loading
        projectCollaborationRepo.addProjectCollab(projectCollab){
            _addCollab.value = it
        }
    }

    fun getSession(result: (User?) -> Unit) {
        authRepo.getSession(result)
    }


}
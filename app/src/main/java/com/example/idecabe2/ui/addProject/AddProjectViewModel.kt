package com.example.idecabe2.ui.addProject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.data.reporitory.ProjectRepository
import com.example.idecabe2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddProjectViewModel @Inject constructor(val repo: ProjectRepository) : ViewModel() {
    //Add Project
    private val _addProject =MutableLiveData<UiState<String>>()
    val addProject: LiveData<UiState<String>>
        get() = _addProject
    fun addProject(project: Project){
        _addProject.value = UiState.Loading
        repo.addProject(project){
            _addProject.value = it
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}
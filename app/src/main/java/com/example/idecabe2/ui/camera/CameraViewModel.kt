package com.example.idecabe2.ui.home

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
class HomeViewModel @Inject constructor(val repo: ProjectRepository) : ViewModel() {

    private val _getProjects = MutableLiveData<UiState<List<Project>>>()
    val getProjects: LiveData<UiState<List<Project>>>
        get() = _getProjects
    fun getProjects(user: User?) {
        _getProjects.value = UiState.Loading
        repo.getProjects(user) {
            _getProjects.value = it
        }
    }

}
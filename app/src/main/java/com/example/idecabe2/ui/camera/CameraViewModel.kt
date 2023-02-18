package com.example.idecabe2.ui.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.data.reporitory.ProjectRepository
import com.example.idecabe2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(val repo: ProjectRepository) : ViewModel() {

    private val _getProjects = MutableLiveData<UiState<List<Project>>>()
    val getProjects: LiveData<UiState<List<Project>>>
        get() = _getProjects
    fun getProjects(user: User?) {
        _getProjects.value = UiState.Loading
        repo.getProjects(user) {
            _getProjects.value = it
        }
    }

    fun onUploadSingleFile(fileUris: Uri, onResult: (UiState<Uri>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repo
                .uploadSingleFile(fileUris, onResult)
        }
    }

    fun onUploadMultipleFiles(fileUris: List<Uri>,onResult: (UiState<List<Uri>>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repo.uploadMultipleFile(fileUris, onResult)
        }
    }



}
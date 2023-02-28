package com.example.idecabe2.ui.projectDetail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.reporitory.ProjectRepository
import com.example.idecabe2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.UnitsRelations
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(val repo: ProjectRepository) : ViewModel() {

//    private val _getProjects = MutableLiveData<UiState<List<Project>>>()
//    val getProjects: LiveData<UiState<List<Project>>>
//        get() = _getProjects
//    fun getProjects() {
//        _getProjects.value = UiState.Loading
//        repo.getProjects {
//            _getProjects.value = it
//        }
//    }


}
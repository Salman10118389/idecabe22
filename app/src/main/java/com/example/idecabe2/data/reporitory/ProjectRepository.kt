package com.example.idecabe2.data.reporitory

import android.provider.ContactsContract.CommonDataKinds.Note
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.utils.UiState

interface ProjectRepository {
    fun getProjects(user: User?, result: (UiState<List<Project>>) -> Unit)
    fun addProject(project: Project, result: (UiState<String>) -> Unit)
    fun updateProject(project: Project, result: (UiState<String>) -> Unit)
}
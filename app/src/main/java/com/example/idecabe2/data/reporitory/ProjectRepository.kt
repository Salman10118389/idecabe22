package com.example.idecabe2.data.reporitory

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Note
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.utils.UiState

interface ProjectRepository {
    fun getProjects(user: User?, result: (UiState<List<Project>>) -> Unit)
    fun addProject(project: Project, result: (UiState<Pair<Project, String>>) -> Unit)
    fun updateProject(project: Project, result: (UiState<String>) -> Unit)
    suspend fun uploadSingleFile(fileUri: Uri, onResult: (UiState<Uri>) -> Unit)
    suspend fun uploadMultipleFile(fileUir: List<Uri>, onResult: (UiState<Uri>) -> Unit)
}
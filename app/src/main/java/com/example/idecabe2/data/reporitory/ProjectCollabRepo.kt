package com.example.idecabe2.data.reporitory

import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.ProjectCollaboration
import com.example.idecabe2.utils.UiState

interface ProjectCollabRepo {
    fun addProjectCollab(projectCollaboration: ProjectCollaboration, result: (UiState<String>) -> Unit)
}
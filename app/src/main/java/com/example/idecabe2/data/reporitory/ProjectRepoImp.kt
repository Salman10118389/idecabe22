package com.example.idecabe2.data.reporitory

import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.utils.FireStoreDocumentField
import com.example.idecabe2.utils.FirestoreTable
import com.example.idecabe2.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore

class ProjectRepoImp(val database: FirebaseFirestore): ProjectRepository {
    override fun getProjects(user: User?, result: (UiState<List<Project>>) -> Unit) {
        database.collection(FirestoreTable.PROJECT)
            .whereEqualTo(FireStoreDocumentField.USER_ID, user?.user_id)
            .get()
            .addOnSuccessListener {
                val projects = arrayListOf<Project>()
                for (document in it){
                    val project = document.toObject(Project::class.java)
                    projects.add(project)
                }
                result.invoke(
                    UiState.Success(projects)
                )
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.failure(
                        it.localizedMessage.toString()
                    )
                )
            }
    }

    override fun addProject(project: Project, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTable.PROJECT).document()
        project.id = document.id
        document.set(project)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Project has been Created...")
                )
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.failure(it.localizedMessage.toString())
                )
            }
    }

    override fun updateProject(project: Project, result: (UiState<String>) -> Unit) {

    }


}
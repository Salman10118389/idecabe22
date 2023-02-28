package com.example.idecabe2.data.reporitory

import com.example.idecabe2.data.model.ProjectCollaboration
import com.example.idecabe2.utils.FireStoreDocumentField
import com.example.idecabe2.utils.FirestoreTable
import com.example.idecabe2.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore

class ProjectCollabRepoImp(private val database: FirebaseFirestore): ProjectCollabRepo{
    override fun addProjectCollab(
        projectCollaboration: ProjectCollaboration,
        result: (UiState<String>) -> Unit
    ) {
        database.collection(FirestoreTable.PROJECT_COLLABORATION)
            .add(projectCollaboration)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Collaboration has been created")
                )
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.failure(it.localizedMessage)
                )
            }
    }

}
package com.example.idecabe2.data.reporitory

import android.net.Uri
import android.util.Log
import com.example.idecabe2.data.model.Project
import com.example.idecabe2.data.model.User
import com.example.idecabe2.utils.FireStoreDocumentField
import com.example.idecabe2.utils.FirestoreTable
import com.example.idecabe2.utils.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProjectRepoImp(private val database: FirebaseFirestore, private val storageReference: StorageReference): ProjectRepository {
    override fun getProjects(user: User?, result: (UiState<List<Project>>) -> Unit) {
        database.collection(FirestoreTable.PROJECT)
            .whereEqualTo(FireStoreDocumentField.USER_ID, user?.user_id)
            .orderBy(FireStoreDocumentField.DATE, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val projects = arrayListOf<Project>()
                Log.d("TAG", "getProjects: " + user?.user_id)
                for (document in it){
                    val project = document.toObject(Project::class.java)
                    projects.add(project)
                }
                result.invoke(
                    UiState.Success(projects)
                )
                Log.d("TAG", "getProjects: " + (user?.user_id ?: ""))
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.failure(
                        it.localizedMessage.toString()
                    )
                )
                Log.e("TAG", "getProjects: " + (user?.user_id ?: ""))
            }
    }

    override fun addProject(project: Project, result: (UiState<Pair<Project, String>>) -> Unit) {
        val document = database.collection(FirestoreTable.PROJECT).document()
        project.id = document.id
        document.set(project)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(Pair(project, "Project has been Created..."))
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

    override suspend fun uploadSingleFile(fileUri: Uri, onResult: (UiState<Uri>) -> Unit) {
        try {
            val uri: Uri = withContext(Dispatchers.IO){
                storageReference
                    .putFile(fileUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            onResult.invoke(UiState.Success(uri))
        }catch (e: FirebaseException){
            onResult.invoke(UiState.failure(e.message.toString()))
        }catch (e: java.lang.Exception){
            onResult.invoke(UiState.failure(e.message.toString()))
        }
    }

    override suspend fun uploadMultipleFile(fileUir: List<Uri>, onResult: (UiState<Uri>) -> Unit) {

    }


}
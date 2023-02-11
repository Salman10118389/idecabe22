package com.example.idecabe2.di

import android.content.SharedPreferences
import com.example.idecabe2.data.reporitory.AuthRepository
import com.example.idecabe2.data.reporitory.AuthRepositoryImp
import com.example.idecabe2.data.reporitory.ProjectRepoImp
import com.example.idecabe2.data.reporitory.ProjectRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object RepoModule {
    @Provides
    @Singleton
    fun provideProjectRepo(database: FirebaseFirestore, storageReference: StorageReference): ProjectRepository{
        return ProjectRepoImp(database, storageReference)
    }

    @Provides
    @Singleton
    fun provideAuthRepo(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
        appReferences: SharedPreferences,
        gson: Gson
        ): AuthRepository{
        return AuthRepositoryImp(auth, database, appReferences, gson)
    }
}
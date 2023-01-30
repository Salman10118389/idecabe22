package com.example.idecabe2.data.reporitory

import android.content.SharedPreferences
import com.example.idecabe2.data.model.User
import com.example.idecabe2.utils.FirestoreTable
import com.example.idecabe2.utils.UiState
import com.example.idecabe2.utils.sharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class AuthRepositoryImp(val auth: FirebaseAuth,
                        val database: FirebaseFirestore,
                        val appReferences: SharedPreferences,
                        val gson: Gson
): AuthRepository {
    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    user.user_id = it.result.user?.uid ?: ""
                    updateUserInfo(user) {state ->
                        when(state){
                            is UiState.Success -> {
                                result.invoke(UiState.Success("User Registration Successfull.."))
                            }
                            is UiState.failure -> {
                                result.invoke(
                                    UiState.failure(state.error)
                                )
                            }
                            is UiState.Loading -> {

                            }
                        }
                    }
                }else{
                    try {
                        throw it.exception ?: java.lang.Exception("Invalid Credentials")
                    }catch (e: FirebaseAuthWeakPasswordException){
                        result.invoke(
                            UiState.failure("Weak Password, Please enter 6 minimum characters Password")
                        )
                    }
                    catch (e: FirebaseAuthInvalidCredentialsException){
                        result.invoke(
                            UiState.failure("Authentication Failed, Pleases enter valid email")
                        )
                    }
                    catch (e: FirebaseAuthUserCollisionException){
                        result.invoke(
                            UiState.failure("Email has been Registered, try another email..")
                        )
                    }catch (e: Exception){
                        result.invoke(UiState.failure(e.message.toString()))
                    }
                }
            }
            .addOnFailureListener{
                 result.invoke(
                     UiState.failure(
                         it.localizedMessage
                     )
                 )
            }
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreTable.USERS).document(user.user_id)
        document.set(user)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("User has been updated Successfully..")
                )
            }
            .addOnFailureListener{
                UiState.failure(it.localizedMessage.toString())
            }
    }


    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    val id = it.result.user?.uid.toString()
                    storeSession(id = id ?: ""){
                        if (it == null) {
                            result.invoke(
                                UiState.failure("Failed to Store Local Session")
                            )
                        }else {
                            result.invoke(
                                UiState.Success("Login Successfull...")
                            )
                        }
                    }
                }
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.failure("Authentication Failed, Please enter the right email & password")
                )
            }
    }

    override fun forgotPassword(user: User, result: (UiState<String>) -> Unit) {

    }

    override fun storeSession(id: String, result: (User?) -> Unit) {
        database.collection(FirestoreTable.USERS).document(id)
            .get()
            .addOnCompleteListener{
                if (it.isSuccessful){
                    val user = it.result.toObject(User::class.java)
                    appReferences.edit().putString(sharedPreferences.USER_SESSION, gson.toJson(user)).apply()
                    result.invoke(user)
                }else{
                    result.invoke(null)
                }
            }
            .addOnFailureListener{
                result.invoke(null)
            }
    }

    override fun getSession(result: (User?) -> Unit) {
        val uses_str = appReferences.getString(sharedPreferences.USER_SESSION, null)

        if (uses_str == null){
            result.invoke(null)
        }else {
            val user =gson.fromJson(uses_str, User::class.java)
            result.invoke(user)
        }
    }

}
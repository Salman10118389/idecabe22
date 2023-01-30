package com.example.idecabe2.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.idecabe2.data.model.User
import com.example.idecabe2.data.reporitory.AuthRepository
import com.example.idecabe2.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel@Inject constructor(val authRepository: AuthRepository) : ViewModel() {
    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
    get() = _register

    fun registerUser(
        email: String,
        password: String,
        user: User
    ){
        _register.value = UiState.Loading
        authRepository.registerUser(
            email = email,
            password = password,
            user = user
        ){
            _register.value = it
        }
    }

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
    get() = _login

    fun login(
        email: String,
        password: String
    ){
        _login.value =UiState.Loading
        authRepository.loginUser(email, password){
            _login.value = it
        }
    }

    fun getSession(result: (User?) -> Unit) {
        authRepository.getSession(result)
    }


}
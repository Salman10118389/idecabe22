package com.example.idecabe2.ui.account

import androidx.lifecycle.ViewModel
import com.example.idecabe2.data.reporitory.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(val authRepo: AuthRepository) : ViewModel() {
    fun logout(result: () -> Unit){
        authRepo.logout { result }
    }
}
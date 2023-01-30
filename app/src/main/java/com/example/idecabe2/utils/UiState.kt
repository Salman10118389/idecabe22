package com.example.idecabe2.utils

sealed class UiState<out T> {
    //Loading Success Failure
    object Loading: UiState<Nothing>()
    data class Success<out T>(val data: T): UiState<T>()
    data class failure(val error: String): UiState<Nothing>()
}
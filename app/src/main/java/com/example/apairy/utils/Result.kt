package com.example.apairy.utils

sealed class Answer<T>(val data:T? = null, val errorMessage:String?= null){

    class Success<T>(data:T, errorMessage: String? = null): Answer<T>(data, errorMessage)
    class Error<T>(errorMessage: String, data: T? = null): Answer<T>(data, errorMessage)
    class Loading<T>: Answer<T>()

}
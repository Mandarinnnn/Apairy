package com.example.apairy.repository

import com.example.apairy.database.MistakeDao
import com.example.apairy.remote.api.ApiaryApi
import com.example.apairy.remote.models.User
import com.example.apairy.utils.Answer
import com.example.apairy.utils.SessionManager
import javax.inject.Inject

class MyRepository @Inject constructor(val api: ApiaryApi, val mistakeDao: MistakeDao,val sessionManager: SessionManager){

    suspend fun register(user: User): Answer<String> {
        return try {
//            if(!isNetworkConnected(sessionManager.context)){
//                Answer.Error<String>("No Internet")
//            }

            val result = api.register(user)
            if(result.success){
                sessionManager.saveToken(result.message)
                sessionManager.saveEmail(user.email)
                Answer.Success("Registered Successfully")
            }else{
                Answer.Error<String>(result.message)
            }
        }catch (e: Exception){
            //e.printStackTrace()
            if(e.message != null){
                if(e.message!!.contains("after 10000ms")){
                    Answer.Error<String>("Не удалось установить связь с сервером")
                }else if(e.message!!.contains("409")){
                    Answer.Error<String>("Пользователь с данной эл. почтой уже существует")
                }else{
                    Answer.Error<String>(e.message ?: "Some Problem Occurred")
                }
            }else{
                Answer.Error<String>(e.message ?: "Some Problem Occurred")
            }
        }
    }

    suspend fun login(user: User): Answer<String> {
        return try {
//            if(!isNetworkConnected(sessionManager.context)){
//                Answer.Error<String>("No Internet")
//            }

            val result = api.login(user)
            if(result.success){
                sessionManager.saveToken(result.message)
                sessionManager.saveEmail(user.email)
                Answer.Success("Login Successfully")
            }else{
                Answer.Error<String>("result.message")
            }
        }catch (e: Exception){
            //e.printStackTrace()
            if(e.message != null){
                if(e.message!!.contains("after 10000ms")){
                    Answer.Error<String>("Не удалось установить связь с сервером")
                }else if(e.message!!.contains("401")){
                    Answer.Error<String>("Не правильные логин или пароль")
                } else{
                    Answer.Error<String>(e.message ?: "Some Problem Occurred")
                }
            }else{
                Answer.Error<String>(e.message ?: "Some Problem Occurred")
            }
        }
    }

   fun logout(): Answer<String> {
        return try{
            sessionManager.logout()
            Answer.Success("Logout Successfully")
        }catch (e: Exception){
            e.printStackTrace()
            Answer.Error<String>(e.message ?: "Some Problem Occurred")
        }
    }





}
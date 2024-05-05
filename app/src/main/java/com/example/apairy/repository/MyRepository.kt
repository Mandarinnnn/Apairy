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
                Answer.Success("Registered Successfully")
            }else{
                Answer.Error<String>(result.message)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Answer.Error<String>(e.message ?: "Some Problem Occurred")
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
                Answer.Success("Login Successfully")
            }else{
                Answer.Error<String>(result.message)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Answer.Error<String>(e.message ?: "Some Problem Occurred")
        }
    }

    suspend fun logout(): Answer<String> {
        return try{
            sessionManager.logout()
            Answer.Success("Logout Successfully")
        }catch (e: Exception){
            e.printStackTrace()
            Answer.Error<String>(e.message ?: "Some Problem Occurred")
        }
    }





}
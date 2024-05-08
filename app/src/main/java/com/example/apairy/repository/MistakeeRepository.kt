package com.example.apairy.repository

import androidx.lifecycle.LiveData
import com.example.apairy.database.MistakeDao
import com.example.apairy.models.Mistake
import com.example.apairy.remote.api.ApiaryApi
import com.example.apairy.remote.models.RemoteMistake
import com.example.apairy.utils.Answer
import com.example.apairy.utils.SessionManager
import javax.inject.Inject

class MistakeeRepository @Inject constructor(val api: ApiaryApi, val mistakeDao: MistakeDao, val sessionManager: SessionManager){

    val getAllMistakes: LiveData<List<Mistake>> = mistakeDao.getAllMistakes()

    suspend fun createMistake(mistake: Mistake): Answer<String>{
        return try {
            val token = sessionManager.getToken()
            mistakeDao.insertMistake(mistake)

            val id = mistakeDao.getMaxId()

            val remoteMistake = RemoteMistake(mistake.id, mistake.name, mistake.solution, mistake.year)

            val response = api.createMistake("Bearer $token", remoteMistake)


            if(response.success){
                val _mistake = Mistake(mistake.name, mistake.solution, mistake.year,
                    false, false, false, mistake.id,)
                mistakeDao.updateMistake(_mistake)
                Answer.Success("Saved")
            } else {
                Answer.Error(response.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Answer.Error(e.message ?: "Some Problem")
        }
    }

    suspend fun updateMistake(mistake: Mistake): Answer<String>{
        return try {
            val token = sessionManager.getToken()
            mistakeDao.updateMistake(mistake)

            val remoteMistake = RemoteMistake(mistake.id, mistake.name, mistake.solution, mistake.year)

            val response = api.updateMistake("Bearer $token", remoteMistake)


            if(response.success){
                mistakeDao.updateMistake(mistake.also{it.isLocallyUpdated = false})
                Answer.Success("Updated")
            } else {
                Answer.Error(response.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Answer.Error(e.message ?: "Some Problem")
        }
    }


    suspend fun deleteMistake(mistake: Mistake){
        try {
            mistakeDao.deleteMistakeLocally(mistake.id)
            val token = sessionManager.getToken()
            val response = api.deleteMistake("Bearer $token", mistake.id)

            if(response.success){
                mistakeDao.deleteMistake(mistake)
                Answer.Success("Deleted")
            } else {
                Answer.Error(response.message)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun syncMistakes(){
        //val token = sessionManager.getToken()

        try {
            val locallyNewMistakes = mistakeDao.getAllLocallyNewMistakes()
            locallyNewMistakes.forEach{
                createMistake(it)
            }

            val locallyUpdatedMistakes = mistakeDao.getAllLocallyUpdatedMistakes()
            locallyUpdatedMistakes.forEach{
                updateMistake(it)
            }

            val locallyDeletedMistakes = mistakeDao.getAllLocallyDeletedMistakes()
            locallyDeletedMistakes.forEach {
                deleteMistake(it)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }


    }


    suspend fun getAllRemoteMistakes():Answer<String> {
        return try{
            val token = sessionManager.getToken()
            val remoteMistakes = api.getAllMistakes("Bearer $token")



            remoteMistakes.forEach {
                val mistake = Mistake(it.name, it.solution, it.year, false,
                    false, false, it.id)
                mistakeDao.insertMistake(mistake)
            }
            Answer.Success("Success")
        } catch (e:Exception){
            e.printStackTrace()
            Answer.Error("Error")
        }



    }

    suspend fun syncState():Answer<String>{
        return try{
            val token = sessionManager.getToken()
            val remoteMistakes = api.getAllMistakes("Bearer $token")


            Answer.Success("Success")
        } catch (e:Exception){
            Answer.Error("Error")
        }
    }

    suspend fun deleteAll(){
        mistakeDao.deleteAllMistakes()
    }

}
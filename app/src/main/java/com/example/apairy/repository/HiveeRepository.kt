package com.example.apairy.repository

import androidx.lifecycle.LiveData
import com.example.apairy.database.HiveDao
import com.example.apairy.database.MigrationDao
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState
import com.example.apairy.models.HoneyAmountCounts
import com.example.apairy.models.Migration
import com.example.apairy.models.StrengthCounts
import com.example.apairy.models.relations.HiveWithStates
import com.example.apairy.remote.api.ApiaryApi
import com.example.apairy.remote.models.RemoteHive
import com.example.apairy.remote.models.RemoteHiveState
import com.example.apairy.remote.models.RemoteMigration
import com.example.apairy.utils.Answer
import com.example.apairy.utils.SessionManager
import javax.inject.Inject

class HiveeRepository@Inject constructor(val api: ApiaryApi, val hiveDao: HiveDao, val sessionManager: SessionManager)  {


    val getAllHives: LiveData<List<Hive>> = hiveDao.getAllHives()

    suspend fun createHive(hive: Hive): Answer<String> {
        return try {
            val token = sessionManager.getToken()

            hiveDao.createHive(hive)

            val remoteHive = RemoteHive(hive.id, hive.name, hive.frameCount,
                hive.queenYear, hive.note, hive.isMarked)

            val response = api.createHive("Bearer $token", remoteHive)


            if(response.success){
                val _hive = Hive(hive.name, hive.frameCount,
                    hive.queenYear, hive.note, hive.isMarked, false,
                    hive.isLocallyUpdated, hive.isLocallyDeleted,hive.id)
                hiveDao.updateHive(_hive)
                Answer.Success("Saved")
            } else {
                Answer.Error(response.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Answer.Error(e.message ?: "Some Problem")
        }
    }

    suspend fun updateHive(hive: Hive): Answer<String> {
        return try {
            val token = sessionManager.getToken()
            hiveDao.updateHive(hive)

            val remoteHive = RemoteHive(hive.id, hive.name, hive.frameCount,
                hive.queenYear, hive.note, hive.isMarked)

            val response = api.updateHive("Bearer $token", remoteHive)


            if(response.success){
                hiveDao.updateHive(hive.also{it.isLocallyUpdated = false})
                Answer.Success("Updated")
            } else {
                Answer.Error(response.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Answer.Error(e.message ?: "Some Problem")
        }
    }


    suspend fun deleteHive(hive: Hive){
        try {
            hiveDao.deleteHiveLocally(hive.id)

            val states: List<HiveState>? = hiveDao.getStatesForHive(hive.id).value

            states?.forEach {
                hiveDao.deleteHiveStateLocally(it.id)
            }


            val token = sessionManager.getToken()
            val response = api.deleteHive("Bearer $token", hive.id)

            if(response.success){
                hiveDao.deleteHive(hive)
                states?.forEach {
                    hiveDao.deleteHiveState(it)
                }
                Answer.Success("Deleted")
            } else {
                Answer.Error(response.message)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun syncHives(){
        //val token = sessionManager.getToken()

        try {
            val locallyNewHives = hiveDao.getAllLocallyNewHives()
            locallyNewHives.forEach{
                createHive(it)
            }

            val locallyUpdatedHives = hiveDao.getAllLocallyUpdatedHives()
            locallyUpdatedHives.forEach{
                updateHive(it)
            }

            val locallyDeletedHives = hiveDao.getAllLocallyDeletedHives()
            locallyDeletedHives.forEach {
                deleteHive(it)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }


    }


    suspend fun getAllRemoteHives(){
        try{
            val token = sessionManager.getToken()
            val remoteHives = api.getAllHives("Bearer $token")

            remoteHives.forEach {
                val hive =  Hive(it.name, it.frameCount,
                    it.queenYear, it.note, it.isMarked, false,
                    false, false,it.id)


                hiveDao.createHive(hive)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }



//    suspend fun insert(hiveState: HiveState){
//        hiveDao.insertHiveState(hiveState)
//    }

//    suspend fun delete(hiveState: HiveState){
//        hiveDao.deleteHiveState(hiveState)
//    }

    fun getHiveWithStates(hiveId: String): LiveData<HiveWithStates> {
        return hiveDao.getHiveWithStates(hiveId)
    }

    fun getStatesForHive(hiveId: String): LiveData<List<HiveState>> {
        return hiveDao.getStatesForHive(hiveId)
    }

    suspend fun createHiveState(hiveState: HiveState): Answer<String> {
        return try {
            val token = sessionManager.getToken()

            hiveDao.createHiveState(hiveState)

            //val id = hiveDao.getMaxIdHiveState()

            val remoteHiveState = RemoteHiveState(hiveState.id, hiveState.hiveId, hiveState.strength,
                hiveState.framesWithBrood, hiveState.honey, hiveState.date)

            val response = api.createHiveState("Bearer $token", remoteHiveState)


            if(response.success){
                val _hiveState =  HiveState(hiveState.hiveId, hiveState.strength,
                    hiveState.framesWithBrood, hiveState.honey, hiveState.date, false,
                    hiveState.isLocallyDeleted,hiveState.id)
                hiveDao.updateHiveState(_hiveState)
                Answer.Success("Saved")
            } else {
                Answer.Error(response.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Answer.Error(e.message ?: "Some Problem")
        }
    }

    suspend fun deleteHiveState(hiveState: HiveState){
        try {
            hiveDao.deleteHiveStateLocally(hiveState.id)
            val token = sessionManager.getToken()
            val response = api.deleteHiveState("Bearer $token", hiveState.id, hiveState.hiveId)

            if(response.success){
                hiveDao.deleteHiveState(hiveState)
                Answer.Success("Deleted")
            } else {
                Answer.Error(response.message)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }


    suspend fun syncHiveStates(){
        //val token = sessionManager.getToken()

        try {
            val locallyNewHiveStates = hiveDao.getAllLocallyNewHiveStates()
            locallyNewHiveStates.forEach{
                createHiveState(it)
            }

            val locallyDeletedHiveStates = hiveDao.getAllLocallyDeletedHiveStates()
            locallyDeletedHiveStates.forEach {
                deleteHiveState(it)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun getAllRemoteHiveStates(){
        try{
            val token = sessionManager.getToken()
            val remoteHiveStates = api.getAllHiveStates("Bearer $token")

            remoteHiveStates.forEach {
                val hiveState =  HiveState( it.hiveId, it.strength,
                    it.frame, it.honey, it.date, false,
                    false,it.id)


                hiveDao.createHiveState(hiveState)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }











    suspend fun getTotalStrengthCounts(): StrengthCounts {
        return hiveDao.getTotalStrengthCounts()
    }

    suspend fun getTotalHoneyAmount(): HoneyAmountCounts {
        return hiveDao.getTotalHoneyAmount()
    }
}
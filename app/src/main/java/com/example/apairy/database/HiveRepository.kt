package com.example.apairy.database

import androidx.lifecycle.LiveData
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState
import com.example.apairy.models.HoneyAmountCounts
import com.example.apairy.models.StrengthCounts
import com.example.apairy.models.relations.HiveWithStates

class HiveRepository (private val hiveDao: HiveDao){

    val getAllHives: LiveData<List<Hive>> = hiveDao.getAllHives()

    suspend fun insert(hive: Hive){
        hiveDao.insertHive(hive)
    }

    suspend fun delete(hive: Hive){
        hiveDao.deleteHive(hive)
    }

    suspend fun update(hive: Hive){
        hiveDao.updateHive(hive)
    }

    suspend fun insert(hiveState: HiveState){
        hiveDao.insertHiveState(hiveState)
    }

    suspend fun delete(hiveState: HiveState){
        hiveDao.deleteHiveState(hiveState)
    }

    fun getHiveWithStates(hiveId: Int): LiveData<HiveWithStates> {
        return hiveDao.getHiveWithStates(hiveId)
    }

    fun getStatesForHive(hiveId: Int): LiveData<List<HiveState>> {
        return hiveDao.getStatesForHive(hiveId)
    }

    suspend fun getTotalStrengthCounts(): StrengthCounts{
        return hiveDao.getTotalStrengthCounts()
    }

    suspend fun getTotalHoneyAmount(): HoneyAmountCounts{
        return hiveDao.getTotalHoneyAmount()
    }
}
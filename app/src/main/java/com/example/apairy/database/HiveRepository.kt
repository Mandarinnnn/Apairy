package com.example.apairy.database

import androidx.lifecycle.LiveData
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState
import com.example.apairy.models.HoneyAmountCounts
import com.example.apairy.models.StrengthCounts
import com.example.apairy.models.relations.HiveWithStates
import javax.inject.Inject

class HiveRepository @Inject constructor(private val hiveDao: HiveDao){

    val getAllHives: LiveData<List<Hive>> = hiveDao.getAllHives()

    suspend fun insert(hive: Hive){
        hiveDao.createHive(hive)
    }

    suspend fun delete(hive: Hive){
        hiveDao.deleteHive(hive)
    }

    suspend fun update(hive: Hive){
        hiveDao.updateHive(hive)
    }

    suspend fun insert(hiveState: HiveState){
        hiveDao.createHiveState(hiveState)
    }

    suspend fun delete(hiveState: HiveState){
        hiveDao.deleteHiveState(hiveState)
    }

    fun getHiveWithStates(hiveId: String): LiveData<HiveWithStates> {
        return hiveDao.getHiveWithStates(hiveId)
    }

    fun getStatesForHive(hiveId: String): LiveData<List<HiveState>> {
        return hiveDao.getStatesForHive(hiveId)
    }

    suspend fun getTotalStrengthCounts(): StrengthCounts{
        return hiveDao.getTotalStrengthCounts2()
    }

    suspend fun getTotalHoneyAmount(): HoneyAmountCounts{
        return hiveDao.getTotalHoneyAmount()
    }
}
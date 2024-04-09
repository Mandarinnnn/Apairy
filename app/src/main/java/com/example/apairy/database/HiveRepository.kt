package com.example.apairy.database

import androidx.lifecycle.LiveData
import com.example.apairy.models.Hive

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

}
package com.example.apairy.repository

import androidx.lifecycle.LiveData
import com.example.apairy.database.MigrationDao
import com.example.apairy.database.MistakeDao
import com.example.apairy.models.Migration
import com.example.apairy.models.Mistake
import com.example.apairy.remote.api.ApiaryApi
import com.example.apairy.remote.models.RemoteMigration
import com.example.apairy.remote.models.RemoteMistake
import com.example.apairy.utils.Answer
import com.example.apairy.utils.SessionManager
import com.example.apairy.utils.isWifiEnabled
import javax.inject.Inject

class MigrationnRepository @Inject constructor(val api: ApiaryApi, val migrationDao: MigrationDao, val sessionManager: SessionManager) {

    val getAllMigrations: LiveData<List<Migration>> = migrationDao.getAllMigrations()

    suspend fun createMigration(migration: Migration): Answer<String> {
        return try {
            val token = sessionManager.getToken()

            migrationDao.insertMigration(migration)


            val remoteMigration = RemoteMigration(migration.id, migration.name, migration.hiveCount,
                migration.startDate, migration.endDate, migration.latitude, migration.longitude,
                migration.note, migration.imageURI)

            val response = api.createMigration("Bearer $token", remoteMigration)


            if(response.success){
                val _migration = Migration(migration.name, migration.hiveCount,
                    migration.startDate, migration.endDate, migration.latitude, migration.longitude,
                    migration.note, migration.imageURI, false, migration.isLocallyUpdated,
                    migration.isLocallyUpdated, migration.id,)
                migrationDao.updateMigration(_migration)
                Answer.Success("Saved")
            } else {
                Answer.Error(response.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Answer.Error(e.message ?: "Some Problem")
        }
    }

    suspend fun updateMigration(migration: Migration): Answer<String> {
        return try {
            val token = sessionManager.getToken()
            migrationDao.updateMigration(migration)

            val remoteMigration = RemoteMigration(migration.id, migration.name, migration.hiveCount,
                migration.startDate, migration.endDate, migration.latitude, migration.longitude,
                migration.note, migration.imageURI)

            val response = api.updateMigration("Bearer $token", remoteMigration)


            if(response.success){
                migrationDao.updateMigration(migration.also{it.isLocallyUpdated = false})
                Answer.Success("Updated")
            } else {
                Answer.Error(response.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Answer.Error(e.message ?: "Some Problem")
        }
    }


    suspend fun deleteMigration(migration: Migration){
        try {
            migrationDao.deleteMigrationLocally(migration.id)
            val token = sessionManager.getToken()
            val response = api.deleteMigration("Bearer $token", migration.id)

            if(response.success){
                migrationDao.deleteMigration(migration)
                Answer.Success("Deleted")
            } else {
                Answer.Error(response.message)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    suspend fun syncMigrations(){
        //val token = sessionManager.getToken()

        try {
            val locallyNewMistakes = migrationDao.getAllLocallyNewMigrations()
            locallyNewMistakes.forEach{
                createMigration(it)
            }

            val locallyUpdatedMistakes = migrationDao.getAllLocallyUpdatedMigrations()
            locallyUpdatedMistakes.forEach{
                updateMigration(it)
            }

            val locallyDeletedMistakes = migrationDao.getAllLocallyDeletedMigrations()
            locallyDeletedMistakes.forEach {
                deleteMigration(it)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }


    }


    suspend fun getAllRemoteMigrations(){
        try{
            val token = sessionManager.getToken()
            val remoteMigration = api.getAllMigration("Bearer $token")

            remoteMigration.forEach {
                val migration = Migration(it.name, it.hiveCount,
                   it.startDate, it.endDate, it.latitude, it.longitude,
                    it.note, it.imageURI, false, false, false,it.id)
                migrationDao.insertMigration(migration)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }



    }

    suspend fun deleteAll(){
        migrationDao.deleteAllMigrations()
    }

}
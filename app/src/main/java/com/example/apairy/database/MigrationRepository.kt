package com.example.apairy.database

import androidx.lifecycle.LiveData
import com.example.apairy.models.Hive
import com.example.apairy.models.Migration

class MigrationRepository(private val migrationDao: MigrationDao) {


    val getAllMigrations: LiveData<List<Migration>> = migrationDao.getAllMigrations()


    suspend fun insert(migration: Migration){
        migrationDao.insertMigration(migration)
    }

    suspend fun delete(migration: Migration){
        migrationDao.deleteMigration(migration)
    }

    suspend fun update(migration: Migration){
        migrationDao.updateMigration(migration)
    }
}
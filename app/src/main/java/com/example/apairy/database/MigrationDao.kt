package com.example.apairy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apairy.models.Migration


@Dao
interface MigrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMigration(migration: Migration)

    @Update
    suspend fun updateMigration(migration: Migration)

    @Delete
    suspend fun deleteMigration(migration: Migration)


    @Query("SELECT * FROM migration_table ORDER BY id ASC")
    fun getAllMigrations(): LiveData<List<Migration>>
}
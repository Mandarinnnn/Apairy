package com.example.apairy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apairy.models.Migration
import com.example.apairy.models.Mistake


@Dao
interface MigrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMigration(migration: Migration)

    @Update
    suspend fun updateMigration(migration: Migration)

    @Delete
    suspend fun deleteMigration(migration: Migration)

    @Query("UPDATE migration_table SET isLocallyDeleted = 1 WHERE id = :id")
    suspend fun deleteMigrationLocally(id: String)


    @Query("SELECT * FROM migration_table WHERE isLocallyDeleted = 0 ORDER BY id ASC")
    fun getAllMigrations(): LiveData<List<Migration>>


    @Query("SELECT * FROM migration_table WHERE isLocallyNew = 1")
    fun getAllLocallyNewMigrations(): List<Migration>

    @Query("SELECT * FROM migration_table WHERE isLocallyUpdated = 1")
    fun getAllLocallyUpdatedMigrations(): List<Migration>

    @Query("SELECT * FROM migration_table WHERE isLocallyDeleted = 1")
    fun getAllLocallyDeletedMigrations(): List<Migration>

    @Query("SELECT MAX(id) FROM migration_table")
    fun getMaxId(): Int

    @Query("DELETE FROM migration_table")
    suspend fun deleteAllMigrations()

}
package com.example.apairy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apairy.models.Mistake

@Dao
interface MistakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: Mistake)

    @Update
    suspend fun updateMistake(mistake: Mistake)

    @Delete
    suspend fun deleteMistake(mistake: Mistake)

    @Query("UPDATE mistakes_table SET isLocallyDeleted = 1 WHERE id = :id")
    suspend fun deleteMistakeLocally(id: Int)


    @Query("SELECT * FROM mistakes_table WHERE isLocallyDeleted = 0 ORDER BY id ASC")
    fun getAllMistakes(): LiveData<List<Mistake>>


    @Query("SELECT * FROM mistakes_table WHERE isSynced = 0")
    fun getAllNotSyncedMistakes(): List<Mistake>

    @Query("SELECT * FROM mistakes_table WHERE isLocallyDeleted = 1")
    fun getAllLocallyDeletedMistakes(): List<Mistake>

}

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
    suspend fun deleteMistakeLocally(id: String)


    @Query("SELECT * FROM mistakes_table WHERE isLocallyDeleted = 0 ORDER BY id ASC")
    fun getAllMistakes(): LiveData<List<Mistake>>


    @Query("SELECT * FROM mistakes_table WHERE isLocallyNew = 1")
    fun getAllLocallyNewMistakes(): List<Mistake>

    @Query("SELECT * FROM mistakes_table WHERE isLocallyUpdated = 1")
    fun getAllLocallyUpdatedMistakes(): List<Mistake>

    @Query("SELECT * FROM mistakes_table WHERE isLocallyDeleted = 1")
    fun getAllLocallyDeletedMistakes(): List<Mistake>


    @Query("SELECT MAX(id) FROM mistakes_table")
    fun getMaxId(): Int

    @Query("DELETE FROM mistakes_table")
    suspend fun deleteAllMistakes()


}

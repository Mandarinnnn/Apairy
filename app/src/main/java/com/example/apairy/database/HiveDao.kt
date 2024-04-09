package com.example.apairy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apairy.models.Hive


@Dao
interface HiveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHive(hive: Hive)

    @Update
    suspend fun updateHive(hive: Hive)

    @Delete
    suspend fun deleteHive(hive: Hive)


    @Query("SELECT * FROM hives_table ORDER BY id ASC")
    fun getAllHives(): LiveData<List<Hive>>


    @Query("UPDATE hives_table Set name = :name, frame = :frame, honey = :honey, strength = :strength, weight = :weight, note = :note WHERE id = :id")
    suspend fun update(id: Int?, name: String?, frame: Int?,honey: Int?,
                       strength: Int?,weight: Int?,note: String?,)
}
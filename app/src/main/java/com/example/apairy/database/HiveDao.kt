package com.example.apairy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState
import com.example.apairy.models.HoneyAmountCounts
import com.example.apairy.models.StrengthCounts
import com.example.apairy.models.relations.HiveWithStates


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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHiveState(hiveState: HiveState)

    @Delete
    suspend fun deleteHiveState(hiveState: HiveState)


    @Query("SELECT * FROM hives_table WHERE id = :hiveId")
    fun getHiveWithStates(hiveId: Int): LiveData<HiveWithStates>


    @Query("SELECT * FROM hive_states_table WHERE hiveId = :hiveId ORDER BY id DESC")
    fun getStatesForHive(hiveId: Int): LiveData<List<HiveState>>




    @Query("SELECT SUM(CASE WHEN strength >= 9 THEN 1 ELSE 0 END) AS strongCount, " +
            "       SUM(CASE WHEN strength BETWEEN 4 AND 8 THEN 1 ELSE 0 END) AS mediumCount, " +
            "       SUM(CASE WHEN strength BETWEEN 0 AND 3 THEN 1 ELSE 0 END) AS weakCount " +
            "FROM (" +
            "SELECT strength FROM hive_states_table WHERE id IN (SELECT MAX(id) FROM hive_states_table GROUP BY hiveId)"+
        ") AS latest_states")
    suspend fun getTotalStrengthCounts(): StrengthCounts


    @Query("SELECT SUM(CASE WHEN honey > 9 THEN 1 ELSE 0 END) AS honeyFrom9ToMore, " +
            "       SUM(CASE WHEN honey > 7.0 AND honey <= 9.0 THEN 1 ELSE 0 END) AS honeyFrom7To9, " +
            "       SUM(CASE WHEN honey > 5.0 AND honey <= 7.0 THEN 1 ELSE 0 END) AS honeyFrom5To7, " +
            "       SUM(CASE WHEN honey > 3.0 AND honey <= 5.0 THEN 1 ELSE 0 END) AS honeyFrom3To5, " +
            "       SUM(CASE WHEN honey >= 1.0 AND honey <= 3.0 THEN 1 ELSE 0 END) AS honeyFrom1To3 " +
            "FROM (" +
            "SELECT honey FROM hive_states_table WHERE id IN (SELECT MAX(id) FROM hive_states_table GROUP BY hiveId)"+
            ") AS latest_states")
    suspend fun getTotalHoneyAmount(): HoneyAmountCounts

//    @Query("UPDATE hives_table Set name = :name, frame = :frame, honey = :honey, strength = :strength, weight = :weight, note = :note WHERE id = :id")
//    suspend fun update(id: Int?, name: String?, frame: Int?,honey: Int?,
//                       strength: Int?,weight: Int?,note: String?,)
}
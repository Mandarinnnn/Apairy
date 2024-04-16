package com.example.apairy.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "hive_states_table")
@Parcelize
data class HiveState(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "hiveId") val hiveId: Int,
    @ColumnInfo(name = "strength") val strength: Int,
    @ColumnInfo(name = "framesWithBrood") val framesWithBrood: Int,
    @ColumnInfo(name = "honey") val honey: Float,
    @ColumnInfo(name = "date") val date: String
) : Parcelable
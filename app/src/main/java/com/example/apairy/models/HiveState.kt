package com.example.apairy.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Entity(tableName = "hive_states_table")
@Parcelize
data class HiveState(
    @ColumnInfo(name = "hiveId") val hiveId: String,
    @ColumnInfo(name = "strength") val strength: Int,
    @ColumnInfo(name = "framesWithBrood") val framesWithBrood: Int,
    @ColumnInfo(name = "honey") val honey: Float,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isLocallyNew") var isLocallyNew: Boolean?,
    @ColumnInfo(name = "isLocallyDeleted") var isLocallyDeleted: Boolean?,
    @PrimaryKey(autoGenerate = false) var id: String = UUID.randomUUID().toString(),
) : Parcelable
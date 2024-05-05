package com.example.apairy.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.UUID


@Entity(tableName = "hives_table")
@Parcelize
data class Hive(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "frameCount") val frameCount: Int,
    @ColumnInfo(name = "queenYear") val queenYear: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "isMarked") val isMarked: Boolean,
    @ColumnInfo(name = "isLocallyNew") var isLocallyNew: Boolean?,
    @ColumnInfo(name = "isLocallyUpdated") var isLocallyUpdated: Boolean?,
    @ColumnInfo(name = "isLocallyDeleted") var isLocallyDeleted: Boolean?,
    @PrimaryKey(autoGenerate = false) var id: String = UUID.randomUUID().toString(),
) : Parcelable




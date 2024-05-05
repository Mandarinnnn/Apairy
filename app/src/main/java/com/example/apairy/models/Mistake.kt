package com.example.apairy.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.UUID


@Entity(tableName = "mistakes_table")
@Parcelize
data class Mistake(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "solution") val solution: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "isLocallyNew") var isLocallyNew: Boolean?,
    @ColumnInfo(name = "isLocallyUpdated") var isLocallyUpdated: Boolean?,
    @ColumnInfo(name = "isLocallyDeleted") var isLocallyDeleted: Boolean?,
    @PrimaryKey(autoGenerate = false) var id: String = UUID.randomUUID().toString(),
): Parcelable

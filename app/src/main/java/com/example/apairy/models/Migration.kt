package com.example.apairy.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.UUID


@Entity(tableName = "migration_table")
@Parcelize
data class Migration(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "hive_count") val hiveCount: Int,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "imageURI") val imageURI: String?,
    @ColumnInfo(name = "isLocallyNew") var isLocallyNew: Boolean?,
    @ColumnInfo(name = "isLocallyUpdated") var isLocallyUpdated: Boolean?,
    @ColumnInfo(name = "isLocallyDeleted") var isLocallyDeleted: Boolean?,
    @PrimaryKey(autoGenerate = false) var id: String = UUID.randomUUID().toString(),
) : Parcelable

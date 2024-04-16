package com.example.apairy.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveState

data class HiveWithStates(
    @Embedded val hive: Hive,
    @Relation(
        parentColumn = "id",
        entityColumn = "hiveId"
    )
    val states: List<HiveState>
)

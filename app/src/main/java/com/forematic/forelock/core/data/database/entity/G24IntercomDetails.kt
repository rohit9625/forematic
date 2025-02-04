package com.forematic.forelock.core.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class G24IntercomDetails(
    @Embedded val intercom: G24IntercomEntity,

    @Relation(parentColumn = "id", entityColumn = "intercomId")
    val callOutNumbers: List<CallOutNumberEntity>,

    @Relation(parentColumn = "id", entityColumn = "intercomId")
    val outputRelays: List<OutputRelayEntity>
)

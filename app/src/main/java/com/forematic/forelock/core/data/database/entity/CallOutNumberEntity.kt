package com.forematic.forelock.core.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "call_out_number",
    foreignKeys = [
        ForeignKey(
            entity = G24IntercomEntity::class,
            parentColumns = ["id"],
            childColumns = ["intercomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CallOutNumberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val intercomId: Int,
    val name: String,
    val number: String
)

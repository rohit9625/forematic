package com.forematic.forelock.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "output_relay",
    foreignKeys = [
        ForeignKey(
            entity = G24IntercomEntity::class,
            parentColumns = ["id"],
            childColumns = ["intercomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OutputRelayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val intercomId: Int,
    @ColumnInfo(name = "output_name")
    val outputName: String,
    @ColumnInfo(name = "relay_time")
    val relayTime: Int,
    @ColumnInfo(name = "keypad_code")
    val keypadCode: String?,
    @ColumnInfo("keypad_code_location")
    val keypadCodeLocation: String?,
    @ColumnInfo(name = "status_text")
    val statusText: String
)

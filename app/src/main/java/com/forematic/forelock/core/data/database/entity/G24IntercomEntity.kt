package com.forematic.forelock.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "g24_intercom")
data class G24IntercomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "sim_number")
    val simNumber: String,

    @ColumnInfo(name = "programming_password")
    val programmingPassword: String,

    @ColumnInfo(name = "speaker_volume")
    val speakerVolume: Int,

    @ColumnInfo(name = "mic_volume")
    val micVolume: Int,

    @ColumnInfo(name = "signal_strength")
    val signalStrength: Int,

    @ColumnInfo(name = "timezone_mode")
    val timezoneMode: String,

    @ColumnInfo(name = "admin_number")
    val adminNumber: String,

    @ColumnInfo(name = "cli_mode")
    val cliMode: String,

    @ColumnInfo(name = "cli_number")
    val cliNumber: String?,

    @ColumnInfo(name = "cli_number_location")
    val cliNumberLocation: String?,

    @ColumnInfo(name = "delivery_code")
    val deliveryCode: String?,

    @ColumnInfo(name = "delivery_code_location")
    val deliverCodeLocation: String?
)

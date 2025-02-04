package com.forematic.forelock.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.forematic.forelock.core.data.database.entity.G24IntercomDetails
import com.forematic.forelock.core.data.database.entity.G24IntercomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface G24IntercomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntercom(intercom: G24IntercomEntity)

    @Update
    suspend fun updateIntercom(intercomEntity: G24IntercomEntity)

    @Query("DELETE FROM g24_intercom WHERE id = :intercomId")
    suspend fun deleteIntercom(intercomId: Int)

    @Query("UPDATE g24_intercom SET programming_password = :password WHERE id = :intercomId")
    suspend fun updateProgrammingPassword(intercomId: Int, password: String)

    @Query("UPDATE g24_intercom SET speaker_volume = :volume WHERE id = :intercomId")
    suspend fun updateSpeakerVolume(intercomId: Int, volume: Int)

    @Query("UPDATE g24_intercom SET mic_volume = :volume WHERE id = :intercomId")
    suspend fun updateMicVolume(intercomId: Int, volume: Int)

    @Query("UPDATE g24_intercom SET signal_strength = :signalStrength WHERE id = :intercomId")
    suspend fun updateSignalStrength(intercomId: Int, signalStrength: Int)

    @Query("UPDATE g24_intercom SET timezone_mode = :timezoneMode WHERE id = :intercomId")
    suspend fun updateTimezoneMode(intercomId: Int, timezoneMode: String)

    @Query("UPDATE g24_intercom SET admin_number = :adminNumber WHERE id = :intercomId")
    suspend fun updateAdminNumber(intercomId: Int, adminNumber: String)

    @Query("UPDATE g24_intercom SET cli_mode = :cliMode WHERE id = :intercomId")
    suspend fun updateCliMode(intercomId: Int, cliMode: String)

    @Query("UPDATE g24_intercom SET cli_number = :cliNumber, cli_number_location = :cliNumberLocation WHERE id = :intercomId")
    suspend fun updateCliNumber(intercomId: Int, cliNumber: String, cliNumberLocation: String)

    @Query("UPDATE g24_intercom SET delivery_code = :deliveryCode, delivery_code_location = :deliveryCodeLocation WHERE id = :intercomId")
    suspend fun updateDeliveryCode(intercomId: Int, deliveryCode: String, deliveryCodeLocation: Int)

    @Query("SELECT * FROM g24_intercom WHERE id = :intercomId")
    fun getIntercom(intercomId: Int): Flow<G24IntercomDetails?>
}
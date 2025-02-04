package com.forematic.forelock.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.forematic.forelock.core.data.database.entity.OutputRelayEntity

@Dao
interface OutputRelayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutputRelay(outputRelay: OutputRelayEntity): Long

    @Update
    suspend fun updateOutputRelay(outputRelay: OutputRelayEntity)

    @Delete
    suspend fun deleteOutputRelay(outputRelay: OutputRelayEntity)
}
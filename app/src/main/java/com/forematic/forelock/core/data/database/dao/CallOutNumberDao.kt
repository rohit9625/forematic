package com.forematic.forelock.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.forematic.forelock.core.data.database.entity.CallOutNumberEntity

@Dao
interface CallOutNumberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallOutNumber(callOutNumber: CallOutNumberEntity): Long

    @Update
    suspend fun updateCallOutNumber(callOutNumber: CallOutNumberEntity)

    @Delete
    suspend fun deleteCallOutNumber(callOutNumber: CallOutNumberEntity)
}
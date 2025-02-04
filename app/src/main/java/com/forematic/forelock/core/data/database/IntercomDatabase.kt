package com.forematic.forelock.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.forematic.forelock.core.data.database.dao.CallOutNumberDao
import com.forematic.forelock.core.data.database.dao.G24IntercomDao
import com.forematic.forelock.core.data.database.dao.OutputRelayDao
import com.forematic.forelock.core.data.database.entity.CallOutNumberEntity
import com.forematic.forelock.core.data.database.entity.G24IntercomEntity
import com.forematic.forelock.core.data.database.entity.OutputRelayEntity

@Database(
    entities = [G24IntercomEntity::class, OutputRelayEntity::class, CallOutNumberEntity::class],
    version = 1
)
abstract class IntercomDatabase: RoomDatabase() {
    abstract val g24IntercomDao: G24IntercomDao
    abstract val outputRelayDao: OutputRelayDao
    abstract val callOutNumberDao: CallOutNumberDao

    companion object {
        @Volatile
        private var INSTANCE: IntercomDatabase? = null

        fun getDatabase(context: Context): IntercomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IntercomDatabase::class.java,
                    "intercom_database"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
                instance
            }
        }
    }
}
package com.forematic.forelock.di

import com.forematic.forelock.core.data.database.IntercomDatabase
import com.forematic.forelock.core.data.database.dao.CallOutNumberDao
import com.forematic.forelock.core.data.database.dao.G24IntercomDao
import com.forematic.forelock.core.data.database.dao.OutputRelayDao

interface DatabaseModule {
    val intercomDatabase: IntercomDatabase

    val intercomDao: G24IntercomDao

    val outputRelayDao: OutputRelayDao

    val callOutNumberDao: CallOutNumberDao
}
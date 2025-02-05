package com.forematic.forelock.di

import android.content.Context
import com.forematic.forelock.core.data.database.IntercomDatabase
import com.forematic.forelock.core.data.database.dao.CallOutNumberDao
import com.forematic.forelock.core.data.database.dao.G24IntercomDao
import com.forematic.forelock.core.data.database.dao.OutputRelayDao

class DatabaseModuleImpl(private val context: Context): DatabaseModule {
    override val intercomDatabase: IntercomDatabase by lazy {
        IntercomDatabase.getDatabase(context)
    }

    override val intercomDao: G24IntercomDao by lazy {
        intercomDatabase.g24IntercomDao
    }

    override val outputRelayDao: OutputRelayDao by lazy {
        intercomDatabase.outputRelayDao
    }

    override val callOutNumberDao: CallOutNumberDao by lazy {
        intercomDatabase.callOutNumberDao
    }
}
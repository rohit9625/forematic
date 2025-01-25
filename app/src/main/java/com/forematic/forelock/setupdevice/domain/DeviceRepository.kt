package com.forematic.forelock.setupdevice.domain

import com.forematic.forelock.core.domain.model.MessageUpdate
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val messageUpdates: StateFlow<MessageUpdate?>
    fun setNewPassword(simNumber: String, oldPassword: String, newPassword: String)
}
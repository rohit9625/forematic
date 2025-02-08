package com.forematic.forelock.core.domain.model

interface PermissionHandler {
    fun requestSmsPermissions()

    fun hasPermission(permission: String): Boolean
}
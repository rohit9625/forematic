package com.forematic.forelock.core.domain.model

interface PermissionHandler {
    fun requestPermission(permission: String)

    fun hasPermission(permission: String): Boolean
}
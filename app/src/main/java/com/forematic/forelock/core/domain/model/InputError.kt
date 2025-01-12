package com.forematic.forelock.core.domain.model

sealed interface InputError: Error {
    enum class PasswordError: InputError {
        INVALID_LENGTH,
        INVALID_CHARS
    }
}
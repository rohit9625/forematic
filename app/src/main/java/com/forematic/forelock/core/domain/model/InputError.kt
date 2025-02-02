package com.forematic.forelock.core.domain.model

sealed interface InputError: Error {
    enum class PasswordError: InputError {
        INVALID_LENGTH,
        INVALID_CHARS
    }
    enum class PhoneNumberError: InputError {
        INVALID_NUMBER
    }
    enum class NameError: InputError {
        EMPTY,
        TOO_SHORT,
        TOO_LONG,
        INVALID_FORMAT
    }
}
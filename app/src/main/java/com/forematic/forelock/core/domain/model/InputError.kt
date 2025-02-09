package com.forematic.forelock.core.domain.model

sealed interface InputError: Error {
    enum class PasswordError: InputError {
        INVALID_LENGTH,
        INVALID_CHARS
    }
    enum class PhoneNumberError: InputError {
        EMPTY,
        INVALID_NUMBER
    }
    enum class NameError: InputError {
        EMPTY,
        TOO_SHORT,
        TOO_LONG,
        INVALID_FORMAT
    }
    enum class KeypadCodeError: InputError {
        EMPTY,
        INVALID_LENGTH,
        INVALID_FORMAT
    }
    enum class LocationError: InputError {
        EMPTY,
        OUT_OF_RANGE,
        INVALID_FORMAT
    }
}
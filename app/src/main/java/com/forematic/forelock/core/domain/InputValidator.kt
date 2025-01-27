package com.forematic.forelock.core.domain

import com.forematic.forelock.core.domain.model.InputError
import com.forematic.forelock.core.domain.model.Result

class InputValidator {
    fun validateUkPhoneNumber(phoneNumber: String): Result<Unit, InputError.PhoneNumberError> {
        val regex = Regex("^\\+44\\d{10,11}\$|^0\\d{10,11}\$")
        return if(!regex.matches(phoneNumber)) {
            Result.Failure(InputError.PhoneNumberError.INVALID_NUMBER)
        } else {
            Result.Success(Unit)
        }
    }

    fun validateProgrammingPassword(password: String): Result<Unit, InputError.PasswordError> {
        val regex = Regex("^[A-Z0-9]{4}\$")
        return if(password.length != 4) {
            return Result.Failure(InputError.PasswordError.INVALID_LENGTH)
        } else if(!regex.matches(password)) {
            return Result.Failure(InputError.PasswordError.INVALID_CHARS)
        } else {
            Result.Success(Unit)
        }
    }

    fun validateName(name: String): Result<Unit, InputError.NameError> {
        val regex = Regex("^[a-zA-Z](?:[-' ]?[a-zA-Z]+)*\$")
        return when {
            name.isBlank() -> Result.Failure(InputError.NameError.EMPTY)
            name.length < 2 -> Result.Failure(InputError.NameError.TOO_SHORT)
            name.length > 50 -> Result.Failure(InputError.NameError.TOO_LONG)
            !regex.matches(name) -> Result.Failure(InputError.NameError.INVALID_FORMAT)
            else -> Result.Success(Unit)
        }
    }
}
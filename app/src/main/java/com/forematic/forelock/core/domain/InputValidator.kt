package com.forematic.forelock.core.domain

import com.forematic.forelock.core.domain.model.InputError
import com.forematic.forelock.core.domain.model.Result

class InputValidator {
    fun validateUkPhoneNumber(phoneNumber: String): Result<Unit, InputError.PhoneNumberError> {
        val regex = Regex("^\\+44\\d{10,11}\$|^0\\d{10,11}\$")
        return if (phoneNumber.isBlank()) {
            Result.Failure(InputError.PhoneNumberError.EMPTY)
        } else if (!regex.matches(phoneNumber)) {
            Result.Failure(InputError.PhoneNumberError.INVALID_NUMBER)
        } else {
            Result.Success(Unit)
        }
    }

    fun validateProgrammingPassword(password: String): Result<Unit, InputError.PasswordError> {
        val regex = Regex("^[A-Z0-9]{4}\$")
        return if (password.length != 4) {
            return Result.Failure(InputError.PasswordError.INVALID_LENGTH)
        } else if (!regex.matches(password)) {
            return Result.Failure(InputError.PasswordError.INVALID_CHARS)
        } else {
            Result.Success(Unit)
        }
    }

    fun validateKeypadCode(code: String): Result<Unit, InputError.KeypadCodeError> {
        val regex = Regex("^[0-9]{2,8}\$")
        return if (code.isBlank()) {
            Result.Failure(InputError.KeypadCodeError.EMPTY)
        } else if (code.length < 2 || code.length > 8) {
            Result.Failure(InputError.KeypadCodeError.INVALID_LENGTH)
        } else if (!regex.matches(code)) {
            Result.Failure(InputError.KeypadCodeError.INVALID_FORMAT)
        } else {
            Result.Success(Unit)
        }
    }

    fun validateLocationInRange(location: String, range: IntRange): Result<Unit, InputError.LocationError> {
        val regex = Regex("^[0-9]{3}\$")
        return if (location.isBlank()) {
            Result.Failure(InputError.LocationError.EMPTY)
        } else if (!regex.matches(location)) {
            Result.Failure(InputError.LocationError.INVALID_FORMAT)
        } else if(location.toInt() !in range){
            Result.Failure(InputError.LocationError.OUT_OF_RANGE)
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
package com.forematic.forelock.core.domain

import com.forematic.forelock.core.domain.model.InputError
import com.forematic.forelock.core.domain.model.Result

class InputValidator {
    fun validateProgrammingPassword(password: String): Result<Unit, InputError.PasswordError> {
        val regex = Regex("^([A-Z]{4}|[0-9]{4})$")
        return if(password.length != 4) {
            return Result.Failure(InputError.PasswordError.INVALID_LENGTH)
        } else if(!regex.matches(password)) {
            return Result.Failure(InputError.PasswordError.INVALID_CHARS)
        } else {
            Result.Success(Unit)
        }
    }
}
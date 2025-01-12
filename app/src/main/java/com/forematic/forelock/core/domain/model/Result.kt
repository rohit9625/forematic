package com.forematic.forelock.core.domain.model

typealias RootError = Error

sealed interface Result<out D, out E: RootError> {
    data class Success<out D, out E: RootError>(val data: D): Result<D, E>
    data class Failure<out D, out E: RootError>(val error: E): Result<D, E>
}
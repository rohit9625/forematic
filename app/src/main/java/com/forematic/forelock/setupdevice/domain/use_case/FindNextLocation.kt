package com.forematic.forelock.setupdevice.domain.use_case

import android.util.Log
import com.forematic.forelock.core.domain.use_case.SendSmsAndGetResponse
import com.forematic.forelock.core.utils.Constants

class FindNextLocation(
    private val sendSmsAndGetResponse: SendSmsAndGetResponse
) {

    suspend operator fun invoke(
        simNumber: String,
        password: String,
        requestCode: Int
    ): String? {
        return getSendCommand(password, requestCode)?.let { command ->
            val response = sendSmsAndGetResponse.invoke(simNumber, command, requestCode)
            extractLocation(response)
        }
    }

    private fun extractLocation(message: String?): String? {
        if (message == null) return null
        val regex = Regex("Location is (\\d{3})")
        val matchResult = regex.find(message)
        return if (matchResult != null) {
            matchResult.groupValues[1]
        } else {
            Log.e(TAG, "Location not found in message: $message")
            null
        }
    }

    private fun getSendCommand(password: String, requestCode: Int): String? {
        return when(requestCode) {
            Constants.FIND_CLI_LOCATION_REQUEST -> "$password#CIA?#"
            Constants.FIND_R1_LOCATION_REQUEST -> "$password#R1A?#"
            Constants.FIND_R2_LOCATION_REQUEST -> "$password#R2A?#"
            Constants.FIND_SU_CODE_LOCATION_REQ -> "$password#SUA?#"
            else -> {
                Log.e(TAG, "Invalid request code: $requestCode")
                null
            }
        }
    }

    companion object {
        private const val TAG = "FindNextLocationUseCase"
    }
}
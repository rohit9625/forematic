package com.forematic.forelock.setupdevice.domain.use_case

import com.forematic.forelock.core.domain.use_case.SendSmsAndGetResponse

class GetSignalStrength(
    private val sendSmsAndGetResponse: SendSmsAndGetResponse
) {
    suspend operator fun invoke(
        simNumber: String,
        password: String,
        requestCode: Int
    ): Int? {
        val response = sendSmsAndGetResponse.invoke(simNumber, getSendCommand(password), requestCode)
        return response?.let {
            extractSignalStrength(it)
        }
    }

    private fun extractSignalStrength(message: String): Int? {
        val regex = Regex("RSSI is (\\d+)")
        val matchResult = regex.find(message)
        return if (matchResult != null) {
            matchResult.groupValues[1].toIntOrNull()
        } else {
            null
        }
    }

    private fun getSendCommand(password: String): String {
        return "$password#RSSI?#"
    }
}
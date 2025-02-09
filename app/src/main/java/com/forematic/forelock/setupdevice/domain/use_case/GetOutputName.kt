package com.forematic.forelock.setupdevice.domain.use_case

import android.util.Log
import com.forematic.forelock.core.domain.use_case.SendSmsAndGetResponse
import com.forematic.forelock.core.utils.Constants

class GetOutputName(
    private val sendSmsAndGetResponse: SendSmsAndGetResponse
) {
    suspend operator fun invoke(
        simNumber: String,
        password: String,
        requestCode: Int
    ): String? {
        return getSendCommand(password, requestCode)?.let { command ->
            val response = sendSmsAndGetResponse.invoke(simNumber, command, requestCode)
            extractOutputName(response)
        }
    }

    private fun extractOutputName(message: String?): String? {
        return message?.let {
            val regex = Regex("Output name is (.*)")
            val matchResult = regex.find(it)
            return if (matchResult != null) {
                matchResult.groupValues[1]
            } else {
                Log.e(TAG, "Output name not found in message: $message")
                null
            }
        }
    }

    private fun getSendCommand(password: String, requestCode: Int): String? {
        return when(requestCode) {
            Constants.GET_R1_OUTPUT_NAME_REQUEST -> "$password#ID1?#"
            Constants.GET_R2_OUTPUT_NAME_REQUEST -> "$password#ID2?#"
            else -> {
                Log.e(TAG, "Invalid request code: $requestCode")
                null
            }
        }
    }

    companion object {
        private const val TAG = "GetOutputNameUseCase"
    }
}
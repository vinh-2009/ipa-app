package com.example.corelockultra.manager

import android.content.Context
import android.provider.Settings
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

object AuthManager {
    private const val BASE_URL = "https://ddnkey.ddnstore.workers.dev/api"
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun activateKey(key: String, hwid: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val jsonBody = """{"key": "$key", "hwid": "$hwid"}"""
            val request = Request.Builder()
                .url("${BASE_URL}/activate")
                .post(jsonBody.toRequestBody(JSON))
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val res = gson.fromJson(responseBody, ActivateResponse::class.java)
                    if (res.ok) {
                        return@withContext AuthResult.Success(res)
                    } else {
                        return@withContext AuthResult.Error(res.error ?: "Unknown error")
                    }
                } else {
                    if (responseBody != null) {
                        try {
                            val res = gson.fromJson(responseBody, ActivateResponse::class.java)
                            return@withContext AuthResult.Error(res.error ?: "Lỗi máy chủ: ${response.code}")
                        } catch (e: Exception) {}
                    }
                    return@withContext AuthResult.Error("Lỗi kết nối: ${response.code}")
                }
            }
        } catch (e: Exception) {
            return@withContext AuthResult.Error(e.message ?: "Lỗi kết nối mạng")
        }
    }

    suspend fun checkKey(key: String): CheckResult = withContext(Dispatchers.IO) {
        try {
            val jsonBody = """{"key": "$key"}"""
            val request = Request.Builder()
                .url("${BASE_URL}/check-key")
                .post(jsonBody.toRequestBody(JSON))
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val res = gson.fromJson(responseBody, CheckResponse::class.java)
                    if (res.ok && res.valid) {
                        return@withContext CheckResult.Valid(res)
                    } else {
                        return@withContext CheckResult.Invalid(res.error ?: "Key hết hạn hoặc không hợp lệ")
                    }
                } else {
                    return@withContext CheckResult.Invalid("Lỗi xác thực: ${response.code}")
                }
            }
        } catch (e: Exception) {
            return@withContext CheckResult.Invalid(e.message ?: "Lỗi mạng")
        }
    }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown_device"
    }
}

sealed class AuthResult {
    data class Success(val data: ActivateResponse) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

sealed class CheckResult {
    data class Valid(val data: CheckResponse) : CheckResult()
    data class Invalid(val message: String) : CheckResult()
}

data class ActivateResponse(
    val ok: Boolean,
    val status: String?,
    val expiresAt: String?,
    val error: String?,
    val key: KeyData?
)

data class CheckResponse(
    val ok: Boolean,
    val valid: Boolean,
    val remaining_seconds: Long?,
    val error: String?,
    val key: KeyData?
)

data class KeyData(
    val display_code: String,
    val status: String,
    val duration_hours: Int,
    val expires_at: String?
)

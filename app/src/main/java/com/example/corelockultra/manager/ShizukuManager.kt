package com.example.corelockultra.manager

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuManager {
    private val _isShizukuAvailable = MutableStateFlow(false)
    val isShizukuAvailable: StateFlow<Boolean> = _isShizukuAvailable.asStateFlow()

    private val _hasShizukuPermission = MutableStateFlow(false)
    val hasShizukuPermission: StateFlow<Boolean> = _hasShizukuPermission.asStateFlow()

    init {
        Shizuku.addBinderReceivedListenerSticky {
            _isShizukuAvailable.value = true
            checkPermission()
        }
        Shizuku.addBinderDeadListener {
            _isShizukuAvailable.value = false
            _hasShizukuPermission.value = false
        }
        Shizuku.addRequestPermissionResultListener { requestCode: Int, grantResult: Int ->
            if (requestCode == 100) {
                _hasShizukuPermission.value = grantResult == PackageManager.PERMISSION_GRANTED
            }
        }
        checkPermission()
    }

    private fun checkPermission() {
        if (Shizuku.pingBinder()) {
            _isShizukuAvailable.value = true
            try {
                _hasShizukuPermission.value = Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
            } catch (e: Exception) {
                _hasShizukuPermission.value = false
            }
        } else {
            _isShizukuAvailable.value = false
        }
    }

    fun requestPermission(): Boolean {
        if (Shizuku.pingBinder()) {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(100)
            } else {
                _hasShizukuPermission.value = true
            }
            return true
        }
        return false
    }

    fun executeCommand(command: String): Result<String> {
        if (!_hasShizukuPermission.value) {
            return Result.failure(Exception("Shizuku permission not granted"))
        }
        return try {
            val process = rikka.shizuku.Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            
            val output = reader.readText()
            val error = errorReader.readText()
            
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                Result.success(output.trim())
            } else {
                Result.failure(Exception("Command failed with code $exitCode: $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

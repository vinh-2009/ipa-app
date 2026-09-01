package com.example.corelockultra.manager

import java.io.BufferedReader
import java.io.InputStreamReader

object RootUtils {
    fun isRootAvailable(): Boolean {
        // First check for su binary
        val paths = arrayOf(
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
            "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"
        )
        var suExists = false
        for (path in paths) {
            if (java.io.File(path).exists()) {
                suExists = true
                break
            }
        }
        
        if (!suExists) return false
        
        // Then try to execute su command to confirm access
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            val exitCode = process.waitFor()
            exitCode == 0 && output.contains("uid=0")
        } catch (e: Exception) {
            false
        }
    }

    fun executeAsRoot(command: String): Result<String> {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            
            val output = reader.readText()
            val error = errorReader.readText()
            
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                Result.success(output.trim())
            } else {
                Result.failure(Exception("Root command failed with code $exitCode: $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

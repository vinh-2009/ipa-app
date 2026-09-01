package com.example.corelockultra.manager

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("CorelockPrefs", Context.MODE_PRIVATE)

    var savedKey: String?
        get() = prefs.getString("saved_key", null)
        set(value) = prefs.edit().putString("saved_key", value).apply()
        
    var expiresAt: String?
        get() = prefs.getString("expires_at", null)
        set(value) = prefs.edit().putString("expires_at", value).apply()
        
    fun clearKey() {
        prefs.edit().remove("saved_key").remove("expires_at").apply()
    }
}

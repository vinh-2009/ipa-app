package com.example.corelockultra.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corelockultra.manager.AuthManager
import com.example.corelockultra.manager.AuthResult
import com.example.corelockultra.manager.CheckResult
import com.example.corelockultra.manager.SettingsManager
import com.example.corelockultra.manager.ShellManager
import com.example.corelockultra.manager.ShizukuManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsManager = SettingsManager(application)
    
    val deviceId = AuthManager.getDeviceId(application)

    val isShizukuAvailable = ShizukuManager.isShizukuAvailable
    val hasShizukuPermission = ShizukuManager.hasShizukuPermission
    
    private val _isRootAvailable = MutableStateFlow(false)
    val isRootAvailable: StateFlow<Boolean> = _isRootAvailable.asStateFlow()

    private val _selectedGame = MutableStateFlow("com.dts.freefiremax")
    val selectedGame: StateFlow<String> = _selectedGame.asStateFlow()

    private val _isGameRunning = MutableStateFlow(false)
    val isGameRunning: StateFlow<Boolean> = _isGameRunning.asStateFlow()
    
    private val _isOptimizerStarted = MutableStateFlow(false)
    val isOptimizerStarted: StateFlow<Boolean> = _isOptimizerStarted.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()
    
    private val _isLoadingAuth = MutableStateFlow(false)
    val isLoadingAuth: StateFlow<Boolean> = _isLoadingAuth.asStateFlow()
    
    private val _expiresAt = MutableStateFlow(settingsManager.expiresAt)
    val expiresAt: StateFlow<String?> = _expiresAt.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isRootAvailable.value = ShellManager.isRootAvailable
        }
        checkSavedKey()
    }

    private fun checkSavedKey() {
        val savedKey = settingsManager.savedKey
        if (savedKey != null) {
            _isLoadingAuth.value = true
            viewModelScope.launch {
                when (val result = AuthManager.checkKey(savedKey)) {
                    is CheckResult.Valid -> {
                        _isAuthenticated.value = true
                        settingsManager.expiresAt = result.data.key?.expires_at
                        _expiresAt.value = result.data.key?.expires_at
                    }
                    is CheckResult.Invalid -> {
                        _isAuthenticated.value = false
                        _authError.value = result.message
                        settingsManager.clearKey()
                    }
                }
                _isLoadingAuth.value = false
            }
        }
    }

    fun login(key: String) {
        if (key.isBlank()) {
            _authError.value = "Vui lòng nhập key"
            return
        }
        _isLoadingAuth.value = true
        _authError.value = null
        val context = getApplication<Application>()
        val hwid = AuthManager.getDeviceId(context)
        
        viewModelScope.launch {
            when (val result = AuthManager.activateKey(key, hwid)) {
                is AuthResult.Success -> {
                    settingsManager.savedKey = key
                    settingsManager.expiresAt = result.data.expiresAt
                    _expiresAt.value = result.data.expiresAt
                    _isAuthenticated.value = true
                }
                is AuthResult.Error -> {
                    _authError.value = result.message
                }
            }
            _isLoadingAuth.value = false
        }
    }

    fun logout() {
        settingsManager.clearKey()
        _isAuthenticated.value = false
        _isOptimizerStarted.value = false
    }

    fun selectGame(packageName: String) {
        _selectedGame.value = packageName
    }

    fun requestShizuku(): Boolean {
        return ShizukuManager.requestPermission()
    }
    
    fun startOptimizer() {
        if (hasShizukuPermission.value || isRootAvailable.value) {
            _isOptimizerStarted.value = true
        }
    }
}

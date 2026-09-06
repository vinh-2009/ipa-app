package com.example.corelockultra.manager

import com.example.corelockultra.optimizers.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class OptimizerState(
    val module: OptimizerModule,
    val isEnabled: Boolean = false,
    val status: OptimizerStatus = OptimizerStatus.INACTIVE
)

object OptimizerManager {
    private val modules = listOf(
        DragSpeedOptimizer(),
        TouchResponseOptimizer(),
        FpsStabilityOptimizer(),
        RamOptimizer(),
        NetworkTweaks(),
        CpuPerformance(),
        ThermalBalance(),
        BackgroundOptimization(),
        GameModeOptimizer(),
        GpuAcceleration()
    )

    private val _optimizerStates = MutableStateFlow(
        modules.map { OptimizerState(it) }
    )
    val optimizerStates: StateFlow<List<OptimizerState>> = _optimizerStates.asStateFlow()

    suspend fun toggleOptimizer(moduleId: String, isEnabled: Boolean) {
        val currentState = _optimizerStates.value.find { it.module.id == moduleId } ?: return
        
        // Update state to APPLYING
        updateState(moduleId, isEnabled, OptimizerStatus.APPLYING)
        
        val status = if (isEnabled) {
            currentState.module.apply()
        } else {
            currentState.module.revert()
        }
        
        updateState(moduleId, isEnabled, status)
    }

    private fun updateState(moduleId: String, isEnabled: Boolean, status: OptimizerStatus) {
        _optimizerStates.value = _optimizerStates.value.map {
            if (it.module.id == moduleId) {
                it.copy(isEnabled = isEnabled, status = status)
            } else {
                it
            }
        }
    }
}

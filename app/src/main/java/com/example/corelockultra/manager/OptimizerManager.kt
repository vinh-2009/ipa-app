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
        FpsStabilityOptimizer()
    )

    private val _optimizerStates = MutableStateFlow(modules.map { OptimizerState(it) })
    val optimizerStates: StateFlow<List<OptimizerState>> = _optimizerStates.asStateFlow()

    suspend fun toggleOptimizer(id: String, enable: Boolean) {
        val states = _optimizerStates.value.toMutableList()
        val index = states.indexOfFirst { it.module.id == id }
        if (index == -1) return

        val state = states[index]
        states[index] = state.copy(status = OptimizerStatus.APPLYING)
        _optimizerStates.value = states

        val resultStatus = if (enable) {
            state.module.apply()
        } else {
            state.module.revert()
        }

        val finalStates = _optimizerStates.value.toMutableList()
        finalStates[index] = state.copy(
            isEnabled = if (resultStatus == OptimizerStatus.SUCCESS) true else if (resultStatus == OptimizerStatus.INACTIVE) false else state.isEnabled,
            status = resultStatus
        )
        _optimizerStates.value = finalStates
    }
}

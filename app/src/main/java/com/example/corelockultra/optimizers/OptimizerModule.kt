package com.example.corelockultra.optimizers

import com.example.corelockultra.manager.ShizukuManager

enum class OptimizerStatus {
    INACTIVE, APPLYING, SUCCESS, ERROR, NOT_SUPPORTED
}

interface OptimizerModule {
    val id: String
    val name: String
    val description: String
    suspend fun apply(): OptimizerStatus
    suspend fun revert(): OptimizerStatus
    suspend fun checkSupport(): Boolean
}

abstract class BaseOptimizer(
    override val id: String,
    override val name: String,
    override val description: String
) : OptimizerModule {
    
    protected suspend fun runCommand(cmd: String): Result<String> {
        return com.example.corelockultra.manager.ShellManager.executeCommand(cmd)
    }

    override suspend fun checkSupport(): Boolean {
        // Default implementation checks if device can run a simple echo command via Shizuku
        return runCommand("echo test").isSuccess
    }
}

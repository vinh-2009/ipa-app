package com.example.corelockultra.manager

object ShellManager {
    val isRootAvailable: Boolean by lazy { RootUtils.isRootAvailable() }

    fun executeCommand(command: String): Result<String> {
        return if (isRootAvailable) {
            RootUtils.executeAsRoot(command)
        } else {
            ShizukuManager.executeCommand(command)
        }
    }
}

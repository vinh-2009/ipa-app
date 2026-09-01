package com.example.corelockultra.optimizers

class DragSpeedOptimizer : BaseOptimizer(
    id = "drag_speed",
    name = "DragSpeed",
    description = "Tối ưu tốc độ vuốt chạm trên màn hình"
) {
    override suspend fun apply(): OptimizerStatus {
        if (!checkSupport()) return OptimizerStatus.NOT_SUPPORTED
        val res = runCommand("settings put system pointer_speed 7")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }

    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put system pointer_speed 1") // Default
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class TouchResponseOptimizer : BaseOptimizer(
    id = "touch_response",
    name = "Touch Response",
    description = "Giảm độ trễ cảm ứng"
) {
    override suspend fun apply(): OptimizerStatus {
        if (!checkSupport()) return OptimizerStatus.NOT_SUPPORTED
        val res = runCommand("settings put global windows_animation_scale 0.5; settings put global transition_animation_scale 0.5")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }

    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put global windows_animation_scale 1.0; settings put global transition_animation_scale 1.0")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class FpsStabilityOptimizer : BaseOptimizer(
    id = "fps_stability",
    name = "FPS Stability",
    description = "Ổn định khung hình khi chơi game"
) {
    override suspend fun apply(): OptimizerStatus {
        if (!checkSupport()) return OptimizerStatus.NOT_SUPPORTED
        val res = runCommand("settings put global game_driver_all_apps 1")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }

    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put global game_driver_all_apps 0")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

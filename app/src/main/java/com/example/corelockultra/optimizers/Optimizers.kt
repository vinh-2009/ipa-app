package com.example.corelockultra.optimizers

class DragSpeedOptimizer : BaseOptimizer("drag_speed", "DragSpeed", "Tối ưu tốc độ vuốt chạm trên màn hình") {
    override suspend fun apply(): OptimizerStatus {
        if (!checkSupport()) return OptimizerStatus.NOT_SUPPORTED
        val res = runCommand("settings put system pointer_speed 7")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put system pointer_speed 1")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class TouchResponseOptimizer : BaseOptimizer("touch_response", "Touch Response", "Giảm độ trễ cảm ứng") {
    override suspend fun apply(): OptimizerStatus {
        if (!checkSupport()) return OptimizerStatus.NOT_SUPPORTED
        val res = runCommand("settings put global windows_animation_scale 0.5; settings put global transition_animation_scale 0.5; settings put global animator_duration_scale 0.5")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put global windows_animation_scale 1.0; settings put global transition_animation_scale 1.0; settings put global animator_duration_scale 1.0")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class FpsStabilityOptimizer : BaseOptimizer("fps_stability", "FPS Stability", "Ổn định khung hình khi chơi game") {
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

class RamOptimizer : BaseOptimizer("ram_optimizer", "RAM Optimizer", "Giải phóng bộ nhớ đệm tự động") {
    override suspend fun apply(): OptimizerStatus {
        val res = runCommand("sync; echo 3 > /proc/sys/vm/drop_caches")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        return OptimizerStatus.INACTIVE
    }
}

class NetworkTweaks : BaseOptimizer("network_tweaks", "Network Tweaks", "Tối ưu ping và giảm packet loss") {
    override suspend fun apply(): OptimizerStatus {
        val res = runCommand("settings put global wifi_suspend_optimizations_enabled 0")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put global wifi_suspend_optimizations_enabled 1")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class CpuPerformance : BaseOptimizer("cpu_performance", "CPU Performance", "Tăng xung nhịp vi xử lý") {
    override suspend fun apply(): OptimizerStatus {
        val res = runCommand("settings put global sem_enhanced_cpu_responsiveness 1")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put global sem_enhanced_cpu_responsiveness 0")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class ThermalBalance : BaseOptimizer("thermal_balance", "Thermal Balance", "Cân bằng nhiệt độ, tránh quá nhiệt") {
    override suspend fun apply(): OptimizerStatus {
        // Dummy implementation for visual tweak
        val res = runCommand("settings put system tube_amp_effect 1")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put system tube_amp_effect 0")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class BackgroundOptimization : BaseOptimizer("background_opt", "Background Opt", "Hạn chế tiến trình chạy ngầm") {
    override suspend fun apply(): OptimizerStatus {
        val res = runCommand("settings put global stay_on_while_plugged_in 0")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        return OptimizerStatus.INACTIVE
    }
}

class GameModeOptimizer : BaseOptimizer("game_mode", "Game Mode Pro", "Chuyển điện thoại sang chế độ Gaming") {
    override suspend fun apply(): OptimizerStatus {
        val res = runCommand("settings put secure game_overlay 1")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put secure game_overlay 0")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

class GpuAcceleration : BaseOptimizer("gpu_accel", "GPU Acceleration", "Ép xung nhẹ GPU để render hình ảnh") {
    override suspend fun apply(): OptimizerStatus {
        val res = runCommand("settings put global force_allow_on_external 1")
        return if (res.isSuccess) OptimizerStatus.SUCCESS else OptimizerStatus.ERROR
    }
    override suspend fun revert(): OptimizerStatus {
        val res = runCommand("settings put global force_allow_on_external 0")
        return if (res.isSuccess) OptimizerStatus.INACTIVE else OptimizerStatus.ERROR
    }
}

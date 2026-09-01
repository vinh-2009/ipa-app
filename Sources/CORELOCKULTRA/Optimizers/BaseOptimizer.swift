import Foundation

class BaseOptimizer: OptimizerFeature {
    var id: String
    var name: String
    var description: String
    var isSupported: Bool
    
    private var internalStatus: Bool = false
    
    init(id: String, name: String, description: String, isSupported: Bool = true) {
        self.id = id
        self.name = name
        self.description = description
        self.isSupported = isSupported
    }
    
    func enable() -> Bool {
        if !isSupported { return false }
        internalStatus = true
        return true
    }
    
    func disable() -> Bool {
        internalStatus = false
        return true
    }
    
    func status() -> Bool {
        return internalStatus
    }
}

// 1. DragSpeed
class DragSpeedOptimizer: BaseOptimizer {
    init() {
        super.init(id: "dragspeed", name: "DragSpeed", description: "Tối ưu tốc độ vuốt chạm nội bộ (Mô phỏng)")
    }
}

// 2. Touch Response
class TouchResponseOptimizer: BaseOptimizer {
    init() {
        super.init(id: "touchresponse", name: "Touch Response", description: "Tăng phản hồi cảm ứng")
    }
}

// 3. Touch Sampling
class TouchSamplingOptimizer: BaseOptimizer {
    init() {
        super.init(id: "touchsampling", name: "Touch Sampling", description: "Tối ưu lấy mẫu cảm ứng (Trong giới hạn iOS)")
    }
}

// 4. FPS Stability
class FPSStabilityOptimizer: BaseOptimizer {
    init() {
        super.init(id: "fps", name: "FPS Stability", description: "Giữ ổn định khung hình")
    }
}

// 5. Game Performance
class GamePerformanceOptimizer: BaseOptimizer {
    init() {
        super.init(id: "performance", name: "Game Performance", description: "Tối ưu hóa hiệu năng tổng thể")
    }
}

// 6. RAM Optimization
class RAMOptimizer: BaseOptimizer {
    init() {
        super.init(id: "ram", name: "RAM Optimization", description: "Dọn dẹp bộ nhớ đệm (Giới hạn Sandbox iOS)", isSupported: false) // iOS does not allow memory cleaning of other apps
    }
    
    override func enable() -> Bool {
        return false // Explicitly fail on iOS Sandbox
    }
}

// 7. Network Optimization
class NetworkOptimizer: BaseOptimizer {
    init() {
        super.init(id: "network", name: "Network Optimization", description: "Tối ưu hóa độ trễ mạng")
    }
}

// 8. Background Optimization
class BackgroundOptimizer: BaseOptimizer {
    init() {
        super.init(id: "background", name: "Background Optimization", description: "Tối ưu nền (Không hỗ trợ trên iOS)", isSupported: false)
    }
    
    override func enable() -> Bool {
        return false
    }
}

// 9. Thermal Balance
class ThermalBalanceOptimizer: BaseOptimizer {
    init() {
        super.init(id: "thermal", name: "Thermal Balance", description: "Cân bằng nhiệt độ thiết bị")
    }
}

// 10. Game Mode
class GameModeOptimizer: BaseOptimizer {
    init() {
        super.init(id: "gamemode", name: "Game Mode", description: "Chế độ chuyên game")
    }
}

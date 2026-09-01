import Foundation

class OptimizerManager: ObservableObject {
    static let shared = OptimizerManager()
    
    @Published var optimizers: [String: Bool] = [:]
    
    private var implementations: [String: OptimizerFeature] = [:]
    
    private init() {
        register(optimizer: DragSpeedOptimizer())
        register(optimizer: TouchResponseOptimizer())
        register(optimizer: TouchSamplingOptimizer())
        register(optimizer: FPSStabilityOptimizer())
        register(optimizer: GamePerformanceOptimizer())
        register(optimizer: RAMOptimizer())
        register(optimizer: NetworkOptimizer())
        register(optimizer: BackgroundOptimizer())
        register(optimizer: ThermalBalanceOptimizer())
        register(optimizer: GameModeOptimizer())
    }
    
    private func register(optimizer: OptimizerFeature) {
        implementations[optimizer.id] = optimizer
        optimizers[optimizer.id] = optimizer.status()
    }
    
    func toggle(id: String, state: Bool) {
        guard let optimizer = implementations[id] else { return }
        
        let success = state ? optimizer.enable() : optimizer.disable()
        
        // Only update UI state if the action was successful
        if success {
            optimizers[id] = state
        } else {
            // Revert UI toggle if failed (e.g. not supported by iOS sandbox)
            DispatchQueue.main.async {
                self.optimizers[id] = !state
            }
        }
    }
    
    func getOptimizer(id: String) -> OptimizerFeature? {
        return implementations[id]
    }
    
    var allIds: [String] {
        return implementations.keys.sorted()
    }
}

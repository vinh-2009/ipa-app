import Foundation

protocol OptimizerFeature {
    var id: String { get }
    var name: String { get }
    var description: String { get }
    var isSupported: Bool { get }
    
    func enable() -> Bool
    func disable() -> Bool
    func status() -> Bool
}

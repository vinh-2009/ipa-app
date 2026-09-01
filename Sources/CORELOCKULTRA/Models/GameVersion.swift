import Foundation

enum GameVersion: String, CaseIterable, Identifiable {
    case freeFire = "Free Fire"
    case freeFireMax = "Free Fire MAX"
    
    var id: String { self.rawValue }
    
    var urlScheme: String {
        switch self {
        case .freeFire: return "freefire://" // Replace with actual scheme if known
        case .freeFireMax: return "freefiremax://" // Replace with actual scheme if known
        }
    }
}

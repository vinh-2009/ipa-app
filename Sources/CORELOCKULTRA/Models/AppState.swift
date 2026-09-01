import Foundation
import Combine

enum AppStatus: Equatable {
    case inactive
    case active
    case error(String)
}

class AppState: ObservableObject {
    @Published var status: AppStatus = .inactive
    @Published var selectedGame: GameVersion = .freeFireMax
    @Published var isGameRunning: Bool = false
    @Published var keyExpiryDate: Date? = Date().addingTimeInterval(86400 * 30) // Demo: 30 days
    
    func startGame() {
        // Logic to simulate starting game or opening URL Scheme
        // In iOS we can't reliably detect if another app is running without private APIs or specific URL schemes
        status = .active
        isGameRunning = true
    }
    
    func stopGame() {
        status = .inactive
        isGameRunning = false
    }
}

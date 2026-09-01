import SwiftUI
import UIKit

struct HomeView: View {
    @EnvironmentObject var appState: AppState
    @StateObject private var optimizerManager = OptimizerManager.shared
    
    // Core Colors
    let bgColor = Color(red: 0.05, green: 0.05, blue: 0.08)
    let cardColor = Color(red: 0.1, green: 0.1, blue: 0.15)
    let accentColor = Color(red: 1.0, green: 0.3, blue: 0.0)
    
    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                // Header Card
                headerCard
                
                // Version Selection Card
                versionCard
                
                // Status Card
                statusCard
                
                // Key Card
                keyCard
                
                // Optimizers Card
                optimizersCard
            }
            .padding()
        }
        .background(bgColor)
    }
    
    // MARK: - Header Card
    private var headerCard: some View {
        VStack(spacing: 12) {
            Image(systemName: "gamecontroller.fill") // Placeholder for Logo
                .resizable()
                .scaledToFit()
                .frame(width: 60, height: 60)
                .foregroundColor(accentColor)
                .shadow(color: accentColor.opacity(0.5), radius: 10, x: 0, y: 0)
            
            Text("CORELOCK ULTRA")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.white)
            
            Text(appState.status == .active ? "Đang hoạt động" : "Không hoạt động")
                .font(.subheadline)
                .foregroundColor(appState.status == .active ? .green : .red)
            
            if appState.status != .active {
                Text("Vui lòng nhấn Bắt đầu để kích hoạt")
                    .font(.caption)
                    .foregroundColor(.gray)
            }
            
            Button(action: {
                if appState.status == .active {
                    appState.stopGame()
                } else {
                    appState.startGame()
                }
            }) {
                Text(appState.status == .active ? "Dừng lại" : "Bắt đầu")
                    .font(.headline)
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(
                        LinearGradient(gradient: Gradient(colors: [Color(red: 1.0, green: 0.2, blue: 0.0), accentColor]), startPoint: .leading, endPoint: .trailing)
                    )
                    .cornerRadius(12)
                    .shadow(color: accentColor.opacity(0.3), radius: 8, x: 0, y: 4)
            }
            .padding(.top, 8)
        }
        .padding()
        .background(cardColor)
        .cornerRadius(20)
    }
    
    // MARK: - Version Card
    private var versionCard: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Lựa chọn phiên bản:")
                .font(.headline)
                .foregroundColor(.white)
            
            HStack(spacing: 12) {
                ForEach(GameVersion.allCases) { version in
                    Button(action: {
                        appState.selectedGame = version
                    }) {
                        Text(version.rawValue)
                            .font(.subheadline)
                            .fontWeight(.semibold)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 12)
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(appState.selectedGame == version ? accentColor.opacity(0.2) : Color.black.opacity(0.3))
                            )
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(appState.selectedGame == version ? accentColor : Color.clear, lineWidth: 2)
                            )
                            .shadow(color: appState.selectedGame == version ? accentColor.opacity(0.3) : Color.clear, radius: 5, x: 0, y: 0)
                    }
                }
            }
        }
        .padding()
        .background(cardColor)
        .cornerRadius(20)
    }
    
    // MARK: - Status Card
    private var statusCard: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Trạng thái")
                .font(.headline)
                .foregroundColor(.white)
            
            HStack {
                Circle()
                    .fill(appState.isGameRunning ? Color.green : Color.red)
                    .frame(width: 10, height: 10)
                Text(appState.isGameRunning ? "Game đang chạy" : "Không phát hiện game")
                    .font(.subheadline)
                    .foregroundColor(.gray)
                Spacer()
            }
            
            Button(action: {
                // Open Game via URL Scheme
                if let url = URL(string: appState.selectedGame.urlScheme) {
                    UIApplication.shared.open(url, options: [:]) { success in
                        if !success {
                            print("Không thể mở game, có thể URL Scheme chưa chính xác hoặc game chưa cài đặt")
                        }
                    }
                }
            }) {
                Text("Mở Game")
                    .font(.headline)
                    .foregroundColor(accentColor)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(accentColor.opacity(0.1))
                    .cornerRadius(12)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(accentColor, lineWidth: 1)
                    )
            }
        }
        .padding()
        .background(cardColor)
        .cornerRadius(20)
    }
    
    @StateObject private var keyManager = KeyManager.shared
    @State private var inputKey: String = ""
    
    // MARK: - Key Card
    private var keyCard: some View {
        VStack(alignment: .leading, spacing: 15) {
            HStack {
                Image(systemName: "key.fill")
                    .foregroundColor(accentColor)
                Text("Quản lý Key")
                    .font(.headline)
                    .foregroundColor(.white)
                Spacer()
                
                if keyManager.isValid {
                    Text("Đã kích hoạt")
                        .font(.caption)
                        .padding(.horizontal, 8)
                        .padding(.vertical, 4)
                        .background(Color.green.opacity(0.2))
                        .foregroundColor(.green)
                        .cornerRadius(8)
                }
            }
            
            if keyManager.isValid {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Key: \(keyManager.displayCode)")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                    Text("Hết hạn: \(formatDate(dateStr: keyManager.expiresAt))")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
            } else {
                VStack(spacing: 12) {
                    TextField("Nhập mã key của bạn...", text: $inputKey)
                        .padding()
                        .background(Color.black.opacity(0.3))
                        .cornerRadius(10)
                        .foregroundColor(.white)
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                        )
                    
                    Text(keyManager.message)
                        .font(.caption)
                        .foregroundColor(keyManager.message.contains("Lỗi") ? .red : .gray)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    HStack(spacing: 15) {
                        Button(action: {
                            keyManager.activateKey(key: inputKey) { _ in }
                        }) {
                            Text("Kích hoạt")
                                .font(.headline)
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(accentColor)
                                .cornerRadius(10)
                        }
                        
                        Button(action: {
                            keyManager.generateFreeKey { newKey in
                                if let newKey = newKey {
                                    inputKey = newKey
                                    keyManager.message = "Đã lấy key Free. Hãy nhấn Kích hoạt."
                                } else {
                                    keyManager.message = "Lỗi lấy key Free"
                                }
                            }
                        }) {
                            Text("Nhận Key Free")
                                .font(.headline)
                                .foregroundColor(accentColor)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(accentColor.opacity(0.1))
                                .cornerRadius(10)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 10)
                                        .stroke(accentColor, lineWidth: 1)
                                )
                        }
                    }
                }
            }
        }
        .padding()
        .background(cardColor)
        .cornerRadius(20)
        .onAppear {
            keyManager.loadSavedKey()
        }
    }
    
    private func formatDate(dateStr: String?) -> String {
        guard let dateStr = dateStr else { return "Vĩnh viễn" }
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
        if let date = formatter.date(from: dateStr) ?? ISO8601DateFormatter().date(from: dateStr) {
            let outFormatter = DateFormatter()
            outFormatter.dateFormat = "dd/MM/yyyy HH:mm"
            return outFormatter.string(from: date)
        }
        return dateStr
    }
    
    // MARK: - Optimizers Card
    private var optimizersCard: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Optimizer Dashboard")
                .font(.headline)
                .foregroundColor(.white)
                .padding(.bottom, 4)
            
            ForEach(optimizerManager.allIds, id: \.self) { id in
                if let optimizer = optimizerManager.getOptimizer(id: id) {
                    OptimizerToggleRow(
                        title: optimizer.name,
                        description: optimizer.description,
                        isSupported: optimizer.isSupported,
                        isOn: Binding<Bool>(
                            get: { optimizerManager.optimizers[id] ?? false },
                            set: { newValue in optimizerManager.toggle(id: id, state: newValue) }
                        )
                    )
                }
            }
        }
        .padding()
        .background(cardColor)
        .cornerRadius(20)
    }
}

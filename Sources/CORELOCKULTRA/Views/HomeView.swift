import SwiftUI

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
                    if UIApplication.shared.canOpenURL(url) {
                        UIApplication.shared.open(url, options: [:], completionHandler: nil)
                    } else {
                        // Fallback or alert
                        print("Cannot open URL scheme")
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
    
    // MARK: - Key Card
    private var keyCard: some View {
        HStack {
            Image(systemName: "key.fill")
                .foregroundColor(accentColor)
            VStack(alignment: .leading) {
                Text("Thời hạn key")
                    .font(.headline)
                    .foregroundColor(.white)
                Text("Demo - Hợp lệ 30 ngày")
                    .font(.caption)
                    .foregroundColor(.green)
            }
            Spacer()
        }
        .padding()
        .background(cardColor)
        .cornerRadius(20)
    }
    
    // MARK: - Optimizers Card
    private var optimizersCard: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Optimizer Dashboard")
                .font(.headline)
                .foregroundColor(.white)
                .padding(.bottom, 4)
            
            let ids = optimizerManager.allIds
            ForEach(ids, id: \.self) { id in
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

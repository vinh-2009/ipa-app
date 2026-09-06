import SwiftUI
import UIKit

struct LoginView: View {
    @StateObject private var keyManager = KeyManager.shared
    @State private var inputKey: String = ""
    
    let bgColor = Color(red: 0.05, green: 0.05, blue: 0.08)
    let cardColor = Color(red: 0.1, green: 0.1, blue: 0.15)
    let accentColor = Color(red: 1.0, green: 0.3, blue: 0.0)
    
    var body: some View {
        VStack(spacing: 30) {
            Spacer()
            
            // Logo
            Image("AppLogo")
                .resizable()
                .scaledToFit()
                .frame(width: 100, height: 100)
                .clipShape(RoundedRectangle(cornerRadius: 20))
                .shadow(color: accentColor.opacity(0.5), radius: 10, x: 0, y: 0)
            
            Text("SUPPERLOCK")
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(.white)
            
            Text("Vui lòng kích hoạt bản quyền để tiếp tục")
                .font(.subheadline)
                .foregroundColor(.gray)
            
            VStack(spacing: 15) {
                TextField("Nhập mã key của bạn...", text: $inputKey)
                    .padding()
                    .background(Color.black.opacity(0.3))
                    .cornerRadius(10)
                    .foregroundColor(.white)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(Color.gray.opacity(0.3), lineWidth: 1)
                    )
                
                if !keyManager.message.isEmpty && keyManager.message != "Vui lòng nhập key" {
                    Text(keyManager.message)
                        .font(.caption)
                        .foregroundColor(keyManager.message.contains("Lỗi") ? .red : .green)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
                
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
                        .shadow(color: accentColor.opacity(0.3), radius: 8, x: 0, y: 4)
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
                    Text("Nhận Key Free (6 Giờ)")
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
            .padding(25)
            .background(cardColor)
            .cornerRadius(20)
            .padding(.horizontal, 20)
            
            Spacer()
            Spacer()
        }
        .background(bgColor.edgesIgnoringSafeArea(.all))
        .onAppear {
            keyManager.loadSavedKey()
        }
    }
}

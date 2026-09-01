import SwiftUI

struct ChatMemberView: View {
    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "message.circle.fill")
                .resizable()
                .scaledToFit()
                .frame(width: 80, height: 80)
                .foregroundColor(Color(red: 1.0, green: 0.3, blue: 0.0))
            
            Text("Chat Member")
                .font(.title)
                .fontWeight(.bold)
                .foregroundColor(.white)
            
            Text("Tham gia cộng đồng để nhận hỗ trợ và cập nhật mới nhất.")
                .multilineTextAlignment(.center)
                .foregroundColor(.gray)
                .padding(.horizontal)
            
            Button(action: {
                if let url = URL(string: "https://t.me/dntweaks") {
                    UIApplication.shared.open(url)
                }
            }) {
                HStack {
                    Image(systemName: "paperplane.fill")
                    Text("Join Telegram Group")
                }
                .font(.headline)
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color(red: 0.2, green: 0.6, blue: 0.8)) // Telegram blue
                .cornerRadius(12)
            }
            .padding(.horizontal, 30)
            
            Button(action: {
                // Open Zalo scheme or URL
            }) {
                HStack {
                    Image(systemName: "bubble.left.and.bubble.right.fill")
                    Text("Join Zalo Group")
                }
                .font(.headline)
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.blue)
                .cornerRadius(12)
            }
            .padding(.horizontal, 30)
            
            Spacer()
        }
        .padding(.top, 50)
        .background(Color(red: 0.05, green: 0.05, blue: 0.08).edgesIgnoringSafeArea(.all))
    }
}

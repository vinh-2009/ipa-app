import SwiftUI

struct AdminView: View {
    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "person.crop.circle.fill")
                .resizable()
                .scaledToFit()
                .frame(width: 80, height: 80)
                .foregroundColor(Color(red: 1.0, green: 0.3, blue: 0.0))
            
            Text("Admin Contact")
                .font(.title)
                .fontWeight(.bold)
                .foregroundColor(.white)
            
            VStack(alignment: .leading, spacing: 15) {
                HStack {
                    Image(systemName: "person.fill")
                        .foregroundColor(.gray)
                        .frame(width: 30)
                    Text("Tác giả: DINH DUC NAM")
                        .foregroundColor(.white)
                }
                
                HStack {
                    Image(systemName: "phone.fill")
                        .foregroundColor(.gray)
                        .frame(width: 30)
                    Text("Zalo: 0395109314")
                        .foregroundColor(.white)
                }
                
                HStack {
                    Image(systemName: "paperplane.fill")
                        .foregroundColor(.gray)
                        .frame(width: 30)
                    Text("Telegram: @dntweaks")
                        .foregroundColor(.white)
                }
            }
            .padding()
            .background(Color(red: 0.1, green: 0.1, blue: 0.15))
            .cornerRadius(15)
            .padding(.horizontal, 30)
            
            Spacer()
        }
        .padding(.top, 50)
        .background(Color(red: 0.05, green: 0.05, blue: 0.08).edgesIgnoringSafeArea(.all))
    }
}

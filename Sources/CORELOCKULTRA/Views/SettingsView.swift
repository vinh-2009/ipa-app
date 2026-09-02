import SwiftUI

struct SettingsView: View {
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Thông tin ứng dụng")) {
                    HStack {
                        Text("Phiên bản")
                        Spacer()
                        Text("1.0.0 (Unsigned)")
                            .foregroundColor(.gray)
                    }
                    
                    HStack {
                        Text("Giấy phép")
                        Spacer()
                        Text("Bản thử nghiệm")
                            .foregroundColor(.gray)
                    }
                }
                
            }
            .navigationTitle("Cài đặt")
            .background(Color(red: 0.05, green: 0.05, blue: 0.08).edgesIgnoringSafeArea(.all))
        }
        .colorScheme(.dark)
    }
}

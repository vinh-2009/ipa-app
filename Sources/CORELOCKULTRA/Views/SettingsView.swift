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
                
                Section(header: Text("Tùy chọn hệ thống (Sandbox)")) {
                    Text("Lưu ý: Do giới hạn bảo mật của iOS Sandbox, một số tính năng tối ưu sâu (như can thiệp RAM hệ thống, CPU governor) sẽ không hoạt động. Các tính năng tối ưu trong ứng dụng chủ yếu hoạt động ở mức mô phỏng hoặc sử dụng API hợp lệ của Apple.")
                        .font(.caption)
                        .foregroundColor(.gray)
                        .padding(.vertical, 4)
                }
            }
            .navigationTitle("Cài đặt")
            .background(Color(red: 0.05, green: 0.05, blue: 0.08).edgesIgnoringSafeArea(.all))
        }
        .colorScheme(.dark)
    }
}

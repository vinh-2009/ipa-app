import SwiftUI

struct OptimizerToggleRow: View {
    let title: String
    let description: String
    let isSupported: Bool
    @Binding var isOn: Bool
    
    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 4) {
                Text(title)
                    .font(.headline)
                    .foregroundColor(.white)
                
                if !isSupported {
                    Text("Không được iOS hỗ trợ")
                        .font(.caption)
                        .foregroundColor(.red)
                } else {
                    Text(description)
                        .font(.caption)
                        .foregroundColor(.gray)
                }
            }
            
            Spacer()
            
            Toggle("", isOn: Binding<Bool>(
                get: { self.isOn },
                set: { newValue in
                    if isSupported {
                        self.isOn = newValue
                    }
                }
            ))
            .labelsHidden()
            .tint(Color(red: 1.0, green: 0.3, blue: 0.0)) // Orange/Red accent
            .disabled(!isSupported)
        }
        .padding(.vertical, 8)
        .padding(.horizontal, 16)
        .background(Color(red: 0.1, green: 0.1, blue: 0.15))
        .cornerRadius(12)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(isOn ? Color(red: 1.0, green: 0.3, blue: 0.0).opacity(0.5) : Color.clear, lineWidth: 1)
        )
        .shadow(color: isOn ? Color(red: 1.0, green: 0.3, blue: 0.0).opacity(0.2) : Color.clear, radius: 5, x: 0, y: 0)
    }
}

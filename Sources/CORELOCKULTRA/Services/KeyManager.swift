import Foundation

class KeyManager: ObservableObject {
    static let shared = KeyManager()
    
    @Published var isValid: Bool = false
    @Published var displayCode: String = ""
    @Published var expiresAt: String? = nil
    @Published var message: String = "Vui lòng nhập key"
    
    private let baseURL = "https://ddnkey.ddnstore.workers.dev"
    
    // UUID cố định mô phỏng HWID của thiết bị
    // Trong thực tế, bạn có thể lấy UIDebuggingInformationOverlay hoặc keychain
    private var deviceId: String {
        if let uuid = UserDefaults.standard.string(forKey: "device_uuid") {
            return uuid
        }
        let uuid = UUID().uuidString
        UserDefaults.standard.set(uuid, forKey: "device_uuid")
        return uuid
    }
    
    func checkKey(key: String, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/check-key") else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let body: [String: String] = ["key": key]
        request.httpBody = try? JSONEncoder().encode(body)
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let data = data, let result = try? JSONDecoder().decode(CheckKeyResponse.self, from: data) {
                    if result.ok {
                        self.isValid = result.valid
                        self.displayCode = result.key.display_code
                        self.expiresAt = result.key.expires_at
                        self.message = result.valid ? "Key hợp lệ" : "Key đã hết hạn"
                        completion(result.valid)
                    } else {
                        self.isValid = false
                        self.message = result.error ?? "Lỗi không xác định"
                        completion(false)
                    }
                } else {
                    self.isValid = false
                    self.message = "Lỗi kết nối máy chủ"
                    completion(false)
                }
            }
        }.resume()
    }
    
    func activateKey(key: String, completion: @escaping (Bool) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/activate") else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let body: [String: String] = ["key": key, "deviceId": deviceId]
        request.httpBody = try? JSONEncoder().encode(body)
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let data = data, let result = try? JSONDecoder().decode(ActivateResponse.self, from: data) {
                    if result.ok {
                        self.isValid = true
                        self.displayCode = result.key?.display_code ?? key
                        self.expiresAt = result.expiresAt
                        self.message = "Kích hoạt thành công!"
                        
                        // Lưu key đã kích hoạt
                        UserDefaults.standard.set(key, forKey: "saved_key")
                        completion(true)
                    } else {
                        self.isValid = false
                        self.message = result.error ?? "Lỗi kích hoạt"
                        completion(false)
                    }
                } else {
                    self.isValid = false
                    self.message = "Lỗi kết nối máy chủ"
                    completion(false)
                }
            }
        }.resume()
    }
    
    func generateFreeKey(completion: @escaping (String?) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/generate-free-key") else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let data = data, let result = try? JSONDecoder().decode(GenerateKeyResponse.self, from: data) {
                    if result.ok {
                        completion(result.key)
                    } else {
                        completion(nil)
                    }
                } else {
                    completion(nil)
                }
            }
        }.resume()
    }
    
    func loadSavedKey() {
        if let savedKey = UserDefaults.standard.string(forKey: "saved_key") {
            // Tự động kiểm tra/kích hoạt lại
            activateKey(key: savedKey) { _ in }
        }
    }
}

// Models
struct CheckKeyResponse: Codable {
    let ok: Bool
    let error: String?
    let key: KeyInfo!
    let valid: Bool!
    let remaining_seconds: Int?
}

struct ActivateResponse: Codable {
    let ok: Bool
    let status: String?
    let error: String?
    let expiresAt: String?
    let key: KeyInfo?
}

struct GenerateKeyResponse: Codable {
    let ok: Bool
    let key: String?
}

struct KeyInfo: Codable {
    let display_code: String
    let status: String
    let max_devices: Int
    let expires_at: String?
}

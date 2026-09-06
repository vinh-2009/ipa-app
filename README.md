# SUPPERLOCK (iOS)

SUPPERLOCK là ứng dụng iOS được thiết kế với giao diện gaming cao cấp, cung cấp tính năng tối ưu trải nghiệm người dùng (tuân thủ giới hạn của hệ điều hành iOS). 

Dự án này được thiết kế để có thể build hoàn toàn tự động thông qua GitHub Actions mà không cần máy tính Mac cá nhân, tạo ra file IPA Unsigned để tự sideload.

## Cách lấy file IPA Unsigned trên Windows

Bạn không cần cài đặt macOS hay Xcode. Hãy làm theo các bước sau:

1. Fork hoặc clone repository này về tài khoản GitHub của bạn.
2. Mở repository trên GitHub, chuyển đến tab **Actions**.
3. Chọn workflow **Build iOS Unsigned IPA** ở thanh bên trái.
4. Nhấn nút **Run workflow** -> **Run workflow** (nếu bạn muốn build thủ công) hoặc đẩy một commit lên nhánh `main`.
5. Đợi workflow chạy xong (mất khoảng 2-5 phút).
6. Khi có dấu check xanh (thành công), cuộn xuống phần **Artifacts** trong trang tóm tắt của workflow.
7. Tải xuống file **CORELOCK-ULTRA-unsigned** (sẽ tải về dưới dạng `.zip`, bên trong chứa file `CORELOCK-ULTRA-unsigned.ipa`).

**Lưu ý quan trọng**: 
- Đây là file IPA **Unsigned**. Bạn không thể cài đặt trực tiếp lên iPhone/iPad nếu chưa có chứng chỉ.
- Bạn cần sử dụng các công cụ như TrollStore, Sideloadly, AltStore, hoặc tài khoản Apple Developer (chứng chỉ .p12 & mobileprovision) để ký và cài đặt file IPA này.

## Giới hạn hệ thống iOS (Sandbox & Security)

Ứng dụng này tuân thủ các quy định bảo mật của iOS:
- Không thể trực tiếp sửa đổi CPU governor, RAM hệ thống hay can thiệp vào Memory của game (như Android root).
- Không bypass anti-cheat hay thay đổi các packet mạng của hệ điều hành.
- Floating Menu overlay lên ứng dụng khác không được hỗ trợ tự do trên iOS. Chức năng tối ưu được thực hiện thông qua Dashboard trong ứng dụng hoặc các API hệ thống hợp lệ.

## Tác giả
- **DINH DUC NAM**
- Zalo: 0395109314
- Telegram: [@dntweaks](https://t.me/dntweaks)

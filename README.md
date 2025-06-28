<p align="center">
  <img src="app/src/main/ic_launcher-rounded.png" alt="Shhh logo" width="120" style="border-radius: 50px;"/>
</p>

<h1 align="center">C.A.M.P.U.S – Cộng Đồng Sinh Viên</h1>

<p align="center">
  Một dự án mã nguồn mở trong khuôn khổ học phần Lập trình Thiết bị Di động tại University of Transport Ho Chi Minh City.
</p>

---

## Giới thiệu

**C.A.M.P.U.S – Cộng Đồng Sinh Viên** là một không gian an toàn và ẩn danh dành cho sinh viên, nơi bạn có thể tự do chia sẻ những suy nghĩ, tâm sự, và những câu chuyện thầm kín mà không lo bị phán xét.
### Các tính năng chính:

* **Đăng bài ẩn danh**: Chia sẻ câu chuyện của bạn với một trong nhiều danh tính được cung cấp sẵn.
* **Tương tác đa dạng**: Thể hiện cảm xúc với các bài viết qua hệ thống reaction và bình luận.
* **Tùy chỉnh linh hoạt**: Lựa chọn trường và danh tính ẩn danh để cá nhân hóa trải nghiệm.
* **Giao diện hiện đại**: Xây dựng hoàn toàn bằng Jetpack Compose, mang lại trải nghiệm mượt mà và đẹp mắt.

---

## Công nghệ & Kiến trúc

Dự án được xây dựng dựa trên các công nghệ và kiến trúc hiện đại, theo mô hình MVVM (Model-View-ViewModel).

| Thành phần | Công nghệ / Thư viện |
| :--- | :--- |
| **Giao diện người dùng (UI)** | Jetpack Compose, Material 3, Coil (hiển thị ảnh), Haze (hiệu ứng mờ) |
| **Kiến trúc** | MVVM, Clean Architecture (Use Cases, Repositories) |
| **Bất đồng bộ** | Kotlin Coroutines, Flow |
| **Dependency Injection** | Hilt (Dagger) |
| **Navigation** | Jetpack Navigation Compose |
| **Dữ liệu từ xa (Remote)** | Retrofit, OkHttp, Gson |
| **Dữ liệu cục bộ (Local)** | Room (Database), Jetpack DataStore (quản lý session), Paging 3 (phân trang) |
| **Xác thực** | Firebase Authentication (Email, Google Sign-In) |

---

## Cài đặt và Chạy thử

1.  **Clone repository:**
    ```bash
    git clone [https://github.com/doanhoaian/shhh-confession-university-app.git](https://github.com/doanhoaian/shhh-confession-university-app.git)
    ```
2.  Mở dự án bằng **Android Studio**.
3.  Cập nhật địa chỉ `BASE_URL` trong tệp `app/build.gradle.kts` để trỏ đến máy chủ backend của bạn.
    ```kotlin
    buildConfigField("String", "BASE_URL", "\"http://YOUR_IP_ADDRESS:3000/api/\"")
    ```
4.  Build và chạy ứng dụng.

---

## Đóng góp

Chúng tôi hoan nghênh mọi sự đóng góp từ cộng đồng! Nếu bạn muốn đóng góp, vui lòng tham khảo tệp [**CONTRIBUTING.md**](./CONTRIBUTING.md) để biết thêm chi tiết về quy trình tạo Pull Request và các quy tắc làm việc.

---

## Nhóm phát triển

| Tên | Vai trò |
| :--- | :--- |
| Đoàn Hoài Ân | Trưởng nhóm |
| Nguyễn Thành Khoa | Thành viên |
| Nguyễn Văn Nhật | Thành viên |

---

> © 2025 • Group of students from University of Transport Ho Chi Minh City – Course Lập trình thiết bị di động.

<!--suppress ALL -->
<p align="center">
  <img src="app/src/main/ic_launcher-rounded.png" alt="Shhh logo" width="120" style="border-radius: 50px;"/>
</p>

<h1 align="center">Shhh – Ứng dụng Confession ẩn danh cho sinh viên</h1>

<p align="center">
Đồ án học phần Lập trình thiết bị di động • University of Transport Ho Chi Minh City • Năm học 2024–2025  
</p>

<p align="center">
  <a href="https://byvn.net/ikSi"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/vi_badge_web_generic.png" alt="Tải trên Google Play" height="70"/></a>
</p>

---

## 📌 Mục tiêu dự án

**Shhh - Câu Chuyện Trường Tôi** là ứng dụng mạng xã hội nhỏ dành cho sinh viên, nơi mọi người có
thể:

- Đăng các bài viết **ẩn danh**
- Xem bài của trường/khoa khác
- **Reaction** (👍, ❤️, 😆...) các bài viết
- Bình luận bài viết
- Xem thống kê bài viết phổ biến

Dự án được thực hiện nhằm:

- Vận dụng kiến thức lập trình mobile (Android – Kotlin)
- Học cách tổ chức, phân công và làm việc nhóm theo mô hình thật
- Trải nghiệm quy trình Dev thực tế (GitHub Flow, CI/CD, release...)

---

## ⚙️ Công nghệ sử dụng

| Thành phần | Công nghệ                                                                |
|------------|--------------------------------------------------------------------------|
| Frontend   | Android (Kotlin, Jetpack Compose)                                        |
| Backend    | Firebase (Storage /Auth), API (Node.js + Express), Database SQL (My SQL) |
| Design     | Figma (Xem tại `/docs`)                                                  |
| CI/CD      | GitHub Actions                                                           |

---

## 📂 Cấu trúc repo

```
shhh-confession-university-app/
├── app/                 # Mã nguồn chính Android
├── doc/                 # Tài liệu thiết kế, đặc tả, luồng người dùng
├── CONTRIBUTING.md      # Hướng dẫn đóng góp
└── README.md            # Tệp hiện tại
```

---

## 📦 Cài đặt & chạy thử

```bash
git clone https://github.com/doanhoaian/shhh-confession-university-app.git
cd shhh-confession-university-app
# Mở bằng Android Studio và build
```

---

## 🧑‍💻 Nhóm phát triển

| Tên               | Vai trò     |
|-------------------|-------------|
| Đoàn Hoài Ân      | Trưởng nhóm |
| Nguyễn Thành Khoa | Thành viên  |
| Nguyễn Văn Nhật   | Thành viên  |

- Mỗi thành viên làm việc theo nhánh `feature/<ten-nhanh>` và tạo PR
- Merge có kiểm duyệt bởi trưởng nhóm

> 📌 Xem thêm phân công nhiệm vụ trong file [`/docs/Task.md`](./docs/Task.md)

---

## 📄 Tài liệu tham khảo

- Thiết kế luồng người dùng: [`/docs/Flow.pdf`](docs/Flow.pdf)
- Đặc tả chức năng: [`/docs/Spec.md`](docs/Spec.md)

---

## 📤 Dự kiến phát hành

Ứng dụng sẽ được phát hành trên **Google Play** sau khi hoàn thiện:

<p align="left">
  <a href="https://byvn.net/ikSi"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/vi_badge_web_generic.png" alt="Tải trên Google Play" height="60"/></a>
</p>

---

## 💬 Liên hệ

Nếu bạn có câu hỏi, góp ý hoặc báo lỗi, vui lòng mở issue hoặc liên hệ qua email:

✉️ an.doan.dev@gmail.com

---

> © 2025 • Group of students from University of Transport Ho Chi Minh City – Course Lập trình thiết bị di động

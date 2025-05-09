# 🤝 Quy trình đóng góp cho dự án *Shhh Confession University App*

Cảm ơn bạn đã đóng góp cho dự án! Vui lòng làm theo hướng dẫn dưới đây để đảm bảo quá trình làm việc nhóm được hiệu quả.

---

## 🚀 Bắt đầu

### 1. Clone dự án về máy (Bỏ qua nếu đã có sẵn)

Sử dụng Clone Repository trong Android Studio hoặc qua terminal:

```bash
git clone https://github.com/doanhoaian/shhh-confession-university-app.git
```

### 2. Chuyển sang nhánh `develop` và cập nhật bản mới nhất

```bash
git checkout develop
git pull origin develop
```

### 3. Tạo nhánh mới từ `develop`

> ⚠️ **Lưu ý:** Tên nhánh nên viết theo format:
>
> - `feature/<ten-chuc-nang>`: cho tính năng mới
> - `bugfix/<ten-loi>`: cho sửa lỗi
> - `hotfix/<ten-sua-gap>`: cho sửa gấp trên production

```bash
git checkout -b feature/<ten-nhanh>
```

Ví dụ:

```bash
git checkout -b feature/login-ui
```

---

## 🛠 Làm việc và đẩy code

### 4. Sau khi hoàn tất thay đổi, commit và push

```bash
git add .
git commit -m "feat: thêm màn hình đăng nhập"
git push origin feature/<ten-nhanh>
```

---

## 🔁 Tạo Pull Request (PR)

### 5. Mở PR từ `feature/<ten-nhanh>` vào `develop` trên GitHub

- Vào repository trên GitHub
- Chọn **"Pull request"**
- Đặt tiêu đề rõ ràng (ví dụ: `Thêm giao diện màn hình đăng nhập`)
- Mô tả ngắn gọn những thay đổi trong PR
- Bạn **không cần người khác duyệt**, nhưng **không thể tự merge** – PR sẽ được xem xét và merge bởi quản lý dự án

---

## 👀 Review & Merge

### 6. Code review

- Đảm bảo đã test kỹ trước khi gửi PR
- Chỉnh sửa theo góp ý nếu có

### 7. Merge

- Thành viên **không thể merge** trực tiếp vào `develop` hoặc `main`
- PR sẽ được merge bởi **quản lý dự án (chủ repository)**

---

## 🧹 Dọn dẹp

### 8. Sau khi PR được merge


Bạn có thể xóa nhánh `feature/<ten-nhanh>` của mình để tránh rối repository:

```bash
git branch -d feature/<ten-nhanh>
git push origin --delete feature/<ten-nhanh>
```

---

## ✅ Lưu ý chung

- Tuân thủ quy chuẩn đặt tên commit:
    - `feat:` – thêm mới
    - `fix:` – sửa lỗi
    - `refactor:` – tái tổ chức code
    - `docs:` – tài liệu
- Giữ commit nhỏ gọn, rõ nghĩa
- Cập nhật tài liệu nếu có thay đổi ảnh hưởng đến người khác

---

🎉 Chúc bạn làm việc hiệu quả!
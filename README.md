# Library CRM (Backend API)

## 📖 Giới thiệu
Library CRM là hệ thống quản lý và chăm sóc độc giả tập trung vào xử lý logic nghiệp vụ phía máy chủ. Dự án hiện tại tập trung vào việc xây dựng một hệ thống API mạnh mẽ, bảo mật để quản lý kho sách, quy trình mượn trả và phân tích dữ liệu độc giả.

## 💻 Công nghệ sử dụng (Tech Stack)
* **Ngôn ngữ & Framework:** Java Spring Boot.
* **Bảo mật:** Spring Security (Xử lý xác thực và phân quyền dựa trên Role).
* **Kiến trúc:** RESTful API.
* **Database:** *(Bạn có thể điền thêm như MySQL/PostgreSQL nếu có)*.

## 🛠 Trạng thái dự án (Project Status)
* **Backend:** Hoàn thiện các module cốt lõi và hệ thống phân quyền.
* **Frontend:** Đang trong lộ trình phát triển (Dự kiến sử dụng ReactJS).

## ✨ Các tính năng Backend đã triển khai

### 1. Hệ thống phân quyền (RBAC)
Cấu hình các Endpoint API chuyên biệt cho 3 nhóm đối tượng:
* **Admin:** Quản lý cấu hình hệ thống và theo dõi Dashboard tổng quát.
* **Librarian (Thủ thư):** Các API nghiệp vụ về quản lý sách, xử lý phiếu mượn/trả và quản lý hồ sơ CRM của khách hàng.
* **Reader (Độc giả):** API tra cứu danh mục sách và xem thông tin lịch sử cá nhân.

### 2. Các Module nghiệp vụ chính
* **Quản lý sách (Book Management):** CRUD thông tin sách, quản lý số lượng tồn kho thực tế.
* **Quản lý độc giả (Member CRM):** Lưu trữ thông tin, phân loại và theo dõi mức độ tương tác của độc giả.
* **Luồng mượn trả (Circulation):** Logic xử lý mượn sách, gia hạn và tính toán ngày trả.
* **Nhập hàng (Procurement):** Quản lý quy trình nhập sách mới từ các nguồn cung cấp.

## 🚀 Hướng dẫn chạy dự án (Local Setup)
1. **Yêu cầu:** Java SDK 17+, Maven.
2. **Cài đặt:**
   ```bash
   mvn clean install
   mvn spring-boot:run

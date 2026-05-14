# Tài khoản đăng nhập mặc định
Sử dụng tài khoản quản trị viên có sẵn trong dữ liệu mẫu để vào hệ thống:

Username: admin

Password: admin123


# Hướng dẫn Cài đặt & Chạy chương trình
## Bước 1: Khởi tạo Cơ sở dữ liệu (MySQL)
1. Mở công cụ quản lý cơ sở dữ liệu (ví dụ: **MySQL Workbench** hoặc **phpMyAdmin**).
2. Sử dụng tệp tin `shoestoredb.sql` được đính kèm trong thư mục dự án.
3. Chạy (Run/Import) toàn bộ nội dung tệp tin này. 
   > **Kết quả:** Hệ thống sẽ tự động tạo database tên là `shoestoredb` và nạp sẵn các dữ liệu mẫu (sản phẩm, nhân viên, tài khoản...).

## Bước 2: Cấu hình kết nối Database trong Code
Bạn cần trỏ code Java đến đúng MySQL trên máy tính của bạn.
1. Mở file mã nguồn: `src/utils/DBConnection.java`.
2. Tìm đến hằng số `PASSWORD` và đổi thành mật khẩu Database của bạn:
   ```java
   // Thông tin kết nối MySQL
   private static final String URL = "jdbc:mysql://localhost:3306/shoestoredb?useUnicode=true&characterEncoding=UTF-8";
   private static final String USER = "root";
   private static final String PASSWORD = "mat_khau_cua_ban"; // <-- SỬA MẬT KHẨU TẠI ĐÂY
   ```
   
## Bước 3: Thêm thư viện kết nối (JDBC Driver)
Nếu các file Java báo lỗi đỏ ở thư viện java.sql, bạn cần nạp driver MySQL:

1. Chuột phải vào Project trong IDE (Eclipse/NetBeans) -> Chọn Build Path -> Configure Build Path...

2. Chuyển sang tab Libraries -> Chọn Classpath.

3. Nhấn Add External JARs... và tìm chọn file mysql-connector-j-9.6.0.jar.

4. Nhấn Apply and Close.

##Bước 4: Khởi chạy chương trình
⚠️ Tuyệt đối không chạy trực tiếp các form chức năng con (như ProductForm, ImportForm) để tránh lỗi mất phiên đăng nhập (Session).

1. Tìm đến file gốc của dự án: src/main/JavaApplication1.java.

2. Chuột phải vào file này -> Chọn Run As -> Java Application.

3. Màn hình đăng nhập sẽ hiện ra đầu tiên.
   

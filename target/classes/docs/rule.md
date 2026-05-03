# RULE.md — Quy tắc code dự án JAVA_COFFEE_MGM

## 1. Kiến trúc dự án

Dự án là Java Swing Maven, dùng mô hình MVC + DAO.

Cấu trúc package chính:

```text
src/main/java/cafemanager/
├── model/
├── dao/
├── controller/
├── view/
├── util/
└── dto/        // chỉ tạo nếu thật sự cần
2. Trách nhiệm từng package
view/

Chỉ chứa giao diện Swing.

Được phép:

JFrame, JPanel, JDialog
Lấy dữ liệu từ JTextField, JTable, JComboBox
Gọi controller
Hiển thị JOptionPane
Cập nhật bảng/label/nút

Không được:

Viết SQL
Dùng PreparedStatement, ResultSet
Xử lý transaction DB
Hash/check password trực tiếp
Chứa logic nghiệp vụ nặng

Ví dụ:

LoginFrame.java
MainFrame.java
POSPanel.java
InvoiceHistoryPanel.java
controller/

Chứa logic nghiệp vụ.

Được phép:

Validate dữ liệu
Xử lý đăng nhập
Xử lý thanh toán
Tạo Bill, BillDetail
Gọi DAO

Không được:

Viết SQL
Code layout Swing
Truy cập DB trực tiếp

Ví dụ:

AuthController.java
BillController.java
dao/

Chứa code truy cập database.

Được phép:

SQL
Connection
PreparedStatement
ResultSet
Transaction
Map dữ liệu DB sang model

Không được:

JOptionPane
Truy cập component Swing
Logic giao diện

Ví dụ:

AccountDAO.java
ProductDAO.java
BillDAO.java
CategoryDAO.java
model/

Chứa class dữ liệu.

Được phép:

Field private
Constructor
Getter/setter
Validation đơn giản
Quan hệ dữ liệu, ví dụ Bill chứa List<BillDetail>

Không được:

SQL
Swing
Controller logic

Ví dụ:

Account.java
Product.java
Bill.java
BillDetail.java
util/

Chứa tiện ích dùng chung.

Ví dụ:

DBConnection.java
PasswordUtils.java
SessionManager.java
CurrencyUtils.java nếu cần
dto/

Chỉ tạo nếu cần object tạm để truyền dữ liệu.

Ví dụ:

POSOrderItem.java

Dùng để giữ dữ liệu giỏ hàng tạm:

productId
productName
unitPrice
quantity
subtotal
3. Luật với NetBeans GUI Builder

Không được sửa code bên trong:

private void initComponents() {
    ...
}

Không được xóa file .form.

Chỉ sửa ở:

import
field trong class
constructor sau initComponents();
hàm private tự viết bên ngoài initComponents()
event handler đã được NetBeans tạo sẵn

Ví dụ đúng:

public POSPanel() {
    initComponents();
    initTables();
    registerEvents();
    loadProducts();
}
4. Database

Database hiện tại:

java_coffee_db

Tên bảng dùng lowercase:

account
category
product
bill
bill_detail

Một số cột đang có:

account:
- accountId
- full_name
- isActive
- password
- role
- username

product:
- productId
- imageUrl
- isActive
- price
- product_name
- category_id

Không tự đổi tên cột DB.

Nếu không chắc tên cột bảng bill hoặc bill_detail, phải hỏi lại hoặc yêu cầu chạy:

DESCRIBE bill;
DESCRIBE bill_detail;
5. Đăng nhập và mật khẩu

Password trong DB là BCrypt hash.

Không được so sánh plaintext kiểu:

inputPassword.equals(account.getPassword())

Phải dùng:

PasswordUtils.checkPassword(inputPassword, account.getPassword())

Khi login thành công:

SessionManager.login(account);

Khi logout:

SessionManager.logout();
6. Phân quyền

Role gồm:

ADMIN
STAFF

ADMIN thấy tất cả tab:

SALES / BILL / MENU / STAFF / STATS

STAFF chỉ thấy:

SALES

MainFrame phải ẩn/hiện tab theo role sau khi login.

7. POSPanel

POSPanel làm các việc:

Load sản phẩm đang bán
Tìm kiếm sản phẩm
Thêm món vào hóa đơn tạm
Nếu món đã có thì cộng số lượng
Xóa món
Hủy hóa đơn
Tính tổng tiền
Thanh toán
Gọi BillController để lưu hóa đơn

POSPanel không được viết SQL trực tiếp.

Bảng sản phẩm không hiển thị productId dài.

Cột bảng sản phẩm:

Tên món | Giá | Danh mục

Cột bảng hóa đơn:

Tên món | Đơn giá | Số lượng | Tạm tính

Vì productId không hiện trên UI, phải dùng List<POSOrderItem> hoặc object tương đương để giữ dữ liệu thật của hóa đơn tạm. Không được xem JTable là nguồn dữ liệu chính duy nhất.

8. Thanh toán

Khi thanh toán:

Bill.status = PAID
Bill.totalAmount = tổng subtotal
Bill.accountId lấy từ SessionManager.getCurrentAccount()
BillDetail phải snapshot:
productId
productName
unitPrice
quantity
subtotal

Lưu bill phải dùng transaction:

1. Insert bill
2. Lấy bill_id vừa sinh
3. Insert nhiều bill_detail
4. Commit
5. Lỗi thì rollback
9. InvoiceHistoryPanel

InvoiceHistoryPanel làm các việc:

Load danh sách hóa đơn
Lọc theo ngày/trạng thái nếu UI có
Làm mới danh sách
Xem chi tiết hóa đơn

Bảng hóa đơn:

Mã HĐ | Nhân viên | Thời gian | Tổng tiền | Trạng thái

Mã hóa đơn hiển thị dạng:

DH000001

Khi xem chi tiết thì parse lại:

DH000001 -> 1

Chi tiết hóa đơn hiển thị:

Tên món | Đơn giá | Số lượng | Tạm tính
10. Quy tắc tạo file mới

Được tạo file mới nếu cần đúng kiến trúc.

Được tạo:

controller/AuthController.java
controller/BillController.java
util/PasswordUtils.java
util/SessionManager.java
util/CurrencyUtils.java hoặc FormatUtils.java nếu cần
dto/POSOrderItem.java nếu cần giữ giỏ hàng tạm

Không được tạo:

POSFrame.java
InvoiceFrame.java
AdminMainFrame.java
StaffMainFrame.java
UI mới trùng với panel đã có

Trước khi tạo file, phải kiểm tra file tương đương đã tồn tại chưa.

11. Build

Sau khi sửa code:

Build Maven project
Sửa hết compile error
Không để import lỗi
Không tạo method/class trùng
Không làm hỏng file .form
USE JAVA_COFFEE_DB;

-- 1. Chèn dữ liệu cho CATEGORY
INSERT INTO CATEGORY (category_name, description) VALUES 
('Cà phê', 'Các loại cà phê pha máy và pha phin truyền thống'),
('Trà trái cây', 'Trà thanh nhiệt kết hợp trái cây tươi'),
('Bánh ngọt', 'Các loại bánh dùng kèm đồ uống'),
('Nước ép', 'Nước ép trái cây nguyên chất 100%'),
('Món ăn nhẹ', 'Các món mặn lót dạ cho sinh viên');

-- 2. Chèn dữ liệu cho ACCOUNT 
-- Lưu ý: Harrison Vo là Admin, Mỹ là Staff
INSERT INTO ACCOUNT (username, password, full_name, role, is_active) VALUES 
('harrison_vo', 'admin123', 'Harrison Vo', 'Admin', TRUE),
('my_nguyen', 'staff456', 'Nguyễn Thị Mỹ', 'Staff', TRUE),
('sinhvien_dut', 'dut789', 'Sinh Viên Bách Khoa', 'Customer', TRUE),
('vukhanh', 'vukhanh11', 'Lê Vũ Khánh', 'Customer', TRUE),
('tiendung', 'dung22', 'Nguyễn Tiến Dũng', 'Staff', TRUE);

-- 3. Chèn dữ liệu cho PRODUCT
INSERT INTO PRODUCT (category_id, product_name, price, image_url, is_active) VALUES 
(1, 'Cà phê muối', 25000, 'cf_muoi.jpg', TRUE),
(1, 'Bạc xỉu', 22000, 'bac_xiu.jpg', TRUE),
(2, 'Trà đào cam sả', 35000, 'tra_dao.jpg', TRUE),
(3, 'Tiramisu', 40000, 'tiramisu.jpg', TRUE),
(5, 'Bánh mì thịt nướng', 20000, 'bm_thit.jpg', TRUE);

-- 4. Chèn dữ liệu cho BILL
INSERT INTO BILL (account_id, total_amount, status) VALUES 
(1, 47000, 'Paid'),
(3, 35000, 'Paid'),
(4, 25000, 'Pending'),
(3, 60000, 'Paid'),
(5, 20000, 'Cancelled');

-- 5. Chèn dữ liệu cho BILL_DETAIL
INSERT INTO BILL_DETAIL (bill_id, product_id, product_name, unit_price, quantity, subtotal) VALUES 
(1, 1, 'Cà phê muối', 25000, 1, 25000),
(1, 2, 'Bạc xỉu', 22000, 1, 22000),
(2, 3, 'Trà đào cam sả', 35000, 1, 35000),
(4, 1, 'Cà phê muối', 25000, 2, 50000),
(4, 5, 'Bánh mì thịt nướng', 10000, 1, 10000);
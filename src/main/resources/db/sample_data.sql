USE java_coffee_db;

-- Password chung: 123
-- BCrypt hash: $2a$10$sb.qjY08CRkHS3UWpigILePJhddCQY1zTCinigzeNOIVyaxgO/XZy
INSERT INTO Account (username, password, full_name, role, is_active) VALUES
('admin', '$2a$10$sb.qjY08CRkHS3UWpigILePJhddCQY1zTCinigzeNOIVyaxgO/XZy', 'Quản lý', 'ADMIN', TRUE),
('staff01', '$2a$10$sb.qjY08CRkHS3UWpigILePJhddCQY1zTCinigzeNOIVyaxgO/XZy', 'Nhân viên 01', 'STAFF', TRUE);

INSERT INTO Category (category_name, description) VALUES
('Cà phê', 'Các món cà phê'),
('Trà', 'Các món trà'),
('Nước ép', 'Các món nước ép'),
('Bánh ngọt', 'Bánh ăn kèm');

INSERT INTO Product (category_id, product_name, price, description, is_active) VALUES
(1, 'Cà phê đen', 20000, 'Cà phê đen truyền thống', TRUE),
(1, 'Cà phê sữa', 25000, 'Cà phê sữa đá', TRUE),
(1, 'Bạc xỉu', 30000, 'Bạc xỉu đá', TRUE),
(2, 'Trà đào', 35000, 'Trà đào cam sả', TRUE),
(2, 'Trà vải', 35000, 'Trà vải thanh mát', TRUE),
(3, 'Nước cam', 30000, 'Nước cam tươi', TRUE),
(4, 'Tiramisu', 45000, 'Bánh tiramisu', TRUE),
(4, 'Bánh flan', 25000, 'Bánh flan caramel', TRUE);

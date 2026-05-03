USE java_coffee_db;

-- =========================
-- 1. SEED ACCOUNT
-- Password chung: 123
-- BCrypt hash: $2a$10$sb.qjY08CRkHS3UWpigILePJhddCQY1zTCinigzeNOIVyaxgO/XZy
-- =========================

INSERT INTO
    account (
        username,
        password,
        full_name,
        role,
        isActive
    )
VALUES (
        'admin',
        '$2a$10$sb.qjY08CRkHS3UWpigILePJhddCQY1zTCinigzeNOIVyaxgO/XZy',
        'Admin',
        'ADMIN',
        TRUE
    ),
    (
        'staff01',
        '$2a$10$sb.qjY08CRkHS3UWpigILePJhddCQY1zTCinigzeNOIVyaxgO/XZy',
        'Nhân viên 01',
        'STAFF',
        TRUE
    );

-- =========================
-- 2. SEED CATEGORY
-- =========================

INSERT INTO
    category (category_name, description)
VALUES ('Cà phê', 'Các món cà phê'),
    ('Trà', 'Các món trà'),
    ('Bánh ngọt', 'Bánh ăn kèm');

-- =========================
-- 3. SEED PRODUCT
-- Không insert productId vì nó AUTO_INCREMENT
-- =========================

INSERT INTO
    product (
        product_name,
        price,
        category_id,
        imageUrl,
        isActive
    )
VALUES (
        'Cà phê đen',
        20000,
        1,
        NULL,
        TRUE
    ),
    (
        'Cà phê sữa',
        25000,
        1,
        NULL,
        TRUE
    ),
    (
        'Bạc xỉu',
        30000,
        1,
        NULL,
        TRUE
    ),
    (
        'Trà đào',
        35000,
        2,
        NULL,
        TRUE
    ),
    (
        'Trà vải',
        35000,
        2,
        NULL,
        TRUE
    ),
    (
        'Tiramisu',
        45000,
        3,
        NULL,
        TRUE
    ),
    (
        'Bánh flan',
        25000,
        3,
        NULL,
        TRUE
    );
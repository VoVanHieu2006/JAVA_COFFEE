CREATE DATABASE IF NOT EXISTS java_coffee_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE java_coffee_db;

DROP TABLE IF EXISTS BillDetail;
DROP TABLE IF EXISTS Bill;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Account;

CREATE TABLE Account (
    account_id  INT            NOT NULL AUTO_INCREMENT,
    username    VARCHAR(50)    NOT NULL,
    password    VARCHAR(255)   NOT NULL,
    full_name   VARCHAR(100)   NOT NULL,
    role        ENUM('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF',
    is_active   BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_account    PRIMARY KEY (account_id),
    CONSTRAINT uq_username   UNIQUE (username),
    CONSTRAINT chk_username  CHECK (LENGTH(username) >= 4)
);

CREATE TABLE Category (
    category_id    INT          NOT NULL AUTO_INCREMENT,
    category_name  VARCHAR(100) NOT NULL,
    description    TEXT,
    CONSTRAINT pk_category    PRIMARY KEY (category_id),
    CONSTRAINT uq_cat_name    UNIQUE (category_name)
);

CREATE TABLE Product (
    product_id    INT             NOT NULL AUTO_INCREMENT,
    category_id   INT             NOT NULL,
    product_name  VARCHAR(150)    NOT NULL,
    price         DECIMAL(10,2)   NOT NULL,
    description   TEXT,
    is_active     BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_product       PRIMARY KEY (product_id),
    CONSTRAINT fk_product_cat   FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE RESTRICT,
    CONSTRAINT chk_price        CHECK (price > 0),
    CONSTRAINT uq_product_name  UNIQUE (product_name, category_id)
);

CREATE TABLE Bill (
    bill_id       INT             NOT NULL AUTO_INCREMENT,
    account_id    INT             NOT NULL,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount  DECIMAL(10,2)   NOT NULL DEFAULT 0,
    status        ENUM('PENDING','PAID','CANCELLED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT pk_bill          PRIMARY KEY (bill_id),
    CONSTRAINT fk_bill_account  FOREIGN KEY (account_id) REFERENCES Account(account_id) ON DELETE RESTRICT,
    CONSTRAINT chk_total        CHECK (total_amount >= 0)
);

CREATE TABLE BillDetail (
    detail_id     INT             NOT NULL AUTO_INCREMENT,
    bill_id       INT             NOT NULL,
    product_id    INT             NOT NULL,
    product_name  VARCHAR(150)    NOT NULL,
    unit_price    DECIMAL(10,2)   NOT NULL,
    quantity      INT             NOT NULL,
    subtotal      DECIMAL(10,2)   NOT NULL,
    CONSTRAINT pk_detail          PRIMARY KEY (detail_id),
    CONSTRAINT fk_detail_bill     FOREIGN KEY (bill_id)    REFERENCES Bill(bill_id)    ON DELETE CASCADE,
    CONSTRAINT fk_detail_product  FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE RESTRICT,
    CONSTRAINT chk_quantity       CHECK (quantity > 0),
    CONSTRAINT chk_unit_price     CHECK (unit_price > 0),
    CONSTRAINT chk_subtotal       CHECK (subtotal > 0)
);

CREATE INDEX idx_product_active ON Product(is_active);
CREATE INDEX idx_bill_created_at ON Bill(created_at);
CREATE INDEX idx_bill_status ON Bill(status);

DELIMITER //
CREATE PROCEDURE sp_GetRevenueByDateRange(IN p_from_date DATE, IN p_to_date DATE)
BEGIN
    SELECT DATE(created_at) AS sale_date,
           COUNT(bill_id) AS total_bills,
           SUM(total_amount) AS daily_revenue
    FROM Bill
    WHERE status = 'PAID'
      AND DATE(created_at) BETWEEN p_from_date AND p_to_date
    GROUP BY DATE(created_at)
    ORDER BY sale_date ASC;
END //

CREATE PROCEDURE sp_GetTopSellingProducts(IN p_from_date DATE, IN p_to_date DATE, IN p_limit INT)
BEGIN
    SELECT bd.product_name,
           SUM(bd.quantity) AS total_sold,
           SUM(bd.subtotal) AS total_revenue
    FROM BillDetail bd
    JOIN Bill b ON bd.bill_id = b.bill_id
    WHERE b.status = 'PAID'
      AND DATE(b.created_at) BETWEEN p_from_date AND p_to_date
    GROUP BY bd.product_name
    ORDER BY total_sold DESC
    LIMIT p_limit;
END //
DELIMITER ;

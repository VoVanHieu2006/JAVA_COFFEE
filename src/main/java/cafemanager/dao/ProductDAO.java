package cafemanager.dao;

import cafemanager.model.Category;
import cafemanager.model.Product;
import cafemanager.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ─── READ ────────────────────────────────────────────────────────────────

    public List<Product> getAllProducts() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = """
                SELECT p.productId, p.product_name, p.price, p.isActive,
                       p.category_id, p.imageUrl,
                       c.categoryId, c.category_name
                FROM product p
                LEFT JOIN category c ON p.category_id = c.categoryId
                ORDER BY p.product_name
                """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Product> findAllActive() {
        List<Product> list = new ArrayList<>();
        String sql = """
                SELECT p.productId, p.product_name, p.price, p.category_id,
                       p.imageUrl, p.isActive,
                       c.categoryId, c.category_name
                FROM product p
                JOIN category c ON p.category_id = c.categoryId
                WHERE p.isActive = TRUE
                ORDER BY p.product_name
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ─── CREATE ──────────────────────────────────────────────────────────────

    /**
     * Thêm sản phẩm mới vào DB.
     * @return productId được sinh ra, hoặc -1 nếu thất bại.
     */
    public int addProduct(Product product) throws Exception {
        String sql = """
                INSERT INTO product (product_name, price, category_id, imageUrl, isActive)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getProductName());
            ps.setBigDecimal(2, product.getPrice());
            ps.setInt(3, product.getCategoryId());
            ps.setString(4, product.getImageUrl());
            ps.setBoolean(5, product.isActive());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    /** Cập nhật tên, giá, danh mục và trạng thái của sản phẩm. */
    public boolean updateProduct(Product product) throws Exception {
        String sql = """
                UPDATE product
                SET product_name = ?, price = ?, category_id = ?, isActive = ?
                WHERE productId = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.getProductName());
            ps.setBigDecimal(2, product.getPrice());
            ps.setInt(3, product.getCategoryId());
            ps.setBoolean(4, product.isActive());
            ps.setInt(5, product.getProductId());
            return ps.executeUpdate() > 0;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    /** Xóa cứng sản phẩm. Nếu muốn xóa mềm, dùng setActive(false) + updateProduct(). */
    public boolean deleteProduct(int productId) throws Exception {
        String sql = "DELETE FROM product WHERE productId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        }
    }

    // ─── HELPER ──────────────────────────────────────────────────────────────

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("productId"));
        p.setProductName(rs.getString("product_name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setImageUrl(rs.getString("imageUrl"));
        p.setActive(rs.getBoolean("isActive"));

        String catName = rs.getString("category_name");
        if (catName != null) {
            Category category = new Category();
            category.setCategoryId(rs.getInt("categoryId"));
            category.setCategoryName(catName);
            p.setCategory(category);
        }
        return p;
    }
}
package cafemanager.dao;

import cafemanager.model.Category;
import cafemanager.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // ─── READ ────────────────────────────────────────────────────────────────

    /** Lấy tất cả danh mục */
    public List<Category> getAllCategories() throws Exception {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT categoryId, category_name, description FROM category ORDER BY category_name";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("categoryId"));
                c.setCategoryName(rs.getString("category_name"));
                c.setDescription(rs.getString("description"));
                list.add(c);
            }
        }
        return list;
    }

    /** Tìm danh mục theo tên (LIKE, không phân biệt hoa thường) */
    public List<Category> searchByName(String keyword) throws Exception {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT categoryId, category_name, description FROM category "
                   + "WHERE category_name LIKE ? ORDER BY category_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category c = new Category();
                    c.setCategoryId(rs.getInt("categoryId"));
                    c.setCategoryName(rs.getString("category_name"));
                    c.setDescription(rs.getString("description"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    // ─── CREATE ──────────────────────────────────────────────────────────────

    /**
     * Thêm danh mục mới.
     * @return categoryId được DB sinh ra, hoặc -1 nếu thất bại.
     */
    public int addCategory(Category category) throws Exception {
        String sql = "INSERT INTO category (category_name, description) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
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

    /** Cập nhật tên và mô tả danh mục theo categoryId. */
    public boolean updateCategory(Category category) throws Exception {
        String sql = "UPDATE category SET category_name = ?, description = ? WHERE categoryId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());
            return ps.executeUpdate() > 0;
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    /**
     * Xóa danh mục theo categoryId.
     * Lưu ý: nên kiểm tra FK (product tham chiếu category) trước khi gọi.
     */
    public boolean deleteCategory(int categoryId) throws Exception {
        String sql = "DELETE FROM category WHERE categoryId = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Kiểm tra xem danh mục có đang được dùng bởi sản phẩm nào không. */
    public boolean isCategoryInUse(int categoryId) throws Exception {
        String sql = "SELECT COUNT(*) FROM product WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
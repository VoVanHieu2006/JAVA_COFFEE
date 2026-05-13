package cafemanager.dao;

import cafemanager.model.Category;
import cafemanager.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements IGenericDAO<Category> {

    @Override
    public boolean insert(Category category) throws Exception {
        return addCategory(category) > 0;
    }

    @Override
    public boolean update(Category category) throws Exception {
        return updateCategory(category);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return deleteCategory(id);
    }

    @Override
    public Category findById(int id) throws Exception {
        String sql = "SELECT category_id, category_name, description FROM Category WHERE category_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Category> findAll() throws Exception {
        return getAllCategories();
    }

    public List<Category> getAllCategories() throws Exception {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, category_name, description FROM Category ORDER BY category_name";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Category> searchByName(String keyword) throws Exception {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, category_name, description FROM Category "
                + "WHERE category_name LIKE ? ORDER BY category_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (keyword == null ? "" : keyword.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public int addCategory(Category category) throws Exception {
        String sql = "INSERT INTO Category (category_name, description) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public boolean updateCategory(Category category) throws Exception {
        String sql = "UPDATE Category SET category_name = ?, description = ? WHERE category_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteCategory(int categoryId) throws Exception {
        String sql = "DELETE FROM Category WHERE category_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean isCategoryInUse(int categoryId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Product WHERE category_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Category mapRow(ResultSet rs) throws Exception {
        Category c = new Category();
        c.setCategoryId(rs.getInt("category_id"));
        c.setCategoryName(rs.getString("category_name"));
        c.setDescription(rs.getString("description"));
        return c;
    }
}

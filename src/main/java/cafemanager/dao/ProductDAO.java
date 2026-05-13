package cafemanager.dao;

import cafemanager.model.Category;
import cafemanager.model.Product;
import cafemanager.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IGenericDAO<Product> {

    @Override
    public boolean insert(Product product) throws Exception {
        return addProduct(product) > 0;
    }

    @Override
    public boolean update(Product product) throws Exception {
        return updateProduct(product);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return deleteProduct(id);
    }

    @Override
    public Product findById(int id) throws Exception {
        String sql = baseSelect() + " WHERE p.product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        return getAllProducts();
    }

    public List<Product> getAllProducts() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY p.product_name";
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
        String sql = baseSelect() + " WHERE p.is_active = TRUE ORDER BY p.product_name";
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

    public List<Product> searchByName(String keyword) throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = baseSelect() + " WHERE p.is_active = TRUE AND p.product_name LIKE ? ORDER BY p.product_name";
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

    public List<Product> findByCategoryId(int categoryId) throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = baseSelect() + " WHERE p.is_active = TRUE AND p.category_id = ? ORDER BY p.product_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public int addProduct(Product product) throws Exception {
        if (isNameExistsInCategory(product.getProductName(), product.getCategoryId(), -1)) {
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại trong danh mục này.");
        }
        String sql = "INSERT INTO Product (category_id, product_name, price, description, is_active) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setBigDecimal(3, product.getPrice());
            ps.setString(4, product.getDescription());
            ps.setBoolean(5, product.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public boolean updateProduct(Product product) throws Exception {
        if (isNameExistsInCategory(product.getProductName(), product.getCategoryId(), product.getProductId())) {
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại trong danh mục này.");
        }
        String sql = "UPDATE Product SET product_name = ?, price = ?, category_id = ?, description = ?, is_active = ? "
                + "WHERE product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setBigDecimal(2, product.getPrice());
            ps.setInt(3, product.getCategoryId());
            ps.setString(4, product.getDescription());
            ps.setBoolean(5, product.isActive());
            ps.setInt(6, product.getProductId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(int productId) throws Exception {
        String sql = "UPDATE Product SET is_active = FALSE WHERE product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean isNameExistsInCategory(String name, int categoryId, int excludeId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Product WHERE LOWER(product_name) = LOWER(?) AND category_id = ? "
                + "AND (? = -1 OR product_id <> ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name == null ? "" : name.trim());
            ps.setInt(2, categoryId);
            ps.setInt(3, excludeId);
            ps.setInt(4, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private String baseSelect() {
        return "SELECT p.product_id, p.category_id, p.product_name, p.price, p.description, p.is_active, "
                + "c.category_id AS c_category_id, c.category_name, c.description AS c_description "
                + "FROM Product p LEFT JOIN Category c ON p.category_id = c.category_id";
    }

    private Product mapRow(ResultSet rs) throws Exception {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setProductName(rs.getString("product_name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setDescription(rs.getString("description"));
        p.setActive(rs.getBoolean("is_active"));
        String catName = rs.getString("category_name");
        if (catName != null) {
            Category category = new Category();
            category.setCategoryId(rs.getInt("c_category_id"));
            category.setCategoryName(catName);
            category.setDescription(rs.getString("c_description"));
            p.setCategory(category);
        }
        return p;
    }
}

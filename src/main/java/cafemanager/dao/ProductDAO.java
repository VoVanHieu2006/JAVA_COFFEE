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

public class ProductDAO {
    public List<Product> getAllProducts() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = """
                SELECT productId, product_name, price, isActive, category_id, imageUrl
                FROM product
                """;

        try (Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setProductName(rs.getString("product_name"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setActive(rs.getBoolean("isActive"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setImageUrl(rs.getString("imageUrl"));
                list.add(p);
            }
        }

        return list;
    }

    public List<Product> findAllActive() {
        List<Product> list = new ArrayList<>();
        String sql = """
                SELECT
                    p.productId,
                    p.product_name,
                    p.price,
                    p.category_id,
                    p.imageUrl,
                    p.isActive,
                    c.categoryId,
                    c.category_name
                FROM product p
                JOIN category c ON p.category_id = c.categoryId
                WHERE p.isActive = TRUE
                ORDER BY p.product_name;
                """;

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("productId"));
                p.setProductName(rs.getString("product_name"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setImageUrl(rs.getString("imageUrl"));
                p.setActive(rs.getBoolean("isActive"));

                Category category = new Category();
                category.setCategoryId(rs.getInt("categoryId"));
                category.setCategoryName(rs.getString("category_name"));
                p.setCategory(category);

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
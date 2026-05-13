package cafemanager.controller;

import cafemanager.dao.CategoryDAO;
import cafemanager.dao.ProductDAO;
import cafemanager.model.Category;
import cafemanager.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller cho module thực đơn.
 *
 * Controller chỉ xử lý nghiệp vụ và gọi DAO. Các việc thuộc giao diện như
 * JOptionPane, JTable, ComboBox và Dialog được đặt ở MenuPanel để giữ MVC sạch hơn.
 */
public class MenuController {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private Runnable dataChangedCallback;

    public void setDataChangedCallback(Runnable callback) {
        this.dataChangedCallback = callback;
    }

    /** Giữ lại tên hàm cũ để MainFrame/MenuPanel cũ vẫn không bị lỗi nếu còn gọi. */
    public void loadFunction(Runnable callback) {
        setDataChangedCallback(callback);
    }

    public List<Category> loadCategories() throws Exception {
        return categoryDAO.getAllCategories();
    }

    public List<Category> searchCategories(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return loadCategories();
        }
        return categoryDAO.searchByName(keyword.trim());
    }

    public int addCategory(String name) throws Exception {
        Category category = new Category();
        category.setCategoryName(name);
        int newId = categoryDAO.addCategory(category);
        notifyDataChanged();
        return newId;
    }

    public boolean updateCategory(int categoryId, String name) throws Exception {
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(name);
        boolean success = categoryDAO.updateCategory(category);
        if (success) {
            notifyDataChanged();
        }
        return success;
    }

    public boolean isCategoryInUse(int categoryId) throws Exception {
        return categoryDAO.isCategoryInUse(categoryId);
    }

    public boolean deleteCategory(int categoryId) throws Exception {
        boolean success = categoryDAO.deleteCategory(categoryId);
        if (success) {
            notifyDataChanged();
        }
        return success;
    }

    public List<Product> loadProducts() throws Exception {
        return productDAO.getAllProducts();
    }

    public Product findProductById(int productId) throws Exception {
        return productDAO.findById(productId);
    }

    public List<Product> searchProducts(String nameKeyword, String priceFrom, String priceTo, Category category) throws Exception {
        List<Product> allProducts = productDAO.getAllProducts();
        String keyword = normalizeKeyword(nameKeyword);
        BigDecimal from = parsePriceOrDefault(priceFrom, BigDecimal.ZERO);
        BigDecimal to = parsePriceOrDefault(priceTo, new BigDecimal("9999999999"));
        int categoryId = category == null ? 0 : category.getCategoryId();

        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException("Giá bắt đầu không được lớn hơn giá kết thúc.");
        }

        return allProducts.stream()
                .filter(p -> keyword == null || p.getProductName().toLowerCase().contains(keyword))
                .filter(p -> p.getPrice() != null)
                .filter(p -> p.getPrice().compareTo(from) >= 0)
                .filter(p -> p.getPrice().compareTo(to) <= 0)
                .filter(p -> categoryId == 0 || p.getCategoryId() == categoryId)
                .collect(Collectors.toList());
    }

    public int addProduct(Product product) throws Exception {
        int newId = productDAO.addProduct(product);
        if (newId > 0) {
            notifyDataChanged();
        }
        return newId;
    }

    public boolean updateProduct(Product product) throws Exception {
        boolean success = productDAO.updateProduct(product);
        if (success) {
            notifyDataChanged();
        }
        return success;
    }

    /** Xóa mềm sản phẩm để bảo toàn lịch sử hóa đơn. */
    public boolean hideProduct(int productId) throws Exception {
        boolean success = productDAO.deleteProduct(productId);
        if (success) {
            notifyDataChanged();
        }
        return success;
    }

    /** Dùng cho combobox lọc sản phẩm. */
    public List<Category> buildCategoryFilterItems(List<Category> categories) {
        List<Category> items = new ArrayList<>();
        items.add(new Category(0, "-- Tất cả --"));
        if (categories != null) {
            items.addAll(categories);
        }
        return items;
    }

    private String normalizeKeyword(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return text.trim().toLowerCase();
    }

    private BigDecimal parsePriceOrDefault(String text, BigDecimal defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return new BigDecimal(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Khoảng giá phải là số hợp lệ.");
        }
    }

    private void notifyDataChanged() {
        if (dataChangedCallback != null) {
            dataChangedCallback.run();
        }
    }
}

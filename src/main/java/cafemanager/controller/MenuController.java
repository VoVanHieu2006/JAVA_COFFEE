package cafemanager.controller;

import cafemanager.dao.CategoryDAO;
import cafemanager.dao.ProductDAO;
import cafemanager.model.Category;
import cafemanager.model.Product;
import cafemanager.view.AddEditDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MenuController — xử lý toàn bộ logic CRUD cho sản phẩm và danh mục.
 * MenuPanel chỉ gọi các method của controller; không chứa logic nghiệp vụ.
 */
public class MenuController {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    /** Cache danh sách category để truyền vào AddEditDialog không cần query lại. */
    private List<Category> categoryCache = new ArrayList<>();

    // ═══════════════════════════════════════════════════════════════════════
    // CATEGORY — load / search / add / edit / delete
    // ═══════════════════════════════════════════════════════════════════════
    
    private Runnable loadPOS; 
    public void loadFunction(Runnable onSuccessCallback){
        loadPOS = onSuccessCallback;
    }
    
    

    /** Load toàn bộ danh mục vào tbCategories và cbbCategory (dùng cho filter product). */
    public void loadCategories(DefaultTableModel catModel, JComboBox<Category> cbbCategory) {
        try {
            categoryCache = categoryDAO.getAllCategories();
            refreshCatTable(catModel, categoryCache);
            refreshCatCombo(cbbCategory, categoryCache);
        } catch (Exception e) {
            showError("Không thể tải danh mục: " + e.getMessage());
        }
    }

    /** Tìm kiếm danh mục theo tên và cập nhật tbCategories. */
    public void searchCategories(String keyword, DefaultTableModel catModel) {
        try {
            List<Category> result = categoryDAO.searchByName(keyword.trim());
            refreshCatTable(catModel, result);
        } catch (Exception e) {
            showError("Lỗi tìm kiếm danh mục: " + e.getMessage());
        }
    }

    /** Thêm danh mục mới qua InputDialog. */
    public void addCategory(JComponent parent,
                            DefaultTableModel catModel,
                            JComboBox<Category> cbbCategory) {
        String name = JOptionPane.showInputDialog(parent, "Tên danh mục mới:",
                "Thêm Danh mục", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.isBlank()) return;

        try {
            Category cat = new Category();
            cat.setCategoryName(name.trim());
            int newId = categoryDAO.addCategory(cat);
            if (newId > 0) {
                JOptionPane.showMessageDialog(parent, "Thêm danh mục thành công!");
                loadCategories(catModel, cbbCategory);
            }
        } catch (Exception e) {
            showError("Lỗi thêm danh mục: " + e.getMessage());
        }
    }

    /** Sửa danh mục đang chọn trong bảng. */
    public void editCategory(JComponent parent,
                             int selectedRow,
                             DefaultTableModel catModel,
                             DefaultTableModel prodModel,
                             JComboBox<Category> cbbCategory) {
        if (selectedRow < 0) {
            showWarn("Vui lòng chọn danh mục cần sửa.");
            return;
        }

        int categoryId = (int) catModel.getValueAt(selectedRow, 0);
        String currentName = (String) catModel.getValueAt(selectedRow, 1);

        String newName = (String) JOptionPane.showInputDialog(parent,
                "Tên danh mục:", "Sửa Danh mục",
                JOptionPane.PLAIN_MESSAGE, null, null, currentName);
        if (newName == null || newName.isBlank()) return;

        try {
            Category cat = new Category();
            cat.setCategoryId(categoryId);
            cat.setCategoryName(newName.trim());
            if (categoryDAO.updateCategory(cat)) {
                JOptionPane.showMessageDialog(parent, "Cập nhật danh mục thành công!");
                loadCategories(catModel, cbbCategory);
                loadProducts(prodModel);
                if (loadPOS != null) loadPOS.run();
            }
        } catch (Exception e) {
            showError("Lỗi sửa danh mục: " + e.getMessage());
        }
    }

    /** Xóa danh mục đang chọn (có kiểm tra FK). */
    public void deleteCategory(JComponent parent,
                               int selectedRow,
                               DefaultTableModel catModel,
                               JComboBox<Category> cbbCategory) {
        if (selectedRow < 0) {
            showWarn("Vui lòng chọn danh mục cần xóa.");
            return;
        }

        int categoryId = (int) catModel.getValueAt(selectedRow, 0);
        String catName = (String) catModel.getValueAt(selectedRow, 1);

        try {
            if (categoryDAO.isCategoryInUse(categoryId)) {
                showWarn("Danh mục \"" + catName + "\" đang được dùng bởi sản phẩm, không thể xóa.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(parent,
                    "Xóa danh mục \"" + catName + "\"?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            if (categoryDAO.deleteCategory(categoryId)) {
                JOptionPane.showMessageDialog(parent, "Đã xóa danh mục.");
                loadCategories(catModel, cbbCategory);
            }
        } catch (Exception e) {
            showError("Lỗi xóa danh mục: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRODUCT — load / search / openAdd / openEdit / delete
    // ═══════════════════════════════════════════════════════════════════════

    /** Load toàn bộ sản phẩm vào tbProducts. */
    public void loadProducts(DefaultTableModel prodModel) {
        try {
            List<Product> products = productDAO.getAllProducts();
            refreshProdTable(prodModel, products);
        } catch (Exception e) {
            showError("Không thể tải sản phẩm: " + e.getMessage());
        }
    }

    /**
     * Lọc sản phẩm theo tên, khoảng giá và category.
     * Gọi bất cứ khi nào các trường filter thay đổi.
     *
     * @param nameKeyword  tìm theo tên (rỗng = bỏ qua)
     * @param priceFrom    giá tối thiểu dạng String (rỗng = bỏ qua)
     * @param priceTo      giá tối đa dạng String (rỗng = bỏ qua)
     * @param category     danh mục lọc (null hoặc id=0 = tất cả)
     * @param prodModel    model của tbProducts để cập nhật
     */
    public void searchProducts(String nameKeyword, String priceFrom, String priceTo, Category category, DefaultTableModel prodModel) {
        try {
            List<Product> all = productDAO.getAllProducts();

            // 1. TƯ DUY TỪ 0 ĐẾN +VÔ CÙNG
            // Khởi tạo mặc định dải giá rộng nhất
            BigDecimal fromPrice = BigDecimal.ZERO; // Bắt đầu từ 0
            BigDecimal toPrice = new BigDecimal("9999999999"); // Đến 9 tỷ (+ vô cùng)

            // Cập nhật lại From nếu người dùng có nhập
            if (priceFrom != null && !priceFrom.isBlank()) {
                try {
                    fromPrice = new BigDecimal(priceFrom.trim());
                } catch (NumberFormatException ignored) { } // Lỗi thì giữ nguyên là 0
            }

            // Cập nhật lại To nếu người dùng có nhập
            if (priceTo != null && !priceTo.isBlank()) {
                try {
                    toPrice = new BigDecimal(priceTo.trim());
                } catch (NumberFormatException ignored) { } // Lỗi thì giữ nguyên là 9 tỷ
            }

            String kw = (nameKeyword != null && !nameKeyword.isBlank()) ? nameKeyword.trim().toLowerCase() : null;
            int catIdFilter = (category != null) ? category.getCategoryId() : 0;

            // Lưu final để dùng trong stream
            final BigDecimal finalFrom = fromPrice;
            final BigDecimal finalTo = toPrice;

            // 2. LỌC SIÊU GỌN VÀ NHANH
            List<Product> filtered = all.stream().filter(p -> {

                // Lọc tên
                if (kw != null && p.getProductName() != null) {
                    if (!p.getProductName().toLowerCase().contains(kw)) return false;
                }

                // Bỏ qua sp lỗi không có giá trong Database
                if (p.getPrice() == null) return false;

                // Lọc dải giá (Bây giờ chỉ cần 2 dòng này thôi, không cần quan tâm rỗng hay không nữa)
                if (p.getPrice().compareTo(finalFrom) < 0) return false; 
                if (p.getPrice().compareTo(finalTo) > 0) return false;   

                // Lọc danh mục
                if (catIdFilter != 0 && p.getCategoryId() != catIdFilter) {
                    return false;
                }

                return true;
            }).collect(Collectors.toList());

            // 3. Xóa data cũ và đẩy lên bảng
            prodModel.setRowCount(0);
            refreshProdTable(prodModel, filtered);

        } catch (Exception e) {
            showError("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
        }
    }

    /**
     * Mở dialog AddEditPanel để THÊM sản phẩm mới.
     * Sau khi user bấm Lưu, tự động reload prodModel.
     *
     * @param parentFrame  JFrame cha (để modal đúng)
     * @param prodModel    model của tbProducts
     */
    public void openAddProductDialog(java.awt.Frame parentFrame,
                                     DefaultTableModel prodModel) {
        ensureCategoryCache();
        if (categoryCache.isEmpty()) return;

        AddEditDialog dialog = new AddEditDialog(parentFrame, categoryCache);
        dialog.setVisible(true); // blocks vì modal=true

        Product result = dialog.getResult();
        if (result == null) return; // user bấm Cancel

        try {
            int newId = productDAO.addProduct(result);
            if (newId > 0) {
                JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công!");
                loadProducts(prodModel);
            }
            if (loadPOS != null) loadPOS.run();
        } catch (Exception e) {
            showError("Lỗi thêm sản phẩm: " + e.getMessage());
        }
    }

    /**
     * Mở dialog AddEditDialog để SỬA sản phẩm đang chọn trong bảng.
     *
     * @param parentFrame  JFrame cha
     * @param selectedRow  hàng đang chọn trong tbProducts (view index)
     * @param prodModel    model của tbProducts
     */
    public void openEditProductDialog(java.awt.Frame parentFrame,
                                      int selectedRow,
                                      DefaultTableModel prodModel) {
        if (selectedRow < 0) {
            showWarn("Vui lòng chọn sản phẩm cần sửa.");
            return;
        }
        ensureCategoryCache();
        if (categoryCache.isEmpty()) return;

        // Đọc dữ liệu từ bảng để dựng Product hiện tại
        int productId   = (int)    prodModel.getValueAt(selectedRow, 0);
        String name     = (String) prodModel.getValueAt(selectedRow, 1);
        String catName  = (String) prodModel.getValueAt(selectedRow, 2);
        Object priceObj =          prodModel.getValueAt(selectedRow, 3);
        String status   = (String) prodModel.getValueAt(selectedRow, 4);

        // Resolve categoryId từ cache theo tên
        int categoryId = categoryCache.stream()
                .filter(c -> c.getCategoryName().equals(catName))
                .mapToInt(Category::getCategoryId)
                .findFirst()
                .orElse(-1);

        Product existing = new Product();
        existing.setProductId(productId);
        existing.setProductName(name);
        existing.setPrice(priceObj instanceof BigDecimal
                ? (BigDecimal) priceObj
                : new BigDecimal(priceObj.toString()));
        existing.setCategoryId(categoryId);
        existing.setActive("Active".equals(status));

        AddEditDialog dialog = new AddEditDialog(parentFrame, categoryCache, existing);
        dialog.setVisible(true); // blocks

        Product result = dialog.getResult();
        if (result == null) return; // user bấm Cancel

        try {
            if (productDAO.updateProduct(result)) {
                JOptionPane.showMessageDialog(null, "Cập nhật sản phẩm thành công!");
                loadProducts(prodModel);
                if (loadPOS != null) loadPOS.run();
            }
        } catch (Exception e) {
            showError("Lỗi sửa sản phẩm: " + e.getMessage());
        }
    }

    /**
     * Xóa sản phẩm đang chọn trong bảng.
     * Đọc productId TRƯỚC khi hỏi confirm để tránh lỗi race condition.
     */
    public void deleteProduct(JComponent parent,
                              int selectedRow,
                              DefaultTableModel prodModel) {
        if (selectedRow < 0) {
            showWarn("Vui lòng chọn sản phẩm cần xóa.");
            return;
        }

        // Đọc ngay, trước mọi thao tác khác
        int productId   = (int)    prodModel.getValueAt(selectedRow, 0);
        String prodName = (String) prodModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(parent,
                "Xóa sản phẩm \"" + prodName + "\"?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            if (productDAO.deleteProduct(productId)) {
                JOptionPane.showMessageDialog(parent, "Đã xóa sản phẩm.");
                loadProducts(prodModel);
                if (loadPOS != null) loadPOS.run();
            }
        } catch (Exception e) {
            showError("Lỗi xóa sản phẩm: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════════════════

    private void ensureCategoryCache() {
        if (categoryCache.isEmpty()) {
            try { categoryCache = categoryDAO.getAllCategories(); }
            catch (Exception e) { showError("Không thể tải danh mục."); }
        }
    }

    private void refreshCatTable(DefaultTableModel model, List<Category> list) {
        model.setRowCount(0);
        for (Category c : list) {
            model.addRow(new Object[]{c.getCategoryId(), c.getCategoryName()});
        }
    }

    private void refreshCatCombo(JComboBox<Category> combo, List<Category> list) {
        combo.removeAllItems();
        // Item đầu tiên: "Tất cả" để filter hiển thị tất cả sản phẩm
        combo.addItem(new Category(0, "-- Tất cả --"));
        for (Category c : list) {
            combo.addItem(c);
        }
    }

    private void refreshProdTable(DefaultTableModel model, List<Product> list) {
        model.setRowCount(0);
        for (Product p : list) {
            String catName = (p.getCategory() != null) ? p.getCategory().getCategoryName() : "";
            String status  = p.isActive() ? "Active" : "Inactive";
            model.addRow(new Object[]{
                p.getProductId(),
                p.getProductName(),
                catName,
                p.getPrice(),
                status
            });
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
}
package cafemanager.view;

import cafemanager.controller.MenuController;
import cafemanager.model.Category;
import cafemanager.model.Product;
import java.awt.Frame;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Panel quản lý thực đơn.
 *
 * View chịu trách nhiệm hiển thị JTable/JComboBox/Dialog. Controller chỉ trả dữ liệu
 * và thực hiện nghiệp vụ thông qua DAO. Cách tách này giúp module bám MVC hơn.
 */
public class MenuPanel extends javax.swing.JPanel {

    private final MenuController controller = new MenuController();
    private DefaultTableModel catModel;
    private DefaultTableModel prodModel;
    private List<Category> categoryCache = new ArrayList<>();

    public MenuPanel() {
        initComponents();
        initModels();
        initEvents();
        loadAll();
    }

    public void setOnDataChangedCallback(Runnable callback) {
        controller.setDataChangedCallback(callback);
    }

    private void initModels() {
        catModel = (DefaultTableModel) tbCategories.getModel();
        prodModel = (DefaultTableModel) tbProducts.getModel();

        cbbCategory.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Category) {
                    setText(((Category) value).getCategoryName());
                }
                return this;
            }
        });
    }

    private void initEvents() {
        btAddCat.addActionListener(e -> addCategory());
        btEditCat.addActionListener(e -> editCategory());
        btDelCat.addActionListener(e -> deleteCategory());

        tfSearchByCategory.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { searchCategory(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { searchCategory(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { searchCategory(); }
        });

        btAddProduct.addActionListener(e -> addProduct());
        btEditProduct.addActionListener(e -> editProduct());
        btDelProduct.addActionListener(e -> hideProduct());

        javax.swing.event.DocumentListener productSearchListener = new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { searchProducts(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { searchProducts(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { searchProducts(); }
        };
        tfProductName.getDocument().addDocumentListener(productSearchListener);
        tfPriceFrom.getDocument().addDocumentListener(productSearchListener);
        tfPriceTo.getDocument().addDocumentListener(productSearchListener);
        cbbCategory.addActionListener(e -> searchProducts());
    }

    private void loadAll() {
        loadCategories();
        loadProducts();
    }

    private void loadCategories() {
        try {
            categoryCache = controller.loadCategories();
            displayCategories(categoryCache);
            displayCategoryFilter(categoryCache);
        } catch (Exception ex) {
            showError("Không thể tải danh mục: " + ex.getMessage());
        }
    }

    private void searchCategory() {
        try {
            displayCategories(controller.searchCategories(tfSearchByCategory.getText()));
        } catch (Exception ex) {
            showError("Lỗi tìm kiếm danh mục: " + ex.getMessage());
        }
    }

    private void loadProducts() {
        try {
            displayProducts(controller.loadProducts());
        } catch (Exception ex) {
            showError("Không thể tải sản phẩm: " + ex.getMessage());
        }
    }

    private void searchProducts() {
        try {
            displayProducts(controller.searchProducts(
                    tfProductName.getText(),
                    tfPriceFrom.getText(),
                    tfPriceTo.getText(),
                    getSelectedFilterCategory()));
        } catch (Exception ex) {
            showError("Lỗi tìm kiếm sản phẩm: " + ex.getMessage());
        }
    }

    private void addCategory() {
        String name = JOptionPane.showInputDialog(this, "Tên danh mục mới:",
                "Thêm danh mục", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.isBlank()) {
            return;
        }
        try {
            if (controller.addCategory(name.trim()) > 0) {
                showInfo("Thêm danh mục thành công.");
                loadCategories();
                loadProducts();
            }
        } catch (Exception ex) {
            showError("Lỗi thêm danh mục: " + ex.getMessage());
        }
    }

    private void editCategory() {
        int modelRow = getSelectedModelRow(tbCategories);
        if (modelRow < 0) {
            showWarning("Vui lòng chọn danh mục cần sửa.");
            return;
        }
        int categoryId = ((Number) catModel.getValueAt(modelRow, 0)).intValue();
        String currentName = String.valueOf(catModel.getValueAt(modelRow, 1));
        String newName = (String) JOptionPane.showInputDialog(this,
                "Tên danh mục:", "Sửa danh mục",
                JOptionPane.PLAIN_MESSAGE, null, null, currentName);
        if (newName == null || newName.isBlank()) {
            return;
        }
        try {
            if (controller.updateCategory(categoryId, newName.trim())) {
                showInfo("Cập nhật danh mục thành công.");
                loadCategories();
                loadProducts();
            }
        } catch (Exception ex) {
            showError("Lỗi sửa danh mục: " + ex.getMessage());
        }
    }

    private void deleteCategory() {
        int modelRow = getSelectedModelRow(tbCategories);
        if (modelRow < 0) {
            showWarning("Vui lòng chọn danh mục cần xóa.");
            return;
        }
        int categoryId = ((Number) catModel.getValueAt(modelRow, 0)).intValue();
        String categoryName = String.valueOf(catModel.getValueAt(modelRow, 1));
        try {
            if (controller.isCategoryInUse(categoryId)) {
                showWarning("Danh mục \"" + categoryName + "\" đang được dùng bởi sản phẩm, không thể xóa.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xóa danh mục \"" + categoryName + "\"?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            if (controller.deleteCategory(categoryId)) {
                showInfo("Đã xóa danh mục.");
                loadCategories();
            }
        } catch (Exception ex) {
            showError("Lỗi xóa danh mục: " + ex.getMessage());
        }
    }

    private void addProduct() {
        if (!ensureCategoryCache()) {
            return;
        }
        AddEditDialog dialog = new AddEditDialog(getParentFrame(), categoryCache);
        dialog.setVisible(true);
        Product result = dialog.getResult();
        if (result == null) {
            return;
        }
        try {
            if (controller.addProduct(result) > 0) {
                showInfo("Thêm sản phẩm thành công.");
                loadProducts();
            }
        } catch (Exception ex) {
            showError("Lỗi thêm sản phẩm: " + ex.getMessage());
        }
    }

    private void editProduct() {
        int modelRow = getSelectedModelRow(tbProducts);
        if (modelRow < 0) {
            showWarning("Vui lòng chọn sản phẩm cần sửa.");
            return;
        }
        if (!ensureCategoryCache()) {
            return;
        }
        int productId = ((Number) prodModel.getValueAt(modelRow, 0)).intValue();
        try {
            Product existing = controller.findProductById(productId);
            if (existing == null) {
                showWarning("Không tìm thấy sản phẩm cần sửa.");
                return;
            }
            AddEditDialog dialog = new AddEditDialog(getParentFrame(), categoryCache, existing);
            dialog.setVisible(true);
            Product result = dialog.getResult();
            if (result == null) {
                return;
            }
            if (controller.updateProduct(result)) {
                showInfo("Cập nhật sản phẩm thành công.");
                loadProducts();
            }
        } catch (Exception ex) {
            showError("Lỗi sửa sản phẩm: " + ex.getMessage());
        }
    }

    private void hideProduct() {
        int modelRow = getSelectedModelRow(tbProducts);
        if (modelRow < 0) {
            showWarning("Vui lòng chọn sản phẩm cần ẩn.");
            return;
        }
        int productId = ((Number) prodModel.getValueAt(modelRow, 0)).intValue();
        String productName = String.valueOf(prodModel.getValueAt(modelRow, 1));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Sản phẩm \"" + productName + "\" sẽ bị ẩn khỏi thực đơn. Lịch sử hóa đơn vẫn được giữ. Tiếp tục?",
                "Xác nhận ẩn sản phẩm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (controller.hideProduct(productId)) {
                showInfo("Sản phẩm đã được ẩn khỏi thực đơn.");
                loadProducts();
            }
        } catch (Exception ex) {
            showError("Lỗi ẩn sản phẩm: " + ex.getMessage());
        }
    }

    private void displayCategories(List<Category> categories) {
        catModel.setRowCount(0);
        for (Category category : categories) {
            catModel.addRow(new Object[]{category.getCategoryId(), category.getCategoryName()});
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void displayCategoryFilter(List<Category> categories) {
        javax.swing.JComboBox combo = cbbCategory;
        combo.removeAllItems();
        for (Category category : controller.buildCategoryFilterItems(categories)) {
            combo.addItem(category);
        }
    }

    private void displayProducts(List<Product> products) {
        prodModel.setRowCount(0);
        for (Product product : products) {
            String categoryName = product.getCategory() == null ? "" : product.getCategory().getCategoryName();
            prodModel.addRow(new Object[]{
                product.getProductId(),
                product.getProductName(),
                categoryName,
                product.getPrice(),
                product.isActive() ? "Active" : "Inactive"
            });
        }
    }

    private boolean ensureCategoryCache() {
        if (categoryCache == null || categoryCache.isEmpty()) {
            loadCategories();
        }
        if (categoryCache == null || categoryCache.isEmpty()) {
            showWarning("Cần có ít nhất một danh mục trước khi thêm sản phẩm.");
            return false;
        }
        return true;
    }

    private Category getSelectedFilterCategory() {
        Object selected = cbbCategory.getSelectedItem();
        if (selected instanceof Category) {
            Category category = (Category) selected;
            return category.getCategoryId() == 0 ? null : category;
        }
        return null;
    }

    private int getSelectedModelRow(javax.swing.JTable table) {
        int viewRow = table.getSelectedRow();
        return viewRow < 0 ? -1 : table.convertRowIndexToModel(viewRow);
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbProducts = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btAddProduct = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCategories = new javax.swing.JTable();
        tfSearchByCategory = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btAddCat = new javax.swing.JButton();
        btEditCat = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btDelCat = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lbCategory = new javax.swing.JLabel();
        cbbCategory = new javax.swing.JComboBox<>();
        lbPrice = new javax.swing.JLabel();
        lbProductName = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tfPriceFrom = new javax.swing.JTextField();
        tfPriceTo = new javax.swing.JTextField();
        tfProductName = new javax.swing.JTextField();
        btEditProduct = new javax.swing.JButton();
        btDelProduct = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setPreferredSize(new java.awt.Dimension(850, 450));
        setLayout(new java.awt.GridBagLayout());

        tbProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Category", "Price", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbProducts);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 864;
        gridBagConstraints.ipady = 230;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        add(jScrollPane3, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2, 1, 1));

        jPanel3.setLayout(new java.awt.GridLayout(1, 3, 1, 1));
        jPanel1.add(jPanel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.ipadx = 850;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jPanel1, gridBagConstraints);

        btAddProduct.setBackground(new java.awt.Color(0, 153, 51));
        btAddProduct.setForeground(new java.awt.Color(255, 255, 255));
        btAddProduct.setText("Add Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 40, 8, 0);
        add(btAddProduct, gridBagConstraints);

        tbCategories.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Category Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbCategories);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 384;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jScrollPane2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 206;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(tfSearchByCategory, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Search Category:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(jLabel1, gridBagConstraints);

        btAddCat.setBackground(new java.awt.Color(0, 153, 51));
        btAddCat.setForeground(new java.awt.Color(255, 255, 255));
        btAddCat.setText("Add Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 20, 8, 0);
        add(btAddCat, gridBagConstraints);

        btEditCat.setBackground(new java.awt.Color(0, 153, 153));
        btEditCat.setForeground(new java.awt.Color(255, 255, 255));
        btEditCat.setText("Edit Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 20, 8, 0);
        add(btEditCat, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setText("CATEGORY");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 51));
        jLabel5.setText("PRODUCT");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        add(jLabel5, gridBagConstraints);

        btDelCat.setBackground(new java.awt.Color(255, 51, 51));
        btDelCat.setForeground(new java.awt.Color(255, 255, 255));
        btDelCat.setText("Delete Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 8, 0);
        add(btDelCat, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCategory.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        lbCategory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbCategory.setText("Category");
        jPanel4.add(lbCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, 90, 20));

        cbbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(cbbCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 100, -1));

        lbPrice.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        lbPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPrice.setText("Price");
        jPanel4.add(lbPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 80, 20));

        lbProductName.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        lbProductName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbProductName.setText("Product Name");
        jPanel4.add(lbProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 130, 20));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("Search Product By:");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("-->");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, -1, -1));

        jLabel3.setText("From");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, -1, -1));

        jLabel7.setText("To");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, -1, -1));
        jPanel4.add(tfPriceFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 80, -1));
        jPanel4.add(tfPriceTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 80, -1));
        jPanel4.add(tfProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 120, -1));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 48;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jPanel4, gridBagConstraints);

        btEditProduct.setBackground(new java.awt.Color(0, 153, 153));
        btEditProduct.setForeground(new java.awt.Color(255, 255, 255));
        btEditProduct.setText("Edit Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 40, 8, 0);
        add(btEditProduct, gridBagConstraints);

        btDelProduct.setBackground(new java.awt.Color(255, 0, 0));
        btDelProduct.setForeground(new java.awt.Color(255, 255, 255));
        btDelProduct.setText("Delete Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 8, 0);
        add(btDelProduct, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddCat;
    private javax.swing.JButton btAddProduct;
    private javax.swing.JButton btDelCat;
    private javax.swing.JButton btDelProduct;
    private javax.swing.JButton btEditCat;
    private javax.swing.JButton btEditProduct;
    private javax.swing.JComboBox<String> cbbCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbCategory;
    private javax.swing.JLabel lbPrice;
    private javax.swing.JLabel lbProductName;
    private javax.swing.JTable tbCategories;
    private javax.swing.JTable tbProducts;
    private javax.swing.JTextField tfPriceFrom;
    private javax.swing.JTextField tfPriceTo;
    private javax.swing.JTextField tfProductName;
    private javax.swing.JTextField tfSearchByCategory;
    // End of variables declaration//GEN-END:variables
}

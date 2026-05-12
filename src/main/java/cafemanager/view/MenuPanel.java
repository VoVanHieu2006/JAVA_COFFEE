package cafemanager.view;
 
import cafemanager.controller.MenuController;
import cafemanager.model.Category;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
 
/**
 * MenuPanel — chỉ chứa code giao diện và gọi MenuController.
 * Toàn bộ logic nghiệp vụ nằm trong MenuController.
 *
 *   - tfProductName  : filter tên sản phẩm   (SEARCH, không dùng để Add/Edit)
 *   - tfPrice        : filter giá "from"      (SEARCH)
 *   - tfPriceTo      : filter giá "to"        (SEARCH)
 *   - cbbCategory    : filter theo danh mục   (SEARCH, "-- Tất cả --" = không lọc)
 *
 * Add / Edit mở AddEditPanel dialog riêng.
 */


public class MenuPanel extends javax.swing.JPanel {

        
    
    private final MenuController controller = new MenuController();
    private Runnable onDataChangedCallback;
 
    public void setOnDataChangedCallback(Runnable callback) {
        this.onDataChangedCallback = callback;
        controller.loadFunction(this.onDataChangedCallback);
    }
    
    
    
 
    private DefaultTableModel catModel;
    private DefaultTableModel prodModel;
 
    public MenuPanel() {
        initComponents();
        initModels();
        initEvents();
        loadAll();
    }
 
    // ─── Khởi tạo model ──────────────────────────────────────────────────────
 
    private void initModels() {
        
        
        
        catModel = (DefaultTableModel) tbCategories.getModel();
        prodModel = (DefaultTableModel) tbProducts.getModel();
 
        // Renderer cho cbbCategory (hiển thị tên category)
        cbbCategory.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Category) setText(((Category) value).getCategoryName());
                return this;
            }
        });
    }
 
    // ─── Đăng ký sự kiện ────────────────────────────────────────────────────
 
    private void initEvents() {
 
        // ── Category ──────────────────────────────────────────────────────
        btAddCat.addActionListener(e ->
            controller.addCategory(this, catModel, getCbbCategoryTyped())
        );
 
        btEditCat.addActionListener(e ->
            controller.editCategory(this,
                tbCategories.getSelectedRow(),
                catModel, prodModel,
                getCbbCategoryTyped())
        );
 
        btDelCat.addActionListener(e ->
            controller.deleteCategory(this,
                tbCategories.getSelectedRow(), catModel, getCbbCategoryTyped())
        );
 
        // Tìm kiếm danh mục theo tên khi gõ
        tfSearchByCategory.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { searchCat(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { searchCat(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { searchCat(); }
            private void searchCat() {
                String kw = tfSearchByCategory.getText();
                if (kw.isBlank()) controller.loadCategories(catModel, getCbbCategoryTyped());
                else              controller.searchCategories(kw, catModel);
            }
        });
 
        // ── Product buttons ──────────────────────────────────────────────
        // Add: mở AddEditPanel dialog
        btAddProduct.addActionListener(e -> {
            java.awt.Frame parentFrame = (java.awt.Frame)
                    SwingUtilities.getWindowAncestor(this);
            controller.openAddProductDialog(parentFrame, prodModel);
        });
 
        // Edit: mở AddEditPanel dialog với data hàng đang chọn
        btEditProduct.addActionListener(e -> {
            java.awt.Frame parentFrame = (java.awt.Frame)
                    SwingUtilities.getWindowAncestor(this);
            controller.openEditProductDialog(parentFrame,
                    tbProducts.getSelectedRow(), prodModel);
        });
 
        // Delete
        btDelProduct.addActionListener(e ->
            controller.deleteProduct(this,
                    tbProducts.getSelectedRow(), prodModel)
        );
 
        // ── Product search (realtime khi thay đổi bất kỳ trường nào) ────
        javax.swing.event.DocumentListener searchListener =
                new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { doSearchProduct(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { doSearchProduct(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearchProduct(); }
        };
 
        tfProductName.getDocument().addDocumentListener(searchListener);
        tfPriceTo.getDocument().addDocumentListener(searchListener);
        tfPriceFrom.getDocument().addDocumentListener(searchListener);
 
        // ComboBox category filter
        cbbCategory.addActionListener(e -> doSearchProduct());
    }
 
    /** Gọi controller.searchProducts với các giá trị filter hiện tại. */
    private void doSearchProduct() {
        String name      = tfProductName.getText();
        String priceFrom = tfPriceFrom.getText();
        String priceTo   = tfPriceTo.getText();
        Category cat     = getSelectedFilterCategory();
        
        controller.searchProducts(name, priceFrom, priceTo, cat, prodModel);
    }
 
    // ─── Load dữ liệu ban đầu ────────────────────────────────────────────────
 
    private void loadAll() {
        controller.loadCategories(catModel, getCbbCategoryTyped());
        controller.loadProducts(prodModel);
    }
 
    // ─── Helper cast ─────────────────────────────────────────────────────────
 
    @SuppressWarnings("unchecked")
    private JComboBox<Category> getCbbCategoryTyped() {
        return (JComboBox<Category>) (JComboBox<?>) cbbCategory;
    }
 
    /**
     * Trả về Category đang chọn trong filter combo.
     * Nếu là "-- Tất cả --" (id=0) hoặc null → trả về null (không lọc theo category).
     */
    private Category getSelectedFilterCategory() {
        Object selected = cbbCategory.getSelectedItem();
        if (selected instanceof Category) {
            Category c = (Category) selected;
            return c.getCategoryId() == 0 ? null : c;
        }
        return null;
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

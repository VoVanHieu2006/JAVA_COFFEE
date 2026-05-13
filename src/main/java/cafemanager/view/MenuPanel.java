package cafemanager.view;

import cafemanager.controller.MenuController;
import cafemanager.model.Category;
import cafemanager.model.Product;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class MenuPanel extends javax.swing.JPanel {

    private final MenuController controller = new MenuController();
    private final javax.swing.JButton btRefreshProduct = new javax.swing.JButton("Làm mới");
    private DefaultTableModel catModel;
    private DefaultTableModel prodModel;
    private List<Category> categoryCache = new ArrayList<>();
    private List<Category> displayedCategories = new ArrayList<>();
    private List<Product> displayedProducts = new ArrayList<>();

    private JTable tbProducts;
    private JTable tbCategories;
    private JScrollPane spProducts;
    private JScrollPane spCategories;
    private JTextField tfProductName;
    private JTextField tfPriceFrom;
    private JTextField tfPriceTo;
    private JComboBox<Category> cbbCategory;
    private JTextField tfSearchByCategory;

    private javax.swing.JButton btAddProduct;
    private javax.swing.JButton btEditProduct;
    private javax.swing.JButton btDelProduct;
    private javax.swing.JButton btAddCat;
    private javax.swing.JButton btEditCat;
    private javax.swing.JButton btDelCat;

    private JLabel lbProductTitle;
    private JLabel lbCategoryTitle;
    private JLabel lbProductName;
    private JLabel lbPriceFrom;
    private JLabel lbPriceTo;
    private JLabel lbCategory;
    private JLabel lbSearchCategory;

    public MenuPanel() {
        initComponentsRuntime();
        buildLayout();
        applyStyles();
        initModels();
        initEvents();
        loadAll();
    }

    private void initComponentsRuntime() {
        setPreferredSize(new Dimension(950, 560));

        tbProducts = new JTable();
        tbProducts.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"STT", "Tên món", "Danh mục", "Giá", "Trạng thái"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        spProducts = new JScrollPane(tbProducts);

        tbCategories = new JTable();
        tbCategories.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"STT", "Tên danh mục"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        spCategories = new JScrollPane(tbCategories);

        tfProductName = new JTextField();
        tfPriceFrom = new JTextField();
        tfPriceTo = new JTextField();
        cbbCategory = new JComboBox<>();
        tfSearchByCategory = new JTextField();

        btAddProduct = new javax.swing.JButton("Thêm món");
        btEditProduct = new javax.swing.JButton("Sửa món");
        btDelProduct = new javax.swing.JButton("Xóa món");
        btAddCat = new javax.swing.JButton("Thêm");
        btEditCat = new javax.swing.JButton("Sửa");
        btDelCat = new javax.swing.JButton("Xóa");

        lbProductTitle = new JLabel("QUẢN LÝ SẢN PHẨM");
        lbCategoryTitle = new JLabel("QUẢN LÝ DANH MỤC");
        lbProductName = new JLabel("Tên món:");
        lbPriceFrom = new JLabel("Giá từ:");
        lbPriceTo = new JLabel("Giá đến:");
        lbCategory = new JLabel("Danh mục:");
        lbSearchCategory = new JLabel("Tìm kiếm danh mục:");
    }

    private void buildLayout() {
        removeAll();
        setLayout(new BorderLayout());

        JPanel productSection = buildProductSection();
        JPanel categorySection = buildCategorySection();

        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(
                javax.swing.JSplitPane.HORIZONTAL_SPLIT, productSection, categorySection);
        splitPane.setResizeWeight(0.66);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildProductSection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.BORDER),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        section.setBackground(UIHelper.PANEL_BG);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.add(lbProductTitle, BorderLayout.NORTH);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterRow.setOpaque(false);
        filterRow.add(lbProductName);
        filterRow.add(tfProductName);
        filterRow.add(lbPriceFrom);
        filterRow.add(tfPriceFrom);
        filterRow.add(lbPriceTo);
        filterRow.add(tfPriceTo);
        filterRow.add(lbCategory);
        filterRow.add(cbbCategory);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(btAddProduct);
        buttonRow.add(btEditProduct);
        buttonRow.add(btDelProduct);
        buttonRow.add(btRefreshProduct);

        JPanel controls = new JPanel(new BorderLayout(0, 8));
        controls.setOpaque(false);
        controls.add(filterRow, BorderLayout.NORTH);
        controls.add(buttonRow, BorderLayout.SOUTH);
        topPanel.add(controls, BorderLayout.CENTER);

        section.add(topPanel, BorderLayout.NORTH);
        section.add(spProducts, BorderLayout.CENTER);
        return section;
    }

    private JPanel buildCategorySection() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.BORDER),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        section.setBackground(UIHelper.PANEL_BG);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.add(lbCategoryTitle, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new BorderLayout(0, 8));
        searchRow.setOpaque(false);
        JPanel searchInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchInput.setOpaque(false);
        searchInput.add(lbSearchCategory);
        searchInput.add(tfSearchByCategory);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(btAddCat);
        buttonRow.add(btEditCat);
        buttonRow.add(btDelCat);

        searchRow.add(searchInput, BorderLayout.NORTH);
        searchRow.add(buttonRow, BorderLayout.SOUTH);
        topPanel.add(searchRow, BorderLayout.CENTER);

        section.add(topPanel, BorderLayout.NORTH);
        section.add(spCategories, BorderLayout.CENTER);
        return section;
    }

    private void applyStyles() {
        setBackground(UIHelper.APP_BG);

        UIHelper.styleSectionTitle(lbProductTitle);
        UIHelper.styleSectionTitle(lbCategoryTitle);
        lbProductTitle.setFont(UIHelper.FONT_SECTION_TITLE.deriveFont(19f));
        lbCategoryTitle.setFont(UIHelper.FONT_SECTION_TITLE.deriveFont(19f));
        lbProductTitle.setForeground(UIHelper.TEXT_DARK);
        lbCategoryTitle.setForeground(UIHelper.TEXT_DARK);

        lbSearchCategory.setFont(UIHelper.FONT_BASE);
        lbProductName.setFont(UIHelper.FONT_BASE);
        lbPriceFrom.setFont(UIHelper.FONT_BASE);
        lbPriceTo.setFont(UIHelper.FONT_BASE);
        lbCategory.setFont(UIHelper.FONT_BASE);

        UIHelper.styleTextField(tfSearchByCategory);
        UIHelper.styleTextField(tfProductName);
        UIHelper.styleTextField(tfPriceFrom);
        UIHelper.styleTextField(tfPriceTo);
        UIHelper.styleComboBox(cbbCategory);

        setMinPref(tfProductName, 210, 36);
        setMinPref(tfPriceFrom, 110, 36);
        setMinPref(tfPriceTo, 110, 36);
        setMinPref(cbbCategory, 190, 36);
        setMinPref(tfSearchByCategory, 260, 36);

        UIHelper.stylePrimaryButton(btAddProduct);
        UIHelper.styleSecondaryButton(btEditProduct);
        UIHelper.styleOutlineDangerButton(btDelProduct);
        UIHelper.styleSecondaryButton(btRefreshProduct);
        setFixedSize(btAddProduct, 130, 38);
        setFixedSize(btEditProduct, 120, 38);
        setFixedSize(btDelProduct, 120, 38);
        setFixedSize(btRefreshProduct, 120, 38);

        UIHelper.stylePrimaryButton(btAddCat);
        UIHelper.styleSecondaryButton(btEditCat);
        UIHelper.styleOutlineDangerButton(btDelCat);
        setFixedSize(btAddCat, 100, 38);
        setFixedSize(btEditCat, 100, 38);
        setFixedSize(btDelCat, 100, 38);

        UIHelper.styleTable(tbProducts);
        UIHelper.styleTable(tbCategories);
        UIHelper.alignCenterColumn(tbProducts, 0);
        UIHelper.alignMoneyColumn(tbProducts, 3);
        UIHelper.alignCenterColumn(tbProducts, 4);
        UIHelper.alignCenterColumn(tbCategories, 0);
        setSttColumnWidth(tbProducts);
        setSttColumnWidth(tbCategories);
    }

    private void setFixedSize(java.awt.Component component, int w, int h) {
        Dimension size = new Dimension(w, h);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
    }

    private void setMinPref(java.awt.Component component, int w, int h) {
        Dimension size = new Dimension(w, h);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
    }

    private void setSttColumnWidth(JTable table) {
        if (table == null || table.getColumnCount() == 0) {
            return;
        }
        table.getColumnModel().getColumn(0).setMinWidth(45);
        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
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
        btRefreshProduct.addActionListener(e -> refreshProductFilters());

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
            resetProductTableScroll();
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
            resetProductTableScroll();
        } catch (Exception ex) {
            showError("Lỗi tìm kiếm sản phẩm: " + ex.getMessage());
        }
    }

    private void refreshProductFilters() {
        tfProductName.setText("");
        tfPriceFrom.setText("");
        tfPriceTo.setText("");
        if (cbbCategory.getItemCount() > 0) {
            cbbCategory.setSelectedIndex(0);
        }
        loadProducts();
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
        if (modelRow >= displayedCategories.size()) {
            showWarning("Không tìm thấy danh mục cần sửa.");
            return;
        }
        Category selectedCategory = displayedCategories.get(modelRow);
        int categoryId = selectedCategory.getCategoryId();
        String currentName = selectedCategory.getCategoryName();
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
        if (modelRow >= displayedCategories.size()) {
            showWarning("Không tìm thấy danh mục cần xóa.");
            return;
        }
        Category selectedCategory = displayedCategories.get(modelRow);
        int categoryId = selectedCategory.getCategoryId();
        String categoryName = selectedCategory.getCategoryName();
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
        if (modelRow >= displayedProducts.size()) {
            showWarning("Không tìm thấy sản phẩm cần sửa.");
            return;
        }
        Product selectedProduct = displayedProducts.get(modelRow);
        int productId = selectedProduct.getProductId();
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
        if (modelRow >= displayedProducts.size()) {
            showWarning("Không tìm thấy sản phẩm cần ẩn.");
            return;
        }
        Product selectedProduct = displayedProducts.get(modelRow);
        int productId = selectedProduct.getProductId();
        String productName = selectedProduct.getProductName();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Sản phẩm \"" + productName + "\" sẽ bị ẩn khỏi thực đơn. Tiếp tục?",
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
        displayedCategories = new ArrayList<>(categories);
        catModel.setRowCount(0);
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            catModel.addRow(new Object[]{i + 1, category.getCategoryName()});
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void displayCategoryFilter(List<Category> categories) {
        JComboBox combo = cbbCategory;
        combo.removeAllItems();
        for (Category category : controller.buildCategoryFilterItems(categories)) {
            combo.addItem(category);
        }
    }

    private void displayProducts(List<Product> products) {
        displayedProducts = new ArrayList<>(products);
        prodModel.setRowCount(0);
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            String categoryName = product.getCategory() == null ? "" : product.getCategory().getCategoryName();
            prodModel.addRow(new Object[]{
                i + 1,
                product.getProductName(),
                categoryName,
                UIHelper.formatMoney(product.getPrice()),
                mapActiveStatus(product.isActive())
            });
        }
    }

    private String mapActiveStatus(boolean active) {
        return active ? "Đang hoạt động" : "Ngừng hoạt động";
    }

    private void resetProductTableScroll() {
        if (tbProducts.getRowCount() > 0) {
            tbProducts.scrollRectToVisible(tbProducts.getCellRect(0, 0, true));
        }
        spProducts.getVerticalScrollBar().setValue(0);
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
}

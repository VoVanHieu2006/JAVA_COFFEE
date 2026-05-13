/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package cafemanager.view;
import cafemanager.model.Category;
import cafemanager.model.Product;
 
import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author Acer
 */
public class AddEditDialog extends javax.swing.JDialog {
    private static final String ACTIVE_UI = "Đang hoạt động";
    private static final String INACTIVE_UI = "Ngừng hoạt động";
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddEditDialog.class.getName());

    private Product result = null;
 
    /** Sản phẩm đang edit (null nếu là Add mới). */
    private Product editingProduct = null;
 
    // ─── Constructor: Add mới ────────────────────────────────────────────────
 
    public AddEditDialog(java.awt.Frame parent, List<Category> categories) {
        super(parent, true);
        initComponents();
        applyStyles();
        setTitle("Thêm Sản phẩm");
        jLabel5.setText("THÊM SẢN PHẨM");
        btSave.setText("Thêm");
        populateCategories(categories);
        // Active mặc định là true khi thêm mới
        cbbActive.setSelectedItem(ACTIVE_UI);
        centerOnParent(parent);
        registerEvents();
    }
 
    // ─── Constructor: Edit ──────────────────────────────────────────────────
 
    public AddEditDialog(java.awt.Frame parent, List<Category> categories, Product product) {
        super(parent, true);
        initComponents();
        applyStyles();
        setTitle("Sửa Sản phẩm");
        jLabel5.setText("SỬA SẢN PHẨM");
        btSave.setText("Lưu thay đổi");
        this.editingProduct = product;
        populateCategories(categories);
        fillForm(product);
        centerOnParent(parent);
        registerEvents();
    }
 
    // ─── Public API ──────────────────────────────────────────────────────────
 
    /** Trả về Product đã được điền/sửa, hoặc null nếu user Cancel. */
    public Product getResult() {
        return result;
    }
 
    // ─── Private helpers ─────────────────────────────────────────────────────
 
    private void populateCategories(List<Category> categories) {
        cbbCategory.removeAllItems();
        for (Category c : categories) {
            cbbCategory.addItem(c);
        }
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
 
        cbbActive.removeAllItems();
        cbbActive.addItem(ACTIVE_UI);
        cbbActive.addItem(INACTIVE_UI);
    }
 
    private void fillForm(Product p) {
        tfName.setText(p.getProductName());
        spnPrice.setValue(p.getPrice().doubleValue());
        tfDescription.setText(p.getDescription());
        cbbActive.setSelectedItem(p.isActive() ? ACTIVE_UI : INACTIVE_UI);
 
        // Chọn đúng category
        for (int i = 0; i < cbbCategory.getItemCount(); i++) {
            Category c = (Category) cbbCategory.getItemAt(i);
            if (c.getCategoryId() == p.getCategoryId()) {
                cbbCategory.setSelectedIndex(i);
                break;
            }
        }
    }
 
    private void registerEvents() {
        btSave.addActionListener(e -> handleSave());
        btCancel.addActionListener(e -> dispose());
    }
 
    private void handleSave() {
        // Validate
        String name = tfName.getText().trim();
        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        Object priceObj = spnPrice.getValue();
        double priceDouble;
        try {
            priceDouble = ((Number) priceObj).doubleValue();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Giá sản phẩm không hợp lệ.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (priceDouble <= 0) {
            JOptionPane.showMessageDialog(this, "Giá sản phẩm phải lớn hơn 0.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        Object catObj = cbbCategory.getSelectedItem();
        if (!(catObj instanceof Category)) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        // Build Product result
        Product p = new Product();
 
        // Nếu đang edit thì giữ lại productId
        if (editingProduct != null) {
            p.setProductId(editingProduct.getProductId());
        }
 
        p.setProductName(name);
        p.setPrice(BigDecimal.valueOf(priceDouble));
        p.setCategoryId(((Category) catObj).getCategoryId());
        p.setDescription(tfDescription.getText());
        p.setActive(ACTIVE_UI.equals(cbbActive.getSelectedItem()));
 
        result = p;
        dispose();
    }
 
    private void centerOnParent(java.awt.Frame parent) {
        setLocationRelativeTo(parent);
    }

    private void applyStyles() {
        getContentPane().setBackground(UIHelper.APP_BG);
        UIHelper.styleSectionTitle(jLabel5);
        UIHelper.styleTextField(tfName);
        UIHelper.styleTextField(tfDescription);
        UIHelper.styleComboBox(cbbCategory);
        UIHelper.styleComboBox(cbbActive);
        UIHelper.stylePrimaryButton(btSave);
        UIHelper.styleSecondaryButton(btCancel);
        tfName.setPreferredSize(new java.awt.Dimension(340, 38));
        tfDescription.setPreferredSize(new java.awt.Dimension(340, 38));
        cbbCategory.setPreferredSize(new java.awt.Dimension(340, 38));
        cbbActive.setPreferredSize(new java.awt.Dimension(180, 38));
        spnPrice.setPreferredSize(new java.awt.Dimension(180, 38));
        spnPrice.setFont(UIHelper.FONT_BASE);
        btSave.setPreferredSize(new java.awt.Dimension(140, 38));
        btCancel.setPreferredSize(new java.awt.Dimension(100, 38));
        alignActionButtons();
    }

    private void alignActionButtons() {
        if (!(getContentPane().getLayout() instanceof java.awt.GridBagLayout)) {
            return;
        }
        java.awt.GridBagLayout layout = (java.awt.GridBagLayout) getContentPane().getLayout();
        java.awt.GridBagConstraints saveConstraints = layout.getConstraints(btSave);
        saveConstraints.anchor = java.awt.GridBagConstraints.EAST;
        saveConstraints.insets = new java.awt.Insets(20, 28, 22, 10);
        layout.setConstraints(btSave, saveConstraints);

        java.awt.GridBagConstraints cancelConstraints = layout.getConstraints(btCancel);
        cancelConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cancelConstraints.insets = new java.awt.Insets(20, 0, 22, 28);
        layout.setConstraints(btCancel, cancelConstraints);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tfName = new javax.swing.JTextField();
        spnPrice = new javax.swing.JSpinner();
        cbbCategory = new javax.swing.JComboBox<>();
        tfDescription = new javax.swing.JTextField();
        cbbActive = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btSave = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(460, 330));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(44, 108, 220));
        jLabel5.setText("PRODUCT");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 28, 12, 28);
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Tên sản phẩm:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 6, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        tfName.setPreferredSize(new java.awt.Dimension(250, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 28);
        getContentPane().add(tfName, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Giá:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 6, 10);
        getContentPane().add(jLabel2, gridBagConstraints);

        spnPrice.setModel(new javax.swing.SpinnerNumberModel(1000.0d, 1000.0d, 999999999.0d, 1000.0d));
        spnPrice.setPreferredSize(new java.awt.Dimension(140, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 28);
        getContentPane().add(spnPrice, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Danh mục:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 6, 10);
        getContentPane().add(jLabel3, gridBagConstraints);

        cbbCategory.setPreferredSize(new java.awt.Dimension(250, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 28);
        getContentPane().add(cbbCategory, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Mô tả:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 6, 10);
        getContentPane().add(jLabel6, gridBagConstraints);

        tfDescription.setPreferredSize(new java.awt.Dimension(250, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 28);
        getContentPane().add(tfDescription, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Trạng thái:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 6, 10);
        getContentPane().add(jLabel4, gridBagConstraints);

        cbbActive.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive" }));
        cbbActive.setPreferredSize(new java.awt.Dimension(140, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 28);
        getContentPane().add(cbbActive, gridBagConstraints);

        btSave.setBackground(new java.awt.Color(44, 108, 220));
        btSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btSave.setForeground(new java.awt.Color(255, 255, 255));
        btSave.setText("Lưu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 28, 22, 10);
        getContentPane().add(btSave, gridBagConstraints);

        btCancel.setText("Hủy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 22, 28);
        getContentPane().add(btCancel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AddEditDialog dialog = new AddEditDialog(new javax.swing.JFrame(), new java.util.ArrayList<>());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btSave;
    private javax.swing.JComboBox<String> cbbActive;
    private javax.swing.JComboBox<Category> cbbCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSpinner spnPrice;
    private javax.swing.JTextField tfDescription;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables
}

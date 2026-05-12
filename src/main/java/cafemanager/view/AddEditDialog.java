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
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddEditDialog.class.getName());

    private Product result = null;
 
    /** Sản phẩm đang edit (null nếu là Add mới). */
    private Product editingProduct = null;
 
    // ─── Constructor: Add mới ────────────────────────────────────────────────
 
    public AddEditDialog(java.awt.Frame parent, List<Category> categories) {
        super(parent, true);
        initComponents();
        setTitle("Thêm Sản phẩm");
        jLabel5.setText("THÊM SẢN PHẨM");
        populateCategories(categories);
        // Active mặc định là true khi thêm mới
        cbbActive.setSelectedItem("Active");
        centerOnParent(parent);
        registerEvents();
    }
 
    // ─── Constructor: Edit ──────────────────────────────────────────────────
 
    public AddEditDialog(java.awt.Frame parent, List<Category> categories, Product product) {
        super(parent, true);
        initComponents();
        setTitle("Sửa Sản phẩm");
        jLabel5.setText("SỬA SẢN PHẨM");
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
        cbbActive.addItem("Active");
        cbbActive.addItem("Inactive");
    }
 
    private void fillForm(Product p) {
        tfName.setText(p.getProductName());
        spnPrice.setValue(p.getPrice().doubleValue());
        cbbActive.setSelectedItem(p.isActive() ? "Active" : "Inactive");
 
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
        p.setActive("Active".equals(cbbCategory.getSelectedItem() instanceof Category
                ? cbbActive.getSelectedItem()
                : "Active"));
        // Active từ cbbActive
        p.setActive("Active".equals(cbbActive.getSelectedItem()));
 
        result = p;
        dispose();
    }
 
    private void centerOnParent(java.awt.Frame parent) {
        setLocationRelativeTo(parent);
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
        cbbCategory = new javax.swing.JComboBox<>();
        cbbActive = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        spnPrice = new javax.swing.JSpinner();
        btSave = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 156;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 8, 0, 40);
        getContentPane().add(tfName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 78;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 8, 0, 0);
        getContentPane().add(cbbCategory, gridBagConstraints);

        cbbActive.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 8, 0, 0);
        getContentPane().add(cbbActive, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 60, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Price:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 60, 0, 0);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Category:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 60, 0, 0);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Active:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 60, 0, 0);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("PRODUCT");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 18, 0, 0);
        getContentPane().add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 8, 0, 0);
        getContentPane().add(spnPrice, gridBagConstraints);

        btSave.setBackground(new java.awt.Color(0, 204, 51));
        btSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btSave.setForeground(new java.awt.Color(255, 255, 255));
        btSave.setText("Save");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 60, 17, 0);
        getContentPane().add(btSave, gridBagConstraints);

        btCancel.setBackground(new java.awt.Color(255, 0, 0));
        btCancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btCancel.setForeground(new java.awt.Color(255, 255, 255));
        btCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 38, 17, 0);
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
    private javax.swing.JSpinner spnPrice;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables
}

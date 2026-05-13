/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cafemanager.view;

import cafemanager.controller.PosController;
import cafemanager.dto.POSOrderItem;
import cafemanager.model.Product;
import cafemanager.util.SessionManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class POSPanel extends javax.swing.JPanel {

    private final PosController posController;
    private List<Product> displayedProducts;
    private DefaultTableModel productTableModel;
    private DefaultTableModel orderTableModel;

    /**
     * Creates new form POSPanel
     */
    public POSPanel() {
        posController = new PosController();
        displayedProducts = new ArrayList<>();
        
        initComponents();
        configureLayout();
        applyStyles();
        initTables();
        registerEvents();
        
        loadProducts();
        updateTotalAmount();
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jPanel1, jPanel2);
        splitPane.setResizeWeight(0.58);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);

        jPanel1.setMinimumSize(new Dimension(500, 0));
        jPanel2.setMinimumSize(new Dimension(400, 0));

        removeAll();
        add(splitPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void applyStyles() {
        setBackground(UIHelper.APP_BG);
        jPanel1.setBackground(UIHelper.APP_BG);
        jPanel2.setBackground(UIHelper.APP_BG);
        jPanel3.setBackground(UIHelper.APP_BG);
        jPanel10.setBackground(UIHelper.APP_BG);

        UIHelper.stylePrimaryButton(btnPay);
        UIHelper.stylePrimaryButton(btnAddItem);
        UIHelper.styleSecondaryButton(btnCancelOrder);
        UIHelper.styleOutlineDangerButton(btnRemoveItem);
        UIHelper.styleSearchField(txtSearch);

        btnAddItem.setPreferredSize(new Dimension(120, 34));
        btnPay.setPreferredSize(new Dimension(115, 38));
        btnCancelOrder.setPreferredSize(new Dimension(115, 38));
        btnRemoveItem.setPreferredSize(new Dimension(115, 38));

        jPanel10.removeAll();
        jPanel10.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        jPanel10.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        jPanel10.add(txtSearch);
        jPanel10.add(jLabel3);
        jPanel10.add(spnQuantity);
        jPanel10.add(btnAddItem);

        jPanel2.setPreferredSize(new Dimension(430, 0));
        jPanel2.removeAll();
        jPanel2.setLayout(new java.awt.BorderLayout(0, 8));
        jPanel2.add(jLabel1, java.awt.BorderLayout.NORTH);
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel2.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel3.removeAll();
        jPanel3.setLayout(new java.awt.BorderLayout(0, 8));
        jPanel3.add(lblTotalAmount, java.awt.BorderLayout.NORTH);

        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 3, 8, 0));
        buttonPanel.add(btnPay);
        buttonPanel.add(btnCancelOrder);
        buttonPanel.add(btnRemoveItem);
        jPanel3.add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }

    private void initTables() {
        productTableModel = (DefaultTableModel) tblProducts.getModel();
        orderTableModel = (DefaultTableModel) tblOrderItems.getModel();
        tblProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblOrderItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        UIHelper.styleTable(tblProducts);
        UIHelper.styleTable(tblOrderItems);

        tblProducts.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

        UIHelper.setColumnWidth(tblProducts, 0, 160, 230);
        UIHelper.setColumnWidth(tblProducts, 1, 120, 150);
        UIHelper.setColumnWidth(tblProducts, 2, 130, 170);
        UIHelper.alignLeftColumn(tblProducts, 0);
        UIHelper.alignMoneyColumn(tblProducts, 1);
        UIHelper.alignLeftColumn(tblProducts, 2);

        tblOrderItems.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

        UIHelper.setColumnWidth(tblOrderItems, 0, 100, 125);
        UIHelper.setColumnWidth(tblOrderItems, 1, 85, 95);
        UIHelper.setColumnWidth(tblOrderItems, 2, 40, 45);
        UIHelper.setColumnWidth(tblOrderItems, 3, 80, 95);
        UIHelper.alignLeftColumn(tblOrderItems, 0);
        UIHelper.alignMoneyColumn(tblOrderItems, 1);
        UIHelper.alignCenterColumn(tblOrderItems, 2);
        UIHelper.alignMoneyColumn(tblOrderItems, 3);

        tblOrderItems.getColumnModel().getColumn(0).setHeaderValue("Tên món");
        tblOrderItems.getColumnModel().getColumn(1).setHeaderValue("Giá");
        tblOrderItems.getColumnModel().getColumn(2).setHeaderValue("SL");
        tblOrderItems.getColumnModel().getColumn(3).setHeaderValue("Tổng");
        tblOrderItems.getTableHeader().repaint();
    }

    private void registerEvents() {
        btnAddItem.addActionListener(evt -> addSelectedProduct());
        btnRemoveItem.addActionListener(evt -> removeSelectedItem());
        btnCancelOrder.addActionListener(evt -> cancelOrder());
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                filterProducts();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                filterProducts();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                filterProducts();
            }
        });
    }

    public void loadProducts() {
        displayedProducts = posController.loadAllActiveProducts();
        refreshProductTable(displayedProducts);
    }

    private void filterProducts() {
        displayedProducts = posController.filterProducts(txtSearch.getText());
        refreshProductTable(displayedProducts);
    }

    private void refreshProductTable(List<Product> products) {
        productTableModel.setRowCount(0);
        for (Product product : products) {
            String categoryName = product.getCategory() != null ? product.getCategory().getCategoryName() : "";
            productTableModel.addRow(new Object[]{
                product.getProductName(),
                UIHelper.formatMoney(product.getPrice()),
                categoryName
            });
        }
    }

    private void addSelectedProduct() {
        int selectedRow = tblProducts.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= displayedProducts.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món.");
            return;
        }

        int quantity = (int) spnQuantity.getValue();
        Product product = displayedProducts.get(selectedRow);

        posController.addOrUpdateOrderItem(product, quantity);
        
        refreshOrderTable();
        updateTotalAmount();
    }

    private void removeSelectedItem() {
        int selectedRow = tblOrderItems.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa.");
            return;
        }

        posController.removeOrderItem(selectedRow);
        refreshOrderTable();
        updateTotalAmount();
    }

    private void cancelOrder() {
        if (posController.isOrderEmpty()) {
            JOptionPane.showMessageDialog(this, "Hóa đơn hiện tại đang trống.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn hủy hóa đơn hiện tại?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            posController.clearOrder();
            refreshOrderTable();
            updateTotalAmount();
        }
    }

    private void refreshOrderTable() {
        orderTableModel.setRowCount(0);
        for (POSOrderItem item : posController.getOrderItems()) {
            orderTableModel.addRow(new Object[]{
                item.getProductName(),
                UIHelper.formatMoney(item.getUnitPrice()),
                item.getQuantity(),
                UIHelper.formatMoney(item.getSubtotal())
            });
        }
    }

    private void updateTotalAmount() {
        BigDecimal total = posController.calculateTotalAmount();
        lblTotalAmount.setText("TỔNG TIỀN: " + UIHelper.formatMoney(total));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProducts = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        spnQuantity = new javax.swing.JSpinner();
        btnAddItem = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrderItems = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lblTotalAmount = new javax.swing.JLabel();
        btnRemoveItem = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();
        btnCancelOrder = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(850, 450));

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 450));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 300));

        tblProducts.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Tên món", "Giá", "Danh mục"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblProducts);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jPanel1.add(jPanel5, java.awt.BorderLayout.LINE_END);
        jPanel1.add(jPanel6, java.awt.BorderLayout.LINE_START);
        jPanel1.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        txtSearch.setPreferredSize(new java.awt.Dimension(260, 34));
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        jPanel10.add(txtSearch);

        jLabel3.setText("Số lượng");
        jPanel10.add(jLabel3);

        spnQuantity.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
        jPanel10.add(spnQuantity);

        btnAddItem.setText("Thêm món");
        jPanel10.add(btnAddItem);

        jPanel1.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(300, 450));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Hóa đơn hiện tại");

        tblOrderItems.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Tên món", "Đơn giá", "Số lượng", "Tạm tính"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblOrderItems);

        lblTotalAmount.setText("TỔNG TIỀN:");

        btnRemoveItem.setText("Xóa món");

        btnPay.setText("Thanh toán");
        btnPay.addActionListener(this::btnPayActionPerformed);

        btnCancelOrder.setText("Hủy hóa đơn");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(btnPay)
                                                .addGap(17, 17, 17)
                                                .addComponent(btnCancelOrder)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnRemoveItem))
                                        .addComponent(lblTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(14, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 23,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnPay)
                                        .addGroup(jPanel3Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnRemoveItem)
                                                .addComponent(btnCancelOrder)))));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(494, 494, 494)
                                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 174,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 414,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 507,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 507,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
    }// </editor-fold>//GEN-END:initComponents

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {                                       
        if (posController.isOrderEmpty()) {
            JOptionPane.showMessageDialog(this, "Hóa đơn chưa có sản phẩm nào.");
            return;
        }

        if (SessionManager.getCurrentAccount() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập lại.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận thanh toán?",
                "Thanh toán",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int accountId = SessionManager.getCurrentAccount().getAccountId();
        boolean success = posController.processPayment(accountId);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công.");
            refreshOrderTable();
            updateTotalAmount();
        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại. Vui lòng thử lại.");
        }
    }

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSearchActionPerformed
        filterProducts();
    }// GEN-LAST:event_txtSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnCancelOrder;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnRemoveItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTotalAmount;
    private javax.swing.JSpinner spnQuantity;
    private javax.swing.JTable tblOrderItems;
    private javax.swing.JTable tblProducts;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}

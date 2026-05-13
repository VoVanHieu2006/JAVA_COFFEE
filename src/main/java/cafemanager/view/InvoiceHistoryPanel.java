/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cafemanager.view;

import cafemanager.controller.BillController;
import cafemanager.model.Bill;
import cafemanager.model.BillDetail;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class InvoiceHistoryPanel extends javax.swing.JPanel {

    private static final String STATUS_ALL = "Tất cả";
    private static final String STATUS_PAID_UI = "Đã thanh toán";
    private static final String STATUS_PENDING_UI = "Chờ thanh toán";
    private static final String STATUS_CANCELLED_UI = "Đã hủy";
    private final BillController billController = new BillController();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final List<Bill> currentBills = new ArrayList<>();
    private DefaultTableModel tableModel;

    /**
     * Creates new form InvoiceHistoryPanel
     */
    public InvoiceHistoryPanel() {
        initComponents();
        configureLayout();
        applyStyles();
        dcFromDate.setPreferredSize(new java.awt.Dimension(150, 34));
        dcToDate.setPreferredSize(new java.awt.Dimension(150, 34));
        dcFromDate.setDateFormatString("dd/MM/yyyy");
        dcToDate.setDateFormatString("dd/MM/yyyy");
        initTable();
        registerEvents();
        loadInvoices(null, null, null);
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.APP_BG);
        jPanel1.setBackground(UIHelper.APP_BG);
        jPanel2.setBackground(UIHelper.APP_BG);
        jPanel4.setBackground(UIHelper.APP_BG);
        jPanel4.setLayout(new BorderLayout());
        jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        jPanel2.removeAll();
        jPanel2.setLayout(new BorderLayout(10, 0));
        JPanel summaryPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 16, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.add(lblTotalInvoices);
        summaryPanel.add(lblTotalRevenue);
        jPanel2.add(summaryPanel, BorderLayout.CENTER);
        jPanel2.add(btnViewDetail, BorderLayout.EAST);

        jPanel4.removeAll();
        jPanel4.add(jScrollPane1, BorderLayout.CENTER);
        jPanel4.add(jPanel2, BorderLayout.SOUTH);

        removeAll();
        add(jPanel1, BorderLayout.NORTH);
        add(jPanel4, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void applyStyles() {
        UIHelper.stylePrimaryButton(btnSearch);
        UIHelper.styleSecondaryButton(btnRefresh);
        UIHelper.styleSecondaryButton(btnViewDetail);
        UIHelper.styleComboBox(cbStatus);
        cbStatus.removeAllItems();
        cbStatus.addItem(STATUS_ALL);
        cbStatus.addItem(STATUS_PAID_UI);
        cbStatus.addItem(STATUS_PENDING_UI);
        cbStatus.addItem(STATUS_CANCELLED_UI);
        lblTotalInvoices.setFont(UIHelper.FONT_BOLD.deriveFont(20f));
        lblTotalRevenue.setFont(UIHelper.FONT_BOLD.deriveFont(20f));
        lblTotalInvoices.setForeground(UIHelper.PRIMARY);
        lblTotalRevenue.setForeground(UIHelper.PRIMARY);
        lblTotalInvoices.setPreferredSize(new Dimension(260, 36));
        lblTotalRevenue.setPreferredSize(new Dimension(420, 36));
        btnViewDetail.setPreferredSize(new Dimension(128, 36));

        java.awt.Component fromDateEditor = dcFromDate.getDateEditor().getUiComponent();
        if (fromDateEditor instanceof javax.swing.JTextField) {
            UIHelper.styleTextField((javax.swing.JTextField) fromDateEditor);
        }
        java.awt.Component toDateEditor = dcToDate.getDateEditor().getUiComponent();
        if (toDateEditor instanceof javax.swing.JTextField) {
            UIHelper.styleTextField((javax.swing.JTextField) toDateEditor);
        }
    }

    public void reloadInvoices() {
        loadInvoices(null, null, null);
    }

    private void initTable() {
        tableModel = (DefaultTableModel) tblInvoices.getModel();
        UIHelper.styleTable(tblInvoices);

        UIHelper.alignMoneyColumn(tblInvoices, 3);
        UIHelper.alignCenterColumn(tblInvoices, 4);
    }

    private void registerEvents() {
        btnSearch.addActionListener(evt -> handleSearch());
        btnRefresh.addActionListener(evt -> handleRefresh());
        btnViewDetail.addActionListener(evt -> handleViewDetail());
    }

    private void handleSearch() {
        LocalDate from = toLocalDate(dcFromDate.getDate());
        LocalDate to = toLocalDate(dcToDate.getDate());
        String status = getSelectedStatus();
        loadInvoices(from, to, status);
    }

    private void handleRefresh() {
        dcFromDate.setDate(null);
        dcToDate.setDate(null);
        cbStatus.setSelectedIndex(0);
        loadInvoices(null, null, null);
    }

    private void loadInvoices(LocalDate from, LocalDate to, String status) {
        try {
            currentBills.clear();
            tableModel.setRowCount(0);

            List<Bill> bills = billController.getBills(from, to, status);
            System.out.println("Loaded invoices: " + bills.size());
            currentBills.addAll(bills);

            for (Bill bill : bills) {
                String billCode = formatBillCode(bill.getBillId());
                String staffName = bill.getAccount() != null ? bill.getAccount().getFullName() : "";
                String createdAt = bill.getCreatedAt() != null
                        ? bill.getCreatedAt().format(timeFormatter)
                        : "";
                tableModel.addRow(new Object[] {
                        billCode,
                        staffName,
                        createdAt,
                        UIHelper.formatMoney(bill.getTotalAmount()),
                        mapBillStatus(bill.getStatus())
                });
            }

            updateTotals();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTotals() {
        lblTotalInvoices.setText("Tổng hóa đơn: " + currentBills.size());

        BigDecimal revenue = BigDecimal.ZERO;
        for (Bill bill : currentBills) {
            if (bill.getTotalAmount() != null
                    && bill.getStatus() != null
                    && bill.getStatus().equalsIgnoreCase("PAID")) {
                revenue = revenue.add(bill.getTotalAmount());
            }
        }

        lblTotalRevenue.setText("Tổng doanh thu: " + UIHelper.formatMoney(revenue));
    }

    private void handleViewDetail() {
        int selectedRow = tblInvoices.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= currentBills.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn.");
            return;
        }

        int modelRow = tblInvoices.convertRowIndexToModel(selectedRow);
        Bill selectedBill = currentBills.get(modelRow);
        String billCode = (String) tableModel.getValueAt(modelRow, 0);
        int billId = parseBillId(billCode);
        if (billId <= 0) {
            JOptionPane.showMessageDialog(this, "Mã hóa đơn không hợp lệ.");
            return;
        }

        List<BillDetail> details = billController.getBillDetails(billId);
        if (details.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết hóa đơn.");
            return;
        }

        showDetailDialog(selectedBill, billCode, details);
    }

    private void showDetailDialog(Bill bill, String billCode, List<BillDetail> details) {
        JDialog dialog = new JDialog(javax.swing.SwingUtilities.getWindowAncestor(this),
                "Chi tiết hóa đơn - " + billCode, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout(8, 8));
        dialog.setSize(760, 500);
        dialog.setMinimumSize(new Dimension(700, 450));

        JPanel headerPanel = new JPanel(new GridLayout(2, 2, 8, 4));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        String staffName = bill.getAccount() != null ? bill.getAccount().getFullName() : "";
        String createdAt = bill.getCreatedAt() != null
                ? bill.getCreatedAt().format(timeFormatter)
                : "";

        headerPanel.add(new JLabel("Mã HĐ: " + billCode));
        headerPanel.add(new JLabel("Nhân viên: " + staffName));
        headerPanel.add(new JLabel("Thời gian: " + createdAt));
        headerPanel.add(new JLabel("Trạng thái: " + mapBillStatus(bill.getStatus())));

        DefaultTableModel detailModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "Tên món", "Đơn giá", "Số lượng", "Tạm tính" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (BillDetail detail : details) {
            detailModel.addRow(new Object[] {
                    detail.getProductName(),
                    UIHelper.formatMoney(detail.getUnitPrice()),
                    detail.getQuantity(),
                    UIHelper.formatMoney(detail.getSubtotal())
            });
        }

        JTable detailTable = new JTable(detailModel);
        UIHelper.styleTable(detailTable);
        UIHelper.alignCenterColumn(detailTable, 2);
        UIHelper.alignMoneyColumn(detailTable, 1);
        UIHelper.alignMoneyColumn(detailTable, 3);
        JScrollPane scrollPane = new JScrollPane(detailTable);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        String totalText = "Tổng tiền: " + UIHelper.formatMoney(bill.getTotalAmount());
        JLabel totalLabel = new JLabel(totalText);
        JButton closeButton = new JButton("Đóng");
        UIHelper.styleSecondaryButton(closeButton);
        closeButton.addActionListener(evt -> dialog.dispose());

        footerPanel.add(totalLabel, BorderLayout.WEST);
        footerPanel.add(closeButton, BorderLayout.EAST);

        dialog.add(headerPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(footerPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String getSelectedStatus() {
        String selected = (String) cbStatus.getSelectedItem();
        if (selected == null || STATUS_ALL.equalsIgnoreCase(selected)) {
            return null;
        }
        if (STATUS_PAID_UI.equals(selected)) {
            return "PAID";
        }
        if (STATUS_PENDING_UI.equals(selected)) {
            return "PENDING";
        }
        if (STATUS_CANCELLED_UI.equals(selected)) {
            return "CANCELLED";
        }
        return selected.trim();
    }

    private String formatBillCode(int billId) {
        return String.format("DH%06d", billId);
    }

    private int parseBillId(String billCode) {
        if (billCode == null || !billCode.startsWith("DH")) {
            return -1;
        }
        try {
            return Integer.parseInt(billCode.substring(2));
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private String formatMoney(BigDecimal amount) {
        return UIHelper.formatMoney(amount);
    }

    private String mapBillStatus(String status) {
        if (status == null) {
            return "";
        }
        switch (status.toUpperCase()) {
            case "PAID":
                return "Đã thanh toán";
            case "PENDING":
                return "Chờ thanh toán";
            case "CANCELLED":
                return "Đã hủy";
            default:
                return status;
        }
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
        jLabel2 = new javax.swing.JLabel();
        dcFromDate = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dcToDate = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvoices = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lblTotalInvoices = new javax.swing.JLabel();
        lblTotalRevenue = new javax.swing.JLabel();
        btnViewDetail = new javax.swing.JButton();

        jLabel2.setText("Từ ngày:");
        jPanel1.add(jLabel2);
        jPanel1.add(dcFromDate);

        jLabel3.setText("Đến ngày:");
        jPanel1.add(jLabel3);
        jPanel1.add(dcToDate);

        jLabel4.setText("Trạng thái");
        jPanel1.add(jLabel4);

        cbStatus.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "PAID", "PENDING", "CANCELLED" }));
        jPanel1.add(cbStatus);

        btnSearch.setText("Tìm kiếm");
        jPanel1.add(btnSearch);
        btnSearch.getAccessibleContext().setAccessibleName("");

        btnRefresh.setText("Làm mới");
        jPanel1.add(btnRefresh);

        tblInvoices.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã HĐ", "Nhân viên", "Thời gian", "Tổng tiền", "Trạng thái"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblInvoices);

        lblTotalInvoices.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        lblTotalInvoices.setText("Tổng hóa đơn: 0");

        lblTotalRevenue.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        lblTotalRevenue.setText("Tổng doanh thu: 0 VNĐ");

        btnViewDetail.setText("Xem chi tiết");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblTotalInvoices, javax.swing.GroupLayout.PREFERRED_SIZE, 137,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(lblTotalRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, 425,
                                        Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnViewDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 128,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnViewDetail, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblTotalRevenue)
                                                        .addComponent(lblTotalInvoices))))
                                .addContainerGap(19, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 762,
                                                Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 25, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(7, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewDetail;
    private javax.swing.JComboBox<String> cbStatus;
    private com.toedter.calendar.JDateChooser dcFromDate;
    private com.toedter.calendar.JDateChooser dcToDate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalInvoices;
    private javax.swing.JLabel lblTotalRevenue;
    private javax.swing.JTable tblInvoices;
    // End of variables declaration//GEN-END:variables
}

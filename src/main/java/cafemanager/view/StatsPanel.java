package cafemanager.view;

import cafemanager.controller.StatsController;
import java.awt.Font;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class StatsPanel extends javax.swing.JPanel {

    private final StatsController controller = new StatsController();
    private DefaultTableModel revenueModel;
    private DefaultTableModel topProductModel;

    public StatsPanel() {
        initComponents();
        setupRuntimeUi();
        initDefaultDates();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterPanel = new javax.swing.JPanel();
        lblFrom = new javax.swing.JLabel();
        dcFrom = new com.toedter.calendar.JDateChooser();
        lblTo = new javax.swing.JLabel();
        dcTo = new com.toedter.calendar.JDateChooser();
        btnFilter = new javax.swing.JButton();
        centerPanel = new javax.swing.JPanel();
        summaryPanel = new javax.swing.JPanel();
        revenueCardPanel = new javax.swing.JPanel();
        lblRevenueTitle = new javax.swing.JLabel();
        lblTotalRevenue = new javax.swing.JLabel();
        billCardPanel = new javax.swing.JPanel();
        lblBillTitle = new javax.swing.JLabel();
        lblTotalBills = new javax.swing.JLabel();
        tablesPanel = new javax.swing.JPanel();
        revenuePanel = new javax.swing.JPanel();
        lblRevenueByDateTitle = new javax.swing.JLabel();
        spRevenueByDate = new javax.swing.JScrollPane();
        tblRevenueByDate = new javax.swing.JTable();
        topPanel = new javax.swing.JPanel();
        lblTopProductsTitle = new javax.swing.JLabel();
        spTopProducts = new javax.swing.JScrollPane();
        tblTopProducts = new javax.swing.JTable();

        setBackground(new java.awt.Color(246, 247, 251));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setPreferredSize(new java.awt.Dimension(900, 600));
        setLayout(new java.awt.BorderLayout(10, 10));

        filterPanel.setBackground(new java.awt.Color(246, 247, 251));
        filterPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 6));

        lblFrom.setText("Từ ngày");
        filterPanel.add(lblFrom);

        dcFrom.setDateFormatString("dd/MM/yyyy");
        dcFrom.setPreferredSize(new java.awt.Dimension(140, 32));
        filterPanel.add(dcFrom);

        lblTo.setText("Đến ngày");
        filterPanel.add(lblTo);

        dcTo.setDateFormatString("dd/MM/yyyy");
        dcTo.setPreferredSize(new java.awt.Dimension(140, 32));
        filterPanel.add(dcTo);

        btnFilter.setText("Lọc thống kê");
        filterPanel.add(btnFilter);

        add(filterPanel, java.awt.BorderLayout.PAGE_START);

        centerPanel.setBackground(new java.awt.Color(246, 247, 251));
        centerPanel.setLayout(new java.awt.BorderLayout(10, 10));

        summaryPanel.setBackground(new java.awt.Color(246, 247, 251));
        summaryPanel.setLayout(new java.awt.GridLayout(1, 2, 12, 12));

        revenueCardPanel.setBackground(new java.awt.Color(255, 255, 255));
        revenueCardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 222, 227)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        revenueCardPanel.setLayout(new java.awt.BorderLayout(6, 6));

        lblRevenueTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblRevenueTitle.setText("Tổng doanh thu");
        revenueCardPanel.add(lblRevenueTitle, java.awt.BorderLayout.PAGE_START);

        lblTotalRevenue.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblTotalRevenue.setForeground(new java.awt.Color(44, 108, 220));
        lblTotalRevenue.setText("0 VNĐ");
        revenueCardPanel.add(lblTotalRevenue, java.awt.BorderLayout.CENTER);

        summaryPanel.add(revenueCardPanel);

        billCardPanel.setBackground(new java.awt.Color(255, 255, 255));
        billCardPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 222, 227)), javax.swing.BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        billCardPanel.setLayout(new java.awt.BorderLayout(6, 6));

        lblBillTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblBillTitle.setText("Số hóa đơn đã thanh toán");
        billCardPanel.add(lblBillTitle, java.awt.BorderLayout.PAGE_START);

        lblTotalBills.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblTotalBills.setForeground(new java.awt.Color(44, 108, 220));
        lblTotalBills.setText("0");
        billCardPanel.add(lblTotalBills, java.awt.BorderLayout.CENTER);

        summaryPanel.add(billCardPanel);

        centerPanel.add(summaryPanel, java.awt.BorderLayout.PAGE_START);

        tablesPanel.setBackground(new java.awt.Color(246, 247, 251));
        tablesPanel.setLayout(new java.awt.GridLayout(2, 1, 12, 12));

        revenuePanel.setBackground(new java.awt.Color(246, 247, 251));
        revenuePanel.setLayout(new java.awt.BorderLayout(8, 8));

        lblRevenueByDateTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblRevenueByDateTitle.setText("Doanh thu theo ngày");
        lblRevenueByDateTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 2, 0, 2));
        revenuePanel.add(lblRevenueByDateTitle, java.awt.BorderLayout.PAGE_START);

        tblRevenueByDate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ngày", "Doanh thu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spRevenueByDate.setViewportView(tblRevenueByDate);

        revenuePanel.add(spRevenueByDate, java.awt.BorderLayout.CENTER);

        tablesPanel.add(revenuePanel);

        topPanel.setBackground(new java.awt.Color(246, 247, 251));
        topPanel.setLayout(new java.awt.BorderLayout(8, 8));

        lblTopProductsTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTopProductsTitle.setText("Top sản phẩm bán chạy");
        lblTopProductsTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 2, 0, 2));
        topPanel.add(lblTopProductsTitle, java.awt.BorderLayout.PAGE_START);

        tblTopProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sản phẩm", "Số lượng bán", "Doanh thu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spTopProducts.setViewportView(tblTopProducts);

        topPanel.add(spTopProducts, java.awt.BorderLayout.CENTER);

        tablesPanel.add(topPanel);

        centerPanel.add(tablesPanel, java.awt.BorderLayout.CENTER);

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void setupRuntimeUi() {
        setBackground(UIHelper.APP_BG);
        filterPanel.setBackground(UIHelper.APP_BG);
        centerPanel.setBackground(UIHelper.APP_BG);
        summaryPanel.setBackground(UIHelper.APP_BG);
        tablesPanel.setBackground(UIHelper.APP_BG);
        revenuePanel.setBackground(UIHelper.APP_BG);
        topPanel.setBackground(UIHelper.APP_BG);

        lblFrom.setFont(UIHelper.FONT_BASE);
        lblTo.setFont(UIHelper.FONT_BASE);
        lblRevenueTitle.setFont(UIHelper.FONT_BASE.deriveFont(Font.BOLD, 15f));
        lblBillTitle.setFont(UIHelper.FONT_BASE.deriveFont(Font.BOLD, 15f));
        lblRevenueByDateTitle.setFont(UIHelper.FONT_BASE.deriveFont(Font.BOLD, 15f));
        lblTopProductsTitle.setFont(UIHelper.FONT_BASE.deriveFont(Font.BOLD, 15f));
        lblRevenueTitle.setForeground(UIHelper.TEXT_DARK);
        lblBillTitle.setForeground(UIHelper.TEXT_DARK);
        lblTotalRevenue.setForeground(UIHelper.PRIMARY);
        lblTotalBills.setForeground(UIHelper.PRIMARY);

        revenueCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        billCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        UIHelper.stylePrimaryButton(btnFilter);
        btnFilter.addActionListener(e -> loadStats());

        revenueModel = new DefaultTableModel(new Object[]{"Ngày", "Doanh thu"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblRevenueByDate.setModel(revenueModel);
        UIHelper.styleTable(tblRevenueByDate);
        tblRevenueByDate.getColumnModel().getColumn(1).setCellRenderer(UIHelper.createCellRenderer(SwingConstants.RIGHT));

        topProductModel = new DefaultTableModel(new Object[]{"Sản phẩm", "Số lượng bán", "Doanh thu"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTopProducts.setModel(topProductModel);
        UIHelper.styleTable(tblTopProducts);
        tblTopProducts.getColumnModel().getColumn(1).setCellRenderer(UIHelper.createCellRenderer(SwingConstants.CENTER));
        tblTopProducts.getColumnModel().getColumn(2).setCellRenderer(UIHelper.createCellRenderer(SwingConstants.RIGHT));
    }

    private void initDefaultDates() {
        LocalDate now = LocalDate.now();
        dcFrom.setDate(toDate(now.withDayOfMonth(1)));
        dcTo.setDate(toDate(now));
    }

    public void loadStats() {
        LocalDate fromDate = toLocalDate(dcFrom.getDate());
        LocalDate toDate = toLocalDate(dcTo.getDate());

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            JOptionPane.showMessageDialog(this,
                    "Ngày bắt đầu không được lớn hơn ngày kết thúc.",
                    "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BigDecimal totalRevenue = controller.getTotalRevenue(fromDate, toDate);
            int totalBills = controller.getTotalBills(fromDate, toDate);
            Map<LocalDate, BigDecimal> revenueByDate = controller.getRevenueByDateRange(fromDate, toDate);
            List<Object[]> topProducts = controller.getTopSellingProducts(fromDate, toDate, 10);

            lblTotalRevenue.setText(UIHelper.formatMoney(totalRevenue));
            lblTotalBills.setText(String.valueOf(totalBills));

            revenueModel.setRowCount(0);
            for (Map.Entry<LocalDate, BigDecimal> entry : revenueByDate.entrySet()) {
                revenueModel.addRow(new Object[]{
                    entry.getKey().toString(),
                    UIHelper.formatMoney(entry.getValue())
                });
            }

            topProductModel.setRowCount(0);
            for (Object[] row : topProducts) {
                BigDecimal revenue = row[2] instanceof BigDecimal ? (BigDecimal) row[2] : BigDecimal.ZERO;
                topProductModel.addRow(new Object[]{
                    row[0],
                    row[1],
                    UIHelper.formatMoney(revenue)
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Không thể tải thống kê: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFilter;
    private javax.swing.JPanel billCardPanel;
    private javax.swing.JPanel centerPanel;
    private com.toedter.calendar.JDateChooser dcFrom;
    private com.toedter.calendar.JDateChooser dcTo;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JLabel lblBillTitle;
    private javax.swing.JLabel lblFrom;
    private javax.swing.JLabel lblRevenueByDateTitle;
    private javax.swing.JLabel lblRevenueTitle;
    private javax.swing.JLabel lblTo;
    private javax.swing.JLabel lblTopProductsTitle;
    private javax.swing.JLabel lblTotalBills;
    private javax.swing.JLabel lblTotalRevenue;
    private javax.swing.JPanel revenueCardPanel;
    private javax.swing.JPanel revenuePanel;
    private javax.swing.JScrollPane spRevenueByDate;
    private javax.swing.JScrollPane spTopProducts;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JPanel tablesPanel;
    private javax.swing.JTable tblRevenueByDate;
    private javax.swing.JTable tblTopProducts;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}

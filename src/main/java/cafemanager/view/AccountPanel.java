package cafemanager.view;

import cafemanager.controller.AccountController;
import cafemanager.model.Account;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class AccountPanel extends javax.swing.JPanel {
    private final AccountController controller = new AccountController();
    private DefaultTableModel tableModel;

    public AccountPanel() {
        initComponents();
        initTableModel();
        initStyles();
        registerEvents();
        loadAccounts();
        updateToggleButtonBySelection();
    }

    private void initTableModel() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Username", "Họ tên", "Vai trò", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblAccounts.setModel(tableModel);
        tblAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIHelper.styleTable(tblAccounts);
        UIHelper.setColumnWidth(tblAccounts, 0, 50, 70);
        UIHelper.setColumnWidth(tblAccounts, 1, 130, 180);
        UIHelper.setColumnWidth(tblAccounts, 2, 180, 260);
        UIHelper.setColumnWidth(tblAccounts, 3, 90, 120);
        UIHelper.setColumnWidth(tblAccounts, 4, 110, 140);
        tblAccounts.getColumnModel().getColumn(0).setCellRenderer(UIHelper.createCellRenderer(SwingConstants.CENTER));
        tblAccounts.getColumnModel().getColumn(3).setCellRenderer(UIHelper.createCellRenderer(SwingConstants.CENTER));
        tblAccounts.getColumnModel().getColumn(4).setCellRenderer(UIHelper.createCellRenderer(SwingConstants.CENTER));
    }

    private void initStyles() {
        setBackground(UIHelper.APP_BG);
        pnlTop.setBackground(UIHelper.APP_BG);
        pnlForm.setBackground(UIHelper.APP_BG);
        pnlButtons.setBackground(UIHelper.APP_BG);

        JLabel[] labels = {lblUsername, lblPassword, lblFullName, lblRole};
        for (JLabel label : labels) {
            label.setFont(UIHelper.FONT_BASE.deriveFont(Font.BOLD));
            label.setForeground(UIHelper.TEXT_DARK);
        }

        styleInput(txtUsername, 170);
        styleInput(txtPassword, 170);
        styleInput(txtFullName, 230);
        cbRole.setPreferredSize(new Dimension(120, 34));
        cbRole.setMinimumSize(new Dimension(120, 34));
        cbRole.setFont(UIHelper.FONT_BASE);
        chkActive.setFont(UIHelper.FONT_BASE);
        chkActive.setBackground(UIHelper.APP_BG);

        UIHelper.stylePrimaryButton(btnAdd);
        UIHelper.styleButton(btnUpdate);
        UIHelper.styleButton(btnPassword);
        UIHelper.styleButton(btnToggleActive);
        UIHelper.styleButton(btnRefresh);
    }

    private void styleInput(JTextField field, int width) {
        UIHelper.styleTextField(field);
        Dimension size = new Dimension(width, 34);
        field.setPreferredSize(size);
        field.setMinimumSize(size);
        field.setColumns(0);
    }

    private void registerEvents() {
        btnRefresh.addActionListener(e -> loadAccounts());
        btnAdd.addActionListener(e -> addAccount());
        btnUpdate.addActionListener(e -> updateAccount());
        btnPassword.addActionListener(e -> changePassword());
        btnToggleActive.addActionListener(e -> toggleAccountStatus());
        tblAccounts.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
                updateToggleButtonBySelection();
            }
        });
    }

    private void loadAccounts() {
        try {
            List<Account> accounts = controller.loadAccounts();
            tableModel.setRowCount(0);
            for (Account account : accounts) {
                tableModel.addRow(new Object[]{
                    account.getAccountId(),
                    account.getUsername(),
                    account.getFullName(),
                    account.getRole(),
                    account.isActive() ? "Active" : "Inactive"
                });
            }
            updateToggleButtonBySelection();
        } catch (Exception ex) {
            showError("Không thể tải danh sách nhân viên: " + ex.getMessage());
        }
    }

    private void addAccount() {
        try {
            controller.createAccount(
                    txtUsername.getText(),
                    getPasswordText(),
                    txtFullName.getText(),
                    (String) cbRole.getSelectedItem(),
                    chkActive.isSelected());
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công.");
            clearForm();
            loadAccounts();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateAccount() {
        int id = getSelectedAccountId();
        if (id <= 0) {
            showError("Vui lòng chọn tài khoản cần cập nhật.");
            return;
        }
        try {
            controller.updateAccount(id, txtFullName.getText(), (String) cbRole.getSelectedItem(), chkActive.isSelected());
            JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công.");
            loadAccounts();
            selectAccountById(id);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void changePassword() {
        int id = getSelectedAccountId();
        if (id <= 0) {
            showError("Vui lòng chọn tài khoản cần đổi mật khẩu.");
            return;
        }
        try {
            controller.updatePassword(id, getPasswordText());
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công.");
            txtPassword.setText("");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void toggleAccountStatus() {
        int id = getSelectedAccountId();
        if (id <= 0) {
            showError("Vui lòng chọn tài khoản cần khóa hoặc mở khóa.");
            return;
        }

        boolean active = isSelectedAccountActive();
        boolean newStatus = !active;
        String action = active ? "khóa" : "mở khóa";
        String title = active ? "Xác nhận khóa" : "Xác nhận mở khóa";
        String message = active
                ? "Tài khoản này sẽ không thể đăng nhập sau khi bị khóa. Tiếp tục?"
                : "Tài khoản này sẽ được phép đăng nhập lại. Tiếp tục?";

        int confirm = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.setAccountActive(id, newStatus);
            JOptionPane.showMessageDialog(this, "Đã " + action + " tài khoản thành công.");
            loadAccounts();
            selectAccountById(id);
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void fillFormFromSelectedRow() {
        int modelRow = getSelectedModelRow();
        if (modelRow < 0) {
            return;
        }
        txtUsername.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        txtFullName.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
        cbRole.setSelectedItem(String.valueOf(tableModel.getValueAt(modelRow, 3)));
        chkActive.setSelected("Active".equals(String.valueOf(tableModel.getValueAt(modelRow, 4))));
        txtPassword.setText("");
    }

    private int getSelectedAccountId() {
        int modelRow = getSelectedModelRow();
        if (modelRow < 0) {
            return -1;
        }
        return ((Number) tableModel.getValueAt(modelRow, 0)).intValue();
    }

    private int getSelectedModelRow() {
        int viewRow = tblAccounts.getSelectedRow();
        if (viewRow < 0) {
            return -1;
        }
        return tblAccounts.convertRowIndexToModel(viewRow);
    }

    private boolean isSelectedAccountActive() {
        int modelRow = getSelectedModelRow();
        if (modelRow < 0) {
            return false;
        }
        return "Active".equals(String.valueOf(tableModel.getValueAt(modelRow, 4)));
    }

    private void updateToggleButtonBySelection() {
        int modelRow = getSelectedModelRow();
        if (modelRow < 0) {
            btnToggleActive.setText("Khóa / Mở khóa");
            btnToggleActive.setEnabled(false);
            return;
        }
        boolean active = "Active".equals(String.valueOf(tableModel.getValueAt(modelRow, 4)));
        btnToggleActive.setText(active ? "Khóa" : "Mở khóa");
        btnToggleActive.setEnabled(true);
    }

    private void selectAccountById(int accountId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object value = tableModel.getValueAt(i, 0);
            if (value instanceof Number && ((Number) value).intValue() == accountId) {
                int viewRow = tblAccounts.convertRowIndexToView(i);
                tblAccounts.setRowSelectionInterval(viewRow, viewRow);
                tblAccounts.scrollRectToVisible(tblAccounts.getCellRect(viewRow, 0, true));
                return;
            }
        }
    }

    private String getPasswordText() {
        return new String(txtPassword.getPassword());
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        cbRole.setSelectedItem("STAFF");
        chkActive.setSelected(true);
        tblAccounts.clearSelection();
        updateToggleButtonBySelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        pnlForm = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblFullName = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        lblRole = new javax.swing.JLabel();
        cbRole = new javax.swing.JComboBox<>();
        chkActive = new javax.swing.JCheckBox();
        pnlButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnPassword = new javax.swing.JButton();
        btnToggleActive = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPaneAccounts = new javax.swing.JScrollPane();
        tblAccounts = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setPreferredSize(new java.awt.Dimension(900, 520));
        setLayout(new java.awt.BorderLayout(10, 10));

        pnlTop.setLayout(new java.awt.BorderLayout(0, 8));

        pnlForm.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 6));

        lblUsername.setText("Username");
        pnlForm.add(lblUsername);

        txtUsername.setPreferredSize(new java.awt.Dimension(170, 34));
        pnlForm.add(txtUsername);

        lblPassword.setText("Mật khẩu");
        pnlForm.add(lblPassword);

        txtPassword.setPreferredSize(new java.awt.Dimension(170, 34));
        pnlForm.add(txtPassword);

        lblFullName.setText("Họ tên");
        pnlForm.add(lblFullName);

        txtFullName.setPreferredSize(new java.awt.Dimension(230, 34));
        pnlForm.add(txtFullName);

        lblRole.setText("Vai trò");
        pnlForm.add(lblRole);

        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STAFF", "ADMIN" }));
        cbRole.setPreferredSize(new java.awt.Dimension(120, 34));
        pnlForm.add(cbRole);

        chkActive.setSelected(true);
        chkActive.setText("Đang hoạt động");
        pnlForm.add(chkActive);

        pnlTop.add(pnlForm, java.awt.BorderLayout.PAGE_START);

        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

        btnAdd.setText("Thêm");
        pnlButtons.add(btnAdd);

        btnUpdate.setText("Cập nhật");
        pnlButtons.add(btnUpdate);

        btnPassword.setText("Đổi mật khẩu");
        pnlButtons.add(btnPassword);

        btnToggleActive.setText("Khóa / Mở khóa");
        pnlButtons.add(btnToggleActive);

        btnRefresh.setText("Tải lại");
        pnlButtons.add(btnRefresh);

        pnlTop.add(pnlButtons, java.awt.BorderLayout.CENTER);

        add(pnlTop, java.awt.BorderLayout.PAGE_START);

        tblAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Username", "Họ tên", "Vai trò", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneAccounts.setViewportView(tblAccounts);

        add(jScrollPaneAccounts, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnPassword;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnToggleActive;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbRole;
    private javax.swing.JCheckBox chkActive;
    private javax.swing.JScrollPane jScrollPaneAccounts;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTable tblAccounts;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}

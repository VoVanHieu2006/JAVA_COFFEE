package cafemanager.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public final class UIHelper {

    // =========================
    // FONT
    // =========================
    public static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SECTION_TITLE = new Font("Segoe UI", Font.BOLD, 18);

    // =========================
    // COLOR PALETTE
    // =========================
    public static final Color APP_BG = new Color(246, 247, 251);
    public static final Color PANEL_BG = Color.WHITE;

    public static final Color PRIMARY = new Color(47, 111, 219);
    public static final Color PRIMARY_HOVER = new Color(36, 95, 192);
    public static final Color PRIMARY_PRESSED = new Color(30, 79, 163);

    public static final Color TEXT_DARK = new Color(17, 24, 39);
    public static final Color TEXT_MUTED = new Color(107, 114, 128);

    public static final Color BORDER = new Color(209, 213, 219);
    public static final Color TABLE_HEADER = new Color(243, 244, 246);
    public static final Color TABLE_SELECTION = new Color(220, 235, 255);

    public static final Color DANGER = new Color(220, 38, 38);
    public static final Color DANGER_HOVER = new Color(185, 28, 28);
    public static final Color DANGER_PRESSED = new Color(153, 27, 27);

    public static final Color WARNING = new Color(245, 158, 11);
    public static final Color SUCCESS = new Color(22, 163, 74);

    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0");

    private UIHelper() {
    }

    // =========================
    // BUTTON STYLE
    // =========================

    /**
     * Nút thường: nền trắng, chữ đen.
     * Dùng cho các nút phụ chung.
     */
    public static void styleButton(JButton button) {
        if (button == null) {
            return;
        }

        button.setFont(FONT_BASE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(115, 34));
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_DARK);
        button.setBorder(BorderFactory.createLineBorder(BORDER));

        button.putClientProperty("JButton.buttonType", "square");
        button.putClientProperty("FlatLaf.style",
                "arc: 4; margin: 6,14,6,14; "
                        + "borderColor: #D1D5DB; "
                        + "hoverBackground: #F3F4F6; "
                        + "pressedBackground: #E5E7EB");
    }

    /**
     * Nút chính: xanh dương.
     * Dùng cho: Đăng nhập, Thêm, Thanh toán, Lọc dữ liệu, Tìm kiếm.
     */
    public static void stylePrimaryButton(JButton button) {
        if (button == null) {
            return;
        }

        styleButton(button);
        button.setFont(FONT_BOLD);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(PRIMARY));

        button.putClientProperty("FlatLaf.style",
                "arc: 4; margin: 6,14,6,14; "
                        + "background: #2F6FDB; "
                        + "foreground: #FFFFFF; "
                        + "borderColor: #2F6FDB; "
                        + "hoverBackground: #245FC0; "
                        + "pressedBackground: #1E4FA3");
    }

    /**
     * Nút phụ: trắng, viền xám.
     * Dùng cho: Cập nhật, Đổi mật khẩu, Tải lại, Làm mới, Đóng, Xem chi tiết.
     */
    public static void styleSecondaryButton(JButton button) {
        if (button == null) {
            return;
        }

        styleButton(button);
        button.setFont(FONT_BASE);
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_DARK);
        button.setBorder(BorderFactory.createLineBorder(BORDER));

        button.putClientProperty("FlatLaf.style",
                "arc: 4; margin: 6,14,6,14; "
                        + "background: #FFFFFF; "
                        + "foreground: #111827; "
                        + "borderColor: #D1D5DB; "
                        + "hoverBackground: #F3F4F6; "
                        + "pressedBackground: #E5E7EB");
    }

    /**
     * Nút nguy hiểm dạng đỏ nền.
     * Dùng khi muốn nhấn mạnh hành động nguy hiểm.
     */
    public static void styleDangerButton(JButton button) {
        if (button == null) {
            return;
        }

        styleButton(button);
        button.setFont(FONT_BOLD);
        button.setBackground(DANGER);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(DANGER));

        button.putClientProperty("FlatLaf.style",
                "arc: 4; margin: 6,14,6,14; "
                        + "background: #DC2626; "
                        + "foreground: #FFFFFF; "
                        + "borderColor: #DC2626; "
                        + "hoverBackground: #B91C1C; "
                        + "pressedBackground: #991B1B");
    }

    /**
     * Nút nguy hiểm dạng viền đỏ.
     * Khuyên dùng cho: Xóa món, Hủy hóa đơn, Xóa sản phẩm, Xóa danh mục, Khóa.
     */
    public static void styleOutlineDangerButton(JButton button) {
        if (button == null) {
            return;
        }

        styleButton(button);
        button.setFont(FONT_BASE);
        button.setBackground(Color.WHITE);
        button.setForeground(DANGER);
        button.setBorder(BorderFactory.createLineBorder(DANGER));

        button.putClientProperty("FlatLaf.style",
                "arc: 4; margin: 6,14,6,14; "
                        + "background: #FFFFFF; "
                        + "foreground: #DC2626; "
                        + "borderColor: #DC2626; "
                        + "hoverBackground: #FEF2F2; "
                        + "pressedBackground: #FEE2E2");
    }

    /**
     * Nút nhỏ.
     */
    public static void styleSmallButton(JButton button) {
        if (button == null) {
            return;
        }

        styleButton(button);
        button.setPreferredSize(new Dimension(95, 32));
        button.putClientProperty("FlatLaf.style",
                "arc: 4; margin: 4,12,4,12; "
                        + "borderColor: #D1D5DB; "
                        + "hoverBackground: #F3F4F6; "
                        + "pressedBackground: #E5E7EB");
    }

    // =========================
    // INPUT STYLE
    // =========================

    public static void styleTextField(JTextField field) {
        if (field == null) {
            return;
        }

        field.setFont(FONT_BASE);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        field.putClientProperty("FlatLaf.style",
                "arc: 4; "
                        + "borderColor: #D1D5DB; "
                        + "focusedBorderColor: #2F6FDB; "
                        + "margin: 4,10,4,10");
    }

    public static void styleSearchField(JTextField textField) {
        if (textField == null) {
            return;
        }

        styleTextField(textField);
        textField.setPreferredSize(new Dimension(260, 34));
        textField.putClientProperty("JTextField.placeholderText", "Tìm món...");
        textField.putClientProperty("JTextField.showClearButton", true);
        textField.putClientProperty("FlatLaf.style",
                "arc: 4; "
                        + "borderColor: #D1D5DB; "
                        + "focusedBorderColor: #2F6FDB; "
                        + "margin: 4,10,4,10");
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        if (comboBox == null) {
            return;
        }

        comboBox.setFont(FONT_BASE);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_DARK);
        comboBox.setPreferredSize(new Dimension(150, 34));

        comboBox.putClientProperty("FlatLaf.style",
                "arc: 4; "
                        + "borderColor: #D1D5DB; "
                        + "focusedBorderColor: #2F6FDB; "
                        + "buttonBackground: #FFFFFF");
    }

    // =========================
    // LABEL STYLE
    // =========================

    public static void styleTitle(JLabel label) {
        if (label == null) {
            return;
        }

        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_DARK);
    }

    public static void styleSectionTitle(JLabel label) {
        if (label == null) {
            return;
        }

        label.setFont(FONT_SECTION_TITLE);
        label.setForeground(TEXT_DARK);
    }

    public static void styleMutedLabel(JLabel label) {
        if (label == null) {
            return;
        }

        label.setFont(FONT_BASE);
        label.setForeground(TEXT_MUTED);
    }

    public static void styleSummaryNumber(JLabel label) {
        if (label == null) {
            return;
        }

        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(PRIMARY);
    }

    // =========================
    // TABLE STYLE
    // =========================

    public static void styleTable(JTable table) {
        if (table == null) {
            return;
        }

        table.setFont(FONT_BASE);
        table.setRowHeight(34);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setGridColor(BORDER);
        table.setSelectionBackground(TABLE_SELECTION);
        table.setSelectionForeground(TEXT_DARK);
        table.setBackground(Color.WHITE);
        table.setForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(FONT_BOLD);
            header.setBackground(TABLE_HEADER);
            header.setForeground(TEXT_DARK);
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
            header.setBorder(BorderFactory.createLineBorder(BORDER));
        }
    }

    public static DefaultTableCellRenderer createCellRenderer(int alignment) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(alignment);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

                if (!isSelected) {
                    setBackground(Color.WHITE);
                    setForeground(TEXT_DARK);
                }

                return this;
            }
        };
    }

    public static void alignColumn(JTable table, int column, int alignment) {
        if (table == null || table.getColumnModel().getColumnCount() <= column) {
            return;
        }

        table.getColumnModel().getColumn(column).setCellRenderer(createCellRenderer(alignment));
    }

    public static void alignMoneyColumn(JTable table, int column) {
        alignColumn(table, column, SwingConstants.RIGHT);
    }

    public static void alignCenterColumn(JTable table, int column) {
        alignColumn(table, column, SwingConstants.CENTER);
    }

    public static void alignLeftColumn(JTable table, int column) {
        alignColumn(table, column, SwingConstants.LEFT);
    }

    public static void setColumnWidth(JTable table, int col, int min, int preferred) {
        if (table == null || table.getColumnModel().getColumnCount() <= col) {
            return;
        }

        table.getColumnModel().getColumn(col).setMinWidth(min);
        table.getColumnModel().getColumn(col).setPreferredWidth(preferred);
    }

    public static class PaddedTableCellRenderer extends DefaultTableCellRenderer {

        private final int padding;

        public PaddedTableCellRenderer(int alignment, int padding) {
            this.padding = padding;
            setHorizontalAlignment(alignment);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setBorder(BorderFactory.createEmptyBorder(0, padding, 0, padding));

            if (!isSelected) {
                setBackground(Color.WHITE);
                setForeground(TEXT_DARK);
            }

            return this;
        }
    }

    // =========================
    // FORMAT
    // =========================

    public static String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return "0 VNĐ";
        }

        return MONEY_FORMAT.format(amount) + " VNĐ";
    }

    public static String formatMoney(Number amount) {
        if (amount == null) {
            return "0 VNĐ";
        }

        return MONEY_FORMAT.format(amount) + " VNĐ";
    }

    public static String formatMoney(double amount) {
        return MONEY_FORMAT.format(amount) + " VNĐ";
    }

    public static String formatMoney(long amount) {
        return MONEY_FORMAT.format(amount) + " VNĐ";
    }
}
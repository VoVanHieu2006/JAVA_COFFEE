package cafemanager.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Dimension;
import java.text.DecimalFormat;

public final class UIHelper {
    public static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Color APP_BG = new Color(246, 247, 251);
    public static final Color PRIMARY = new Color(44, 108, 220);
    public static final Color TEXT_DARK = new Color(40, 40, 40);
    public static final Color BORDER = new Color(220, 222, 227);

    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0");

    private UIHelper() {
    }

    public static void styleButton(JButton button) {
        if (button == null) {
            return;
        }
        button.setFont(FONT_BASE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(115, 34));
        button.putClientProperty("JButton.buttonType", "square");
        button.putClientProperty("FlatLaf.style", "arc: 4; margin: 6,14,6,14");
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_DARK);
    }

    public static void stylePrimaryButton(JButton button) {
        if (button == null) {
            return;
        }
        styleButton(button);
        button.setFont(FONT_BASE.deriveFont(Font.BOLD));
        button.setBackground(new Color(47, 111, 219));
        button.setForeground(Color.WHITE);
        button.putClientProperty("FlatLaf.style",
                "arc: 4; margin: 6,14,6,14; "
                        + "hoverBackground: #245FC0; pressedBackground: #1E4FA3");
    }

    public static void styleSmallButton(JButton button) {
        if (button == null) {
            return;
        }
        styleButton(button);
        button.setPreferredSize(new Dimension(95, 32));
        button.putClientProperty("FlatLaf.style", "arc: 8; margin: 4,12,4,12");
    }

    public static void styleTextField(JTextField field) {
        if (field == null) {
            return;
        }
        field.setFont(FONT_BASE);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
    }

    public static void styleSearchField(JTextField textField) {
        if (textField == null) {
            return;
        }
        styleTextField(textField);
        textField.setPreferredSize(new Dimension(260, 34));
        textField.putClientProperty("JTextField.placeholderText", "T\u00ecm m\u00f3n...");
        textField.putClientProperty("JTextField.showClearButton", true);
        textField.putClientProperty("FlatLaf.style", "arc: 4; margin: 4,10,4,10");
    }

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
        table.setSelectionBackground(new Color(220, 232, 250));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setFont(FONT_BASE.deriveFont(Font.BOLD));
            header.setBackground(new Color(245, 246, 248));
            header.setForeground(TEXT_DARK);
        }
    }

    public static String formatMoney(java.math.BigDecimal amount) {
        if (amount == null) {
            return "0 VNĐ";
        }
        return MONEY_FORMAT.format(amount) + " VNĐ";
    }

    public static DefaultTableCellRenderer createCellRenderer(int alignment) {
        return new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(alignment);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
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
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, padding, 0, padding));
            return this;
        }
    }
}

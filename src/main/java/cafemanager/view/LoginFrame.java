package cafemanager.view;

import cafemanager.controller.AuthController;
import cafemanager.model.Account;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(LoginFrame.class.getName());

    private final AuthController authController = new AuthController();

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JButton btnExit;

    public LoginFrame() {
        buildLoginUI();
        registerEvents();
    }

    private void buildLoginUI() {
        setTitle("Đăng nhập - Quản lý quán cà phê");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 520));
        setSize(new Dimension(1000, 650));

        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(UIHelper.APP_BG);

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(UIHelper.PANEL_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.BORDER),
                BorderFactory.createEmptyBorder(26, 38, 26, 38)));
        cardPanel.setPreferredSize(new Dimension(520, 380));
        cardPanel.setMinimumSize(new Dimension(480, 340));
        cardPanel.setMaximumSize(new Dimension(560, 420));
        cardPanel.add(createFormPanel(), BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        rootPanel.add(cardPanel, gbc);

        setContentPane(rootPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("QUẢN LÝ QUÁN CÀ PHÊ");
        lblTitle.setFont(UIHelper.FONT_TITLE.deriveFont(Font.BOLD, 31f));
        lblTitle.setForeground(UIHelper.TEXT_DARK);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setMaximumSize(new Dimension(460, 48));

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(UIHelper.FONT_BASE);
        lblUser.setForeground(UIHelper.TEXT_DARK);

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(UIHelper.FONT_BASE);
        lblPass.setForeground(UIHelper.TEXT_DARK);

        txtUser = new JTextField();
        UIHelper.styleTextField(txtUser);
        setFixedSize(txtUser, new Dimension(360, 40));

        txtPass = new JPasswordField();
        UIHelper.styleTextField(txtPass);
        txtPass.setEchoChar('•');
        txtPass.putClientProperty("FlatLaf.style", "showRevealButton: true");
        setFixedSize(txtPass, new Dimension(360, 40));

        btnLogin = new JButton("Đăng nhập");
        UIHelper.stylePrimaryButton(btnLogin);
        setFixedSize(btnLogin, new Dimension(160, 40));

        btnExit = new JButton("Thoát");
        UIHelper.styleSecondaryButton(btnExit);
        setFixedSize(btnExit, new Dimension(120, 40));

        JPanel userBlock = createInputBlock(lblUser, txtUser);
        JPanel passBlock = createInputBlock(lblPass, txtPass);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(btnLogin);
        buttonRow.add(btnExit);

        formPanel.add(Box.createVerticalGlue());
        formPanel.add(lblTitle);
        formPanel.add(Box.createVerticalStrut(34));
        formPanel.add(userBlock);
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(passBlock);
        formPanel.add(Box.createVerticalStrut(28));
        formPanel.add(buttonRow);
        formPanel.add(Box.createVerticalGlue());

        return formPanel;
    }

    private JPanel createInputBlock(JLabel label, JTextField field) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setPreferredSize(new Dimension(360, 72));
        panel.setMaximumSize(new Dimension(360, 72));

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
        return panel;
    }

    private void setFixedSize(Component component, Dimension size) {
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
    }

    private void registerEvents() {
        btnLogin.addActionListener(evt -> handleLogin());
        btnExit.addActionListener(evt -> {
            dispose();
            if (java.awt.Frame.getFrames().length == 0) {
                System.exit(0);
            }
        });
        txtUser.addActionListener(evt -> handleLogin());
        txtPass.addActionListener(evt -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword());

        try {
            Account account = authController.login(username, password);
            MainFrame mainFrame = new MainFrame(account);
            mainFrame.setVisible(true);
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi đăng nhập.", "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

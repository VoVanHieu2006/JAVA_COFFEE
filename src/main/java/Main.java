
import cafemanager.view.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {

        // ----------------- CODE TEST IN CSDL ---------------------------
        // try {
        // // Giao diện theo phong cách Windows cho chuyên nghiệp
        // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        //
        // java.awt.EventQueue.invokeLater(() -> {
        // new MainFrame().setVisible(true);
        // });

        // ----------------- CODE TEST TẠO CƠ SỞ DỮ LIỆU ---------------------------
        //
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });

    }
}
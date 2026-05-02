
import cafemanager.view.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        
        //----------------- CODE TEST IN CSDL ---------------------------
//        try {
//            // Giao diện theo phong cách Windows cho chuyên nghiệp
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        java.awt.EventQueue.invokeLater(() -> {
//            new MainFrame().setVisible(true);
//        });

        //----------------- CODE TEST TẠO CƠ SỞ DỮ LIỆU ---------------------------
//
        try {
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("LOI KHI TAO DATABASE: " + e.getMessage());
            e.printStackTrace();
        }
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });



    }
}

import cafemanager.ui.MainFrame;
import javax.swing.UIManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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
            System.out.println("Đang khởi tạo Migration...");
            
            // Dòng này sẽ quét persistence.xml và các @Entity để tự động tạo bảng
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CafePU");
            EntityManager em = emf.createEntityManager();
            
            System.out.println("DATABASE KHOI TAO THANH CONG");
            
            em.close();
            emf.close();
        } catch (Exception e) {
            System.err.println("LOI KHI TAO DATABASE: " + e.getMessage());
            e.printStackTrace();
        }



    }
}
package cafemanager.util;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/JAVA_COFFEE_DB";
        return DriverManager.getConnection(url, "root", "");
    }
}
package cafemanager.dao;

import cafemanager.model.Account;
import cafemanager.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {

    public Account findByUsername(String username) {
        String sql = """
                    SELECT accountId, full_name, isActive, password, role, username
                    FROM account
                    WHERE username = ?
                    LIMIT 1
                """;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Account account = new Account();

                account.setAccountId(rs.getInt("accountId"));
                account.setFullName(rs.getString("full_name"));
                account.setActive(rs.getBoolean("isActive"));
                account.setPassword(rs.getString("password"));
                account.setRole(rs.getString("role"));
                account.setUsername(rs.getString("username"));

                return account;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
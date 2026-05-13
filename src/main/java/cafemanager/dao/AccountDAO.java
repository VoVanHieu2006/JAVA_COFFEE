package cafemanager.dao;

import cafemanager.model.Account;
import cafemanager.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements IGenericDAO<Account> {

    @Override
    public boolean insert(Account account) throws Exception {
        String sql = "INSERT INTO Account (username, password, full_name, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getFullName());
            ps.setString(4, account.getRole());
            ps.setBoolean(5, account.isActive());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Account account) throws Exception {
        String sql = "UPDATE Account SET full_name = ?, role = ?, is_active = ? WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, account.getFullName());
            ps.setString(2, account.getRole());
            ps.setBoolean(3, account.isActive());
            ps.setInt(4, account.getAccountId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "UPDATE Account SET is_active = FALSE WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Account findById(int id) throws Exception {
        String sql = "SELECT account_id, username, password, full_name, role, is_active, created_at FROM Account WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public List<Account> findAll() throws Exception {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT account_id, username, password, full_name, role, is_active, created_at FROM Account ORDER BY account_id DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Account findByUsername(String username) {
        String sql = "SELECT account_id, username, password, full_name, role, is_active, created_at FROM Account WHERE username = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updatePassword(int accountId, String hashedPassword) throws Exception {
        String sql = "UPDATE Account SET password = ? WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean usernameExists(String username, int excludeId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Account WHERE LOWER(username) = LOWER(?) AND (? = -1 OR account_id <> ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username == null ? "" : username.trim());
            ps.setInt(2, excludeId);
            ps.setInt(3, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Account mapRow(ResultSet rs) throws Exception {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUsername(rs.getString("username"));
        account.setPassword(rs.getString("password"));
        account.setFullName(rs.getString("full_name"));
        account.setRole(rs.getString("role"));
        account.setActive(rs.getBoolean("is_active"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            account.setCreatedAt(created.toLocalDateTime());
        }
        return account;
    }
}

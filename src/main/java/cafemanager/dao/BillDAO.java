package cafemanager.dao;

import cafemanager.model.Account;
import cafemanager.model.Bill;
import cafemanager.model.BillDetail;
import cafemanager.util.DBConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BillDAO implements IGenericDAO<Bill> {

    @Override
    public boolean insert(Bill bill) throws Exception {
        return insertBillWithDetails(bill);
    }

    @Override
    public boolean update(Bill bill) throws Exception {
        String sql = "UPDATE Bill SET status = ?, total_amount = ? WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bill.getStatus());
            ps.setBigDecimal(2, bill.getTotalAmount());
            ps.setInt(3, bill.getBillId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        String sql = "UPDATE Bill SET status = 'CANCELLED' WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Bill findById(int id) throws Exception {
        String sql = baseBillSelect() + " WHERE b.bill_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapBill(rs) : null;
            }
        }
    }

    @Override
    public List<Bill> findAll() throws Exception {
        return findBills(null, null, null);
    }

    public List<Bill> findBills(LocalDate fromDate, LocalDate toDate, String status) {
        List<Bill> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(baseBillSelect());
        List<Object> params = new ArrayList<>();
        boolean hasWhere = false;

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" WHERE b.status = ?");
            params.add(status.trim().toUpperCase());
            hasWhere = true;
        }
        if (fromDate != null) {
            sql.append(hasWhere ? " AND" : " WHERE").append(" b.created_at >= ?");
            params.add(Timestamp.valueOf(fromDate.atStartOfDay()));
            hasWhere = true;
        }
        if (toDate != null) {
            sql.append(hasWhere ? " AND" : " WHERE").append(" b.created_at <= ?");
            LocalDateTime endOfDay = LocalDateTime.of(toDate, LocalTime.MAX);
            params.add(Timestamp.valueOf(endOfDay));
        }
        sql.append(" ORDER BY b.created_at DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBill(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BillDetail> findBillDetails(int billId) {
        List<BillDetail> list = new ArrayList<>();
        String sql = "SELECT detail_id, bill_id, product_id, product_name, unit_price, quantity, subtotal "
                + "FROM BillDetail WHERE bill_id = ? ORDER BY detail_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillDetail detail = new BillDetail();
                    detail.setDetailId(rs.getInt("detail_id"));
                    detail.setBillId(rs.getInt("bill_id"));
                    detail.setProductId(rs.getInt("product_id"));
                    detail.setProductName(rs.getString("product_name"));
                    detail.setUnitPrice(rs.getBigDecimal("unit_price"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setSubtotal(rs.getBigDecimal("subtotal"));
                    list.add(detail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertBillWithDetails(Bill bill) {
        String sqlBill = "INSERT INTO Bill (account_id, created_at, total_amount, status) VALUES (?, NOW(), ?, ?)";
        String sqlDetail = "INSERT INTO BillDetail (bill_id, product_id, product_name, unit_price, quantity, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int newBillId;
            try (PreparedStatement psBill = conn.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setInt(1, bill.getAccountId());
                psBill.setBigDecimal(2, bill.getTotalAmount());
                psBill.setString(3, bill.getStatus());
                psBill.executeUpdate();
                try (ResultSet rs = psBill.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new RuntimeException("Không lấy được mã hóa đơn vừa tạo.");
                    }
                    newBillId = rs.getInt(1);
                }
            }

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                for (BillDetail detail : bill.getBillDetails()) {
                    psDetail.setInt(1, newBillId);
                    psDetail.setInt(2, detail.getProductId());
                    psDetail.setString(3, detail.getProductName());
                    psDetail.setBigDecimal(4, detail.getUnitPrice());
                    psDetail.setInt(5, detail.getQuantity());
                    psDetail.setBigDecimal(6, detail.getSubtotal());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<LocalDate, BigDecimal> getRevenueByDateRange(LocalDate fromDate, LocalDate toDate) throws Exception {
        Map<LocalDate, BigDecimal> result = new LinkedHashMap<>();
        String sql = "SELECT DATE(created_at) AS sale_date, SUM(total_amount) AS revenue "
                + "FROM Bill WHERE status = 'PAID' AND DATE(created_at) BETWEEN ? AND ? "
                + "GROUP BY DATE(created_at) ORDER BY sale_date";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fromDate));
            ps.setDate(2, java.sql.Date.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getDate("sale_date").toLocalDate(), rs.getBigDecimal("revenue"));
                }
            }
        }
        return result;
    }

    public BigDecimal getTotalRevenue(LocalDate fromDate, LocalDate toDate) throws Exception {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) AS total_revenue FROM Bill "
                + "WHERE status = 'PAID' AND DATE(created_at) BETWEEN ? AND ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fromDate));
            ps.setDate(2, java.sql.Date.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getBigDecimal("total_revenue") : BigDecimal.ZERO;
            }
        }
    }

    public int getTotalBills(LocalDate fromDate, LocalDate toDate) throws Exception {
        String sql = "SELECT COUNT(*) AS total_bills FROM Bill WHERE status = 'PAID' AND DATE(created_at) BETWEEN ? AND ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fromDate));
            ps.setDate(2, java.sql.Date.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("total_bills") : 0;
            }
        }
    }

    public List<Object[]> getTopSellingProducts(LocalDate fromDate, LocalDate toDate, int limit) throws Exception {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT bd.product_name, SUM(bd.quantity) AS total_sold, SUM(bd.subtotal) AS total_revenue "
                + "FROM BillDetail bd JOIN Bill b ON bd.bill_id = b.bill_id "
                + "WHERE b.status = 'PAID' AND DATE(b.created_at) BETWEEN ? AND ? "
                + "GROUP BY bd.product_name ORDER BY total_sold DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fromDate));
            ps.setDate(2, java.sql.Date.valueOf(toDate));
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("product_name"),
                        rs.getInt("total_sold"),
                        rs.getBigDecimal("total_revenue")
                    });
                }
            }
        }
        return list;
    }

    private String baseBillSelect() {
        return "SELECT b.bill_id, b.account_id, b.created_at, b.total_amount, b.status, a.full_name "
                + "FROM Bill b JOIN Account a ON b.account_id = a.account_id";
    }

    private Bill mapBill(ResultSet rs) throws Exception {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("bill_id"));
        bill.setAccountId(rs.getInt("account_id"));
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) {
            bill.setCreatedAt(created.toLocalDateTime());
        }
        bill.setTotalAmount(rs.getBigDecimal("total_amount"));
        bill.setStatus(rs.getString("status"));
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setFullName(rs.getString("full_name"));
        account.setUsername("system_user");
        account.setRole("STAFF");
        bill.setAccount(account);
        return bill;
    }
}

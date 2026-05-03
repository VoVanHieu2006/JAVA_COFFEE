package cafemanager.dao;

import cafemanager.model.Bill;
import cafemanager.model.BillDetail;
import cafemanager.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public List<Bill> findBills(LocalDate fromDate, LocalDate toDate, String status) {
        List<Bill> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT b.billId, a.full_name, b.created_at, b.totalAmount, b.status "
                        + "FROM bill b "
                        + "JOIN account a ON b.account_id = a.accountId ");

        List<Object> params = new ArrayList<>();
        boolean hasWhere = false;

        if (status != null && !status.trim().isEmpty()) {
            sql.append("WHERE b.status = ? ");
            params.add(status.trim());
            hasWhere = true;
        }

        if (fromDate != null) {
            sql.append(hasWhere ? "AND " : "WHERE ");
            sql.append("b.created_at >= ? ");
            params.add(Timestamp.valueOf(fromDate.atStartOfDay()));
            hasWhere = true;
        }

        if (toDate != null) {
            sql.append(hasWhere ? "AND " : "WHERE ");
            LocalDateTime endOfDay = LocalDateTime.of(toDate, LocalTime.MAX);
            sql.append("b.created_at <= ? ");
            params.add(Timestamp.valueOf(endOfDay));
        }

        sql.append("ORDER BY b.created_at DESC");

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("billId"));
                    Timestamp created = rs.getTimestamp("created_at");
                    if (created != null) {
                        bill.setCreatedAt(created.toLocalDateTime());
                    }
                    bill.setTotalAmount(rs.getBigDecimal("totalAmount"));
                    bill.setStatus(rs.getString("status"));

                    if (rs.getString("full_name") != null) {
                        cafemanager.model.Account account = new cafemanager.model.Account();
                        account.setFullName(rs.getString("full_name"));
                        bill.setAccount(account);
                    }

                    list.add(bill);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BillDetail> findBillDetails(int billId) {
        List<BillDetail> list = new ArrayList<>();
        String sql = """
                SELECT p.product_name, bd.unitPrice, bd.quantity, bd.subtotal
                FROM bill_detail bd
                JOIN product p ON bd.product_id = p.productId
                WHERE bd.bill_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillDetail detail = new BillDetail();
                    detail.setProductName(rs.getString("product_name"));
                    detail.setUnitPrice(rs.getBigDecimal("unitPrice"));
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
        String sqlBill = """
                    INSERT INTO bill (created_at, status, totalAmount, account_id)
                    VALUES (NOW(), ?, ?, ?)
                """;

        String sqlDetail = """
                    INSERT INTO bill_detail
                    (quantity, subtotal, unitPrice, bill_id, product_id)
                    VALUES (?, ?, ?, ?, ?)
                """;

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psBill = conn.prepareStatement(
                    sqlBill,
                    Statement.RETURN_GENERATED_KEYS);

            psBill.setString(1, bill.getStatus());
            psBill.setBigDecimal(2, bill.getTotalAmount());
            psBill.setInt(3, bill.getAccountId());

            psBill.executeUpdate();

            ResultSet rs = psBill.getGeneratedKeys();

            int newBillId;

            if (rs.next()) {
                newBillId = rs.getInt(1);
            } else {
                throw new RuntimeException("Không lấy được mã hóa đơn vừa tạo.");
            }

            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            for (BillDetail detail : bill.getBillDetails()) {
                psDetail.setInt(1, detail.getQuantity());
                psDetail.setBigDecimal(2, detail.getSubtotal());
                psDetail.setBigDecimal(3, detail.getUnitPrice());
                psDetail.setInt(4, newBillId);
                psDetail.setInt(5, detail.getProductId());
                psDetail.addBatch();
            }

            psDetail.executeBatch();

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
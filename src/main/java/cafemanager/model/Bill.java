package cafemanager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int billId;
    private int accountId;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;    // ✅ BigDecimal
    private String status;             // "PENDING", "PAID", "CANCELLED"
    
    // Composition: Bill chứa danh sách BillDetail
    private List<BillDetail> billDetails = new ArrayList<>();
    
    // Tham chiếu đến Account (để hiển thị tên nhân viên)
    private Account account;

    public Bill() {}

    public Bill(int accountId, BigDecimal totalAmount, String status) {
        this.accountId = accountId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Helper method: Thêm chi tiết vào hóa đơn
    public void addDetail(BillDetail detail) {
        billDetails.add(detail);
        // Tự cập nhật totalAmount khi thêm món
        this.totalAmount = this.totalAmount.add(detail.getSubtotal());
    }

    // Getters & Setters
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<BillDetail> getBillDetails() { return billDetails; }
    public void setBillDetails(List<BillDetail> billDetails) { this.billDetails = billDetails; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
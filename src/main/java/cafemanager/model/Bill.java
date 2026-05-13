package cafemanager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int billId;
    private int accountId;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private String status = "PENDING";
    private List<BillDetail> billDetails = new ArrayList<>();
    private Account account;

    public Bill() {
    }

    public Bill(int accountId, BigDecimal totalAmount, String status) {
        this.accountId = accountId;
        setTotalAmount(totalAmount);
        setStatus(status);
        this.createdAt = LocalDateTime.now();
    }

    public void addDetail(BillDetail detail) {
        if (detail == null) {
            return;
        }
        billDetails.add(detail);
        totalAmount = totalAmount.add(detail.getSubtotal());
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || !(status.equalsIgnoreCase("PENDING") || status.equalsIgnoreCase("PAID") || status.equalsIgnoreCase("CANCELLED"))) {
            throw new IllegalArgumentException("Trạng thái hóa đơn không hợp lệ.");
        }
        this.status = status.toUpperCase();
    }

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails == null ? new ArrayList<>() : billDetails;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

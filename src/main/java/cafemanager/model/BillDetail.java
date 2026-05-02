package cafemanager.model;

import java.math.BigDecimal;

public class BillDetail {
    private int detailId;
    private int billId;
    private int productId;
    
    // Snapshot: Lưu tên và giá tại thời điểm bán (không tham chiếu động)
    private String productName;
    private BigDecimal unitPrice;      // ✅ BigDecimal
    private int quantity;
    private BigDecimal subtotal;       // ✅ BigDecimal = unitPrice × quantity

    public BillDetail() {}

    public BillDetail(int productId, String productName, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        // Tự tính subtotal khi khởi tạo
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters & Setters
    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
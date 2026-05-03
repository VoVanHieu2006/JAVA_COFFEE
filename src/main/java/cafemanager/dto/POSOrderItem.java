package cafemanager.dto;

import java.math.BigDecimal;

public class POSOrderItem {
    private int productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;

    public POSOrderItem(int productId, String productName, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        recalcSubtotal();
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void addQuantity(int additional) {
        this.quantity += additional;
        recalcSubtotal();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        recalcSubtotal();
    }

    private void recalcSubtotal() {
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

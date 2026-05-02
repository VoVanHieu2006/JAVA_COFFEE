package cafemanager.model;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private BigDecimal price;      // ✅ BigDecimal thay vì double
    private String imageUrl;
    private boolean isActive;
    
    // Composition: Product chứa đối tượng Category (để hiển thị tên danh mục)
    private Category category;

    public Product() {}

    public Product(int productId, String productName, BigDecimal price, int categoryId, boolean isActive) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.categoryId = categoryId;
        this.isActive = isActive;
    }

    // Getters & Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    @Override
    public String toString() {
        return productName + " - " + price + "₫";
    }
}
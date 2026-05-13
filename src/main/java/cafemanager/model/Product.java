package cafemanager.model;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private BigDecimal price;
    private String description = "";
    private boolean active = true;
    private Category category;

    public Product() {
    }

    public Product(int productId, String productName, BigDecimal price, int categoryId, boolean active) {
        this.productId = productId;
        setProductName(productName);
        setPrice(price);
        this.categoryId = categoryId;
        this.active = active;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Danh mục sản phẩm không hợp lệ.");
        }
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        if (productName.matches(".*[<>'\";].*")) {
            throw new IllegalArgumentException("Tên sản phẩm không được chứa ký tự đặc biệt.");
        }
        if (productName.trim().length() > 150) {
            throw new IllegalArgumentException("Tên sản phẩm không được vượt quá 150 ký tự.");
        }
        this.productName = productName.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0.");
        }
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description.trim();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            this.categoryId = category.getCategoryId();
        }
    }

    @Override
    public String toString() {
        return productName + " - " + price + "₫";
    }
}

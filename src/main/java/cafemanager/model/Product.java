package cafemanager.model;
import jakarta.persistence.*;
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    
    @Column(name = "product_name", length = 200, nullable = false)
    private String productName;
    
    private double price;
    private String imageUrl;
    private boolean isActive;
    
    @ManyToOne
    @JoinColumn(name = "category_id") // Khóa ngoại liên kết sang bảng Category
    private Category category;

    public Product() {}

    public Product(int productId, Category category, String productName, double price, String imageUrl, boolean isActive) {
        this.productId = productId;
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
    
}
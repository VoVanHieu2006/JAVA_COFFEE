package cafemanager.model;

public class Category {
    private int categoryId;
    private String categoryName;
    private String description;

    public Category() {
    }

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        setCategoryName(categoryName);
    }

    public Category(int categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        setCategoryName(categoryName);
        setDescription(description);
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống.");
        }
        this.categoryName = categoryName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description.trim();
    }

    @Override
    public String toString() {
        return categoryName;
    }
}

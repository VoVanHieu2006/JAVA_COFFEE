package cafemanager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private String username;
    private String password;      // Đã hash (BCrypt)
    private String fullName;
    private String role;          // "ADMIN" hoặc "STAFF"
    private boolean isActive;
    private LocalDateTime createdAt;

    public Account() {}

    public Account(int accountId, String username, String fullName, String role, boolean isActive) {
        this.accountId = accountId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.isActive = isActive;
    }

    // Getters & Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
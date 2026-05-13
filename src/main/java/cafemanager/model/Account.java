package cafemanager.model;

import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private boolean active = true;
    private LocalDateTime createdAt;

    public Account() {
    }

    public Account(int accountId, String username, String fullName, String role, boolean active) {
        this.accountId = accountId;
        setUsername(username);
        setFullName(fullName);
        setRole(role);
        this.active = active;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().length() < 4) {
            throw new IllegalArgumentException("Tên đăng nhập phải có ít nhất 4 ký tự.");
        }
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        this.fullName = fullName.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || !(role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("STAFF"))) {
            throw new IllegalArgumentException("Vai trò tài khoản không hợp lệ.");
        }
        this.role = role.toUpperCase();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

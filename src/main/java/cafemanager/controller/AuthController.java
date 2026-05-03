package cafemanager.controller;

import cafemanager.dao.AccountDAO;
import cafemanager.model.Account;
import cafemanager.util.PasswordUtils;
import cafemanager.util.SessionManager;

public class AuthController {

    private final AccountDAO accountDAO;

    public AuthController() {
        this.accountDAO = new AccountDAO();
    }

    public Account login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập mật khẩu.");
        }

        Account account = accountDAO.findByUsername(username.trim());

        if (account == null) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        if (!account.isActive()) {
            throw new IllegalArgumentException("Tài khoản đã bị khóa.");
        }

        boolean validPassword = PasswordUtils.checkPassword(password, account.getPassword());

        if (!validPassword) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        SessionManager.login(account);
        return account;
    }

    public void logout() {
        SessionManager.logout();
    }
}
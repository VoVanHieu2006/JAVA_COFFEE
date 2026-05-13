package cafemanager.controller;

import cafemanager.dao.AccountDAO;
import cafemanager.model.Account;
import cafemanager.util.PasswordUtils;
import java.util.List;

public class AccountController {
    private final AccountDAO accountDAO = new AccountDAO();

    public List<Account> loadAccounts() throws Exception {
        return accountDAO.findAll();
    }

    public boolean createAccount(String username, String rawPassword, String fullName, String role, boolean active) throws Exception {
        if (rawPassword == null || rawPassword.length() < 3) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 3 ký tự.");
        }
        if (accountDAO.usernameExists(username, -1)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(PasswordUtils.hashPassword(rawPassword));
        account.setFullName(fullName);
        account.setRole(role);
        account.setActive(active);
        return accountDAO.insert(account);
    }

    public boolean updateAccount(int accountId, String fullName, String role, boolean active) throws Exception {
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản cần cập nhật.");
        }
        account.setFullName(fullName);
        account.setRole(role);
        account.setActive(active);
        return accountDAO.update(account);
    }

    public boolean updatePassword(int accountId, String rawPassword) throws Exception {
        if (rawPassword == null || rawPassword.length() < 3) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 3 ký tự.");
        }
        return accountDAO.updatePassword(accountId, PasswordUtils.hashPassword(rawPassword));
    }

    public boolean setAccountActive(int accountId, boolean active) throws Exception {
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản cần cập nhật trạng thái.");
        }
        account.setActive(active);
        return accountDAO.update(account);
    }

    public boolean lockAccount(int accountId) throws Exception {
        return setAccountActive(accountId, false);
    }
}

package cafemanager.util;

import cafemanager.model.Account;

public class SessionManager {

    private static Account currentAccount;

    public static void login(Account account) {
        currentAccount = account;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    public static void logout() {
        currentAccount = null;
    }
}
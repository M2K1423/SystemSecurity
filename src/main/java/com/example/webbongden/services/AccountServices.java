package com.example.webbongden.services;

import com.example.webbongden.dao.AccountDao;
import com.example.webbongden.dao.model.Account;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

public class AccountServices {
    public final AccountDao accountDao;

    public AccountServices() {
        this.accountDao = new AccountDao();
    }


    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    public List<Account> getAccountByUserName(String name) {
        return accountDao.getAccountByUserName(name);
    }

    public boolean addAccount(Account account) {
        String hashedPassword = hashPassword(account.getPassword());
        account.setPassword(hashedPassword);
        return accountDao.addAccount(account);
    }

    public boolean deleteAccountById(int accountId) {
        return accountDao.deleteAccountById(accountId);
    }

    public Account authenticate(String username, String plainPassword) {
        // Lấy tài khoản từ DAO dựa trên username
        Account account = accountDao.authenticate(username);

        if (account == null) {
            return null; // Tài khoản không tồn tại
        }

        // So sánh mật khẩu người dùng nhập (plainPassword) với mật khẩu đã băm (account.getPassword())
        if (BCrypt.checkpw(plainPassword, account.getPassword())) {
            return account; // Trả về tài khoản nếu mật khẩu khớp
        }

        return null; // Trả về null nếu mật khẩu không khớp
    }


    public Account getAccountById(int accountId) {
        return accountDao.getAccountById(accountId);
    }

    public boolean updateAccount(Account account) {
        return accountDao.updateAccount(account);
    }


//    ========================TRANG ĐĂNG KÝ=============================
    public boolean registerAccount(Account account) {
        // Hash mật khẩu trước khi gọi DAO
        String hashedPassword = hashPassword(account.getPassword());
        account.setPassword(hashedPassword);

        // Gọi DAO để thêm tài khoản
        return accountDao.addAccountUser(account);
    }


    // Hàm hash mật khẩu
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Hàm kiểm tra mật khẩu
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    //======================Lấy lại mk ===================================

    // Kiểm tra email có tồn tại
    public boolean checkEmailExists(String email) {
        return accountDao.checkEmailExists(email);
    }

    // Tạo mật khẩu tạm thời
    public String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // Mật khẩu 8 ký tự ngẫu nhiên
    }

    // Cập nhật mật khẩu tạm thời
    public boolean updatePassword(String email, String temporaryPassword) {
        String hashedPassword = BCrypt.hashpw(temporaryPassword, BCrypt.gensalt());
        return accountDao.updatePassword(email, hashedPassword);
    }

//     Gửi mật khẩu qua email
    public boolean sendTemporaryPasswordEmail(String email, String temporaryPassword) {
        try {
            String subject = "Khôi phục mật khẩu";
            String body = "Mật khẩu tạm thời của bạn là: " + temporaryPassword +
                    "\nVui lòng đăng nhập và thay đổi mật khẩu ngay lập tức.";

            EmailService.sendEmail(email, subject, body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //User
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // Lấy tài khoản từ DAO dựa trên username
        Account account = accountDao.authenticate(username);

        if (account == null) {
            return false; // Tài khoản không tồn tại
        }

        // Kiểm tra mật khẩu cũ
        if (!checkPassword(oldPassword, account.getPassword())) {
            return false; // Mật khẩu cũ không chính xác
        }

        // Hash mật khẩu mới
        String hashedPassword = hashPassword(newPassword);

        // Cập nhật mật khẩu mới
        return accountDao.updatePassword(account.getEmail(), hashedPassword);
    }
}

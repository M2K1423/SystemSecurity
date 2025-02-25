package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Account;
import org.jdbi.v3.core.Jdbi;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class AccountDao {
    private final Jdbi jdbi;

    // Constructor truyền Jdbi
    public AccountDao() {
        this.jdbi = JDBIConnect.get();
    }

    // Lây tat ca tai khoan kh
    public List<Account> getAllAccounts() {
        String sql = "SELECT id, username, email, created_at, role FROM accounts";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new Account(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getDate("created_at"),
                                rs.getString("role")
                        ))
                        .list()
        );

    }

    // Lấy ds account dựa vào username
    public List<Account> getAccountByUserName(String username) {
        String sql = "SELECT id, username, email, role FROM accounts WHERE username LIKE CONCAT('%', :username, '%')";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("username", username) // Gán giá trị cho tham số :username
                        .map((rs, ctx) -> new Account(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("role")
                        ))
                        .list()
        );
    }

    public boolean addAccount(Account account) {
        return jdbi.inTransaction(handle -> {
            // Kiểm tra xem username đã tồn tại trong bảng accounts hay chưa
            String checkUsernameSql = "SELECT COUNT(*) FROM accounts WHERE username = :username";
            Integer count = handle.createQuery(checkUsernameSql)
                    .bind("username", account.getUsername())
                    .mapTo(Integer.class)
                    .one();

            // Nếu username đã tồn tại, không cho phép thêm tài khoản
            if (count > 0) {
                return false; // Trả về false nếu username đã tồn tại
            }

            // Kiểm tra khách hàng đã tồn tại trong bảng customers hay chưa
            String findCustomerSql = "SELECT id FROM customers WHERE cus_name = :cusName";
            Integer customerId = handle.createQuery(findCustomerSql)
                    .bind("cusName", account.getCusName())
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            // Nếu khách hàng chưa tồn tại, thêm mới
            if (customerId == null) {
                String addCustomerSql = "INSERT INTO customers (cus_name) VALUES (:cusName)";
                customerId = handle.createUpdate(addCustomerSql)
                        .bind("cusName", account.getCusName())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                // Nếu không thể tạo khách hàng, ném ngoại lệ
                if (customerId == null) {
                    throw new IllegalStateException("Không thể thêm khách hàng mới.");
                }
            }

            // Thêm tài khoản với customer_id
            String sql = "INSERT INTO accounts (username, email, password, role, customer_id, cus_name, created_at) " +
                    "VALUES (:username, :email, :password, :role, :customerId, :cusName, NOW())";

            int rowsAffected = handle.createUpdate(sql)
                    .bind("username", account.getUsername())    // Gán giá trị username
                    .bind("email", account.getEmail())          // Gán giá trị email
                    .bind("password", account.getPassword())    // Gán giá trị password (hash trước nếu cần)
                    .bind("role", account.getRole())            // Gán giá trị role
                    .bind("customerId", customerId)             // Gán giá trị customer_id (sử dụng từ bảng customers)
                    .bind("cusName", account.getCusName())      // Gán giá trị cus_name
                    .execute();

            return rowsAffected > 0; // Trả về true nếu thêm tài khoản thành công
        });
    }

    public boolean deleteAccountById(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = :accountId";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("accountId", accountId) // Gán giá trị accountId
                        .execute() > 0 // Trả về true nếu có dòng bị xóa
        );
    }

    //Kiểm tra tài khoản có trong Database k
//    public Account authenticate(String username, String password) {
//        String sql = "SELECT id, username, password, role FROM accounts WHERE username = :username AND password = :password";
//
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .bind("username", username)
//                        .bind("password", password)
//                        .mapToBean(Account.class)
//                        .findOne() // Trả về Optional<Account>
//                        .orElse(null) // Trả về null nếu không tìm thấy
//        );
//    }

    public Account authenticate(String username) {
        String sql = "SELECT id, email, username, password, role FROM accounts WHERE username = :username";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("username", username)
                        .mapToBean(Account.class)
                        .findOne()
                        .orElse(null) // Trả về null nếu không tìm thấy tài khoản
        );
    }


    //Lay account theo id
    public Account getAccountById(int accountId) {
        String sql = "SELECT " +
                "id, " +
                "customer_id, " +
                "cus_name, " +
                "email, " +
                "username, " +
                "password, " +
                "created_at, " +
                "role " +
                "FROM accounts " +
                "WHERE id = :accountId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("accountId", accountId) // Gắn giá trị ID
                        .map((rs, ctx) -> new Account(
                                rs.getInt("id"),
                                rs.getInt("customer_id"),
                                rs.getString("cus_name"),
                                rs.getString("email"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getDate("created_at"),
                                rs.getString("role")
                        ))
                        .findOne() // Trả về kết quả đầu tiên (nếu có)
                        .orElse(null) // Nếu không có bản ghi, trả về null
        );
    }

    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts " +
                "SET cus_name = :cusName, " +
                "username = :username, " +
                "email = :email, " +
                "password = :password, " +
                "role = :role " +
                "WHERE id = :id";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("cusName", account.getCusName())
                        .bind("username", account.getUsername())
                        .bind("email", account.getEmail())
                        .bind("password", account.getPassword())
                        .bind("role", account.getRole())
                        .bind("id", account.getId())
                        .execute() > 0
        );
    }

    public String getCustomerNameByAccountId(int accountId) {
        String sql = "SELECT cus_name " +
                "FROM accounts " +
                "WHERE id = :accountId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("accountId", accountId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null) // Trả về null nếu không tìm thấy
        );
    }

    public boolean addAccountUser(Account account) {
        return jdbi.inTransaction(handle -> {
            // Kiểm tra xem username đã tồn tại trong bảng accounts hay chưa
            String checkUsernameSql = "SELECT COUNT(*) FROM accounts WHERE username = :username";
            Integer count = handle.createQuery(checkUsernameSql)
                    .bind("username", account.getUsername())
                    .mapTo(Integer.class)
                    .one();

            // Nếu username đã tồn tại, không cho phép thêm tài khoản
            if (count > 0) {
                return false; // Trả về false nếu username đã tồn tại
            }

            // Kiểm tra khách hàng đã tồn tại trong bảng customers hay chưa
            String findCustomerSql = "SELECT id FROM customers WHERE cus_name = :cusName";
            Integer customerId = handle.createQuery(findCustomerSql)
                    .bind("cusName", account.getCusName())
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            // Nếu khách hàng chưa tồn tại, thêm mới
            if (customerId == null) {
                String addCustomerSql = "INSERT INTO customers (cus_name) VALUES (:cusName)";
                customerId = handle.createUpdate(addCustomerSql)
                        .bind("cusName", account.getCusName())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                // Nếu không thể tạo khách hàng, ném ngoại lệ
                if (customerId == null) {
                    throw new IllegalStateException("Không thể thêm khách hàng mới.");
                }
            }

            // Thêm tài khoản với customer_id, role mặc định là "user"
            String sql = "INSERT INTO accounts (username, email, password, role, customer_id, cus_name, created_at) " +
                    "VALUES (:username, :email, :password, :role, :customerId, :cusName, NOW())";

            int rowsAffected = handle.createUpdate(sql)
                    .bind("username", account.getUsername())    // Gán giá trị username
                    .bind("email", account.getEmail())          // Gán giá trị email
                    .bind("password", account.getPassword())    // Gán giá trị password (hash trước nếu cần)
                    .bind("role", "user")                       // Gán role mặc định là "user"
                    .bind("customerId", customerId)             // Gán giá trị customer_id (sử dụng từ bảng customers)
                    .bind("cusName", account.getCusName())      // Gán giá trị cus_name
                    .execute();

            return rowsAffected > 0; // Trả về true nếu thêm tài khoản thành công
        });
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE email = :email";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }
    public boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE accounts SET password = :password WHERE email = :email";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("password", hashedPassword)
                        .bind("email", email)
                        .execute() > 0
        );
    }

    //User
    public boolean updatePassword2(String email, String hashedPassword) {
        String sql = "UPDATE accounts SET password = :password WHERE email = :email";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("password", hashedPassword)
                        .bind("email", email)
                        .execute() > 0
        );
    }



    public static void main(String[] args) {
        String plainPassword = "admin123";

        // Tạo mật khẩu đã băm
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        // In ra mật khẩu đã băm
        System.out.println("Hashed Password: " + hashedPassword);
    }
}

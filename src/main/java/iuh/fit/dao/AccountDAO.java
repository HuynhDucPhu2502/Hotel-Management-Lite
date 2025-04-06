package iuh.fit.dao;

import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AccountDAO {

    // Lấy danh sách tất cả tài khoản
    public static List<Account> getAccount() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT a FROM Account a
                JOIN FETCH a.employee e
                """;
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy tài khoản theo ID
    public static Account getDataByID(String accountID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.find(Account.class, accountID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy tài khoản khi đăng nhập
    public static Account getLogin(String username, String password) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT a FROM Account a
                JOIN FETCH a.employee e
                WHERE a.userName = :username
                """;
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            query.setParameter("username", username);
            Account account = query.getSingleResult();

            if (account != null && account.getPassword().equals(password)) {
                return account;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tạo mới tài khoản
    public static void createData(Account account) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            // Đảm bảo đối tượng Employee được quản lý (managed)
            if (!em.contains(account.getEmployee())) {
                account.setEmployee(em.merge(account.getEmployee()));
            }

            // Lấy ID tiếp theo
            String nextID = getNextAccountID();
            account.setAccountID(nextID);

            // Cập nhật ID tiếp theo
            updateNextAccountID(nextID);

            // Lưu đối tượng Account
            em.persist(account);
            tx.commit();
            System.out.println("Tạo tài khoản thành công với mã: " + nextID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Xóa tài khoản
    public static void deleteData(String accountID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Account account = em.find(Account.class, accountID);
            if (account != null) {
                em.remove(account);
                tx.commit();
                System.out.println("Xóa tài khoản thành công: " + accountID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật tài khoản
    public static void updateData(Account account) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.merge(account);
            tx.commit();
            System.out.println("Cập nhật tài khoản thành công: " + account.getAccountID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy ID tiếp theo từ GlobalSequence
    public static String getNextAccountID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'Account'";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "ACC-000001";
        }
    }

    // Cập nhật ID tiếp theo
    private static void updateNextAccountID(String currentNextID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String prefix = "ACC-";
            int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
            String newNextID = prefix + String.format("%06d", nextIDNum);

            String jpql = """
                UPDATE GlobalSequence gs
                SET gs.nextID = :newNextID
                WHERE gs.tableName = 'Account'
                """;
            em.getTransaction().begin();
            em.createQuery(jpql)
                    .setParameter("newNextID", newNextID)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy tài khoản theo mã nhân viên
    public static Account getAccountByEmployeeID(String employeeID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT a FROM Account a
                WHERE a.employee.employeeCode = :employeeID
                """;
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            query.setParameter("employeeID", employeeID);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm tài khoản theo ID chứa từ khóa
    public static List<Account> findDataByContainsId(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT a FROM Account a
                JOIN FETCH a.employee e
                WHERE LOWER(a.accountID) LIKE :input
                """;
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            query.setParameter("input", "%" + input.toLowerCase() + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

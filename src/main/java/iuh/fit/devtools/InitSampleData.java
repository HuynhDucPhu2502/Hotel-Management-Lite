package iuh.fit.devtools;

import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import iuh.fit.security.PasswordHashing;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;

// =================================================================
// Lớp tạo dữ liệu cho bài
// =================================================================
public class InitSampleData {
    public static void main(String[] args) {
        initEmployeeAndAccountData();
    }

    // =================================================================
    // Hàm tạo dữ liệu nhân viên và tài khoản
    // =================================================================
    public static void initEmployeeAndAccountData() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<Employee> employees = List.of(
                    new Employee("EMP-000001", "Huynh Duc Phu", "0912345678", "123 Ho Chi Minh", Gender.MALE, "001099012345", LocalDate.of(1985, 6, 15), ObjectStatus.ACTIVE, Position.MANAGER),
                    new Employee("EMP-000002", "Nguyen Xuan Chuc", "0908765432", "456 Hue", Gender.MALE, "002199012346", LocalDate.of(1990, 4, 22), ObjectStatus.ACTIVE, Position.RECEPTIONIST),
                    new Employee("EMP-000003", "Le Tran Gia Huy", "0987654321", "789 Ho Chi Minh", Gender.MALE, "003299012347", LocalDate.of(1992, 8, 19), ObjectStatus.ACTIVE, Position.MANAGER),
                    new Employee("EMP-000004", "Dang Nguyen Tien Phat", "0934567890", "321 Binh Phuoc", Gender.MALE, "004399012348", LocalDate.of(1987, 12, 5), ObjectStatus.ACTIVE, Position.RECEPTIONIST),
                    new Employee("EMP-000005", "Vu Ba Hai", "0923456789", "654 Long An", Gender.MALE, "004399012349", LocalDate.of(1995, 3, 30), ObjectStatus.ACTIVE, Position.MANAGER)
            );

            for (Employee e : employees) {
                em.persist(e);
                Account acc = new Account(
                        "ACC-" + e.getEmployeeCode().substring(4),
                        e.getFullName().toLowerCase().replace(" ", ""),
                        PasswordHashing.hashPassword("test123@"),
                        AccountStatus.ACTIVE,
                        e
                );
                em.persist(acc);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            EntityManagerUtil.close();
        }
    }


}

package iuh.fit;

import iuh.fit.models.Account;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

import java.time.LocalDate;

/**
 * Admin 1/14/2025
 **/
public class PhuMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();

        em.getTransaction().begin();

        // Tạo dữ liệu Customer và Account
         generateFakeCustomerData(faker, em);
        // Tạo dữ liệu Employee
        generateFakeEmployeeAndAccountData(faker, em);


        em.getTransaction().commit();

        em.close();
        emf.close();
    }

    private static void generateFakeCustomerData(Faker faker, EntityManager em) {
        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer();
            customer.setFullName(faker.name().fullName());
            customer.setPhoneNumber(faker.number().digits(10));
            customer.setAddress(faker.address().fullAddress());
            customer.setGender(faker.options().option(Gender.class));
            customer.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
            customer.setIsActivate(faker.options().option(ObjectStatus.class));
            customer.setIdCardNumber(faker.number().digits(12));
            customer.setCustomerCode("CUS-" + faker.number().digits(6));

            em.persist(customer);
        }
    }

    private static void generateFakeEmployeeAndAccountData(Faker faker, EntityManager em) {
        for (int i = 1; i <= 10; i++) {
            Employee employee = new Employee();
            employee.setFullName(faker.name().fullName());
            employee.setPhoneNumber(faker.number().digits(10));
            employee.setAddress(faker.address().fullAddress());
            employee.setGender(faker.options().option(Gender.class));
            employee.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
            employee.setIsActivate(faker.options().option(ObjectStatus.class));
            employee.setIdCardNumber(faker.number().digits(12));
            employee.setEmployeeCode("EMP-" + faker.number().digits(6));
            employee.setPosition(faker.options().option(Position.class));

            Account account = new Account();
            account.setAccountID("ACC-" + faker.number().digits(6));
            account.setUserName(faker.name().username());
            account.setPassword(faker.internet().password());
            account.setStatus(faker.options().option(AccountStatus.class));

            employee.setAccount(account);
            account.setEmployee(employee);

            em.persist(employee);
        }
    }
}

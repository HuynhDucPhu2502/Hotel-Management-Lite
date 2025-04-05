package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class CustomerDAO {

    // Lấy tất cả Customer
    public static List<Customer> findAll() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    """
                    SELECT c FROM Customer c 
                    WHERE c.isActivate = :status
                    """, Customer.class);
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Lấy Customer theo ID
    public static Customer findById(String customerID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    """
                    SELECT c FROM Customer c 
                    WHERE c.customerCode = :customerID 
                    AND c.isActivate = :status
                    """, Customer.class);
            query.setParameter("customerID", customerID);
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tạo mới Customer
    public static void create(Customer customer) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Lấy mã Customer mới từ GlobalSequence
            String jpqlSelect = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = :tableName";
            TypedQuery<String> querySelect = em.createQuery(jpqlSelect, String.class);
            querySelect.setParameter("tableName", "Customer");
            String currentNextID = querySelect.getSingleResult();

            // Gán ID vào Customer
            customer.setCustomerCode(currentNextID);

            // Tăng ID lên 1 và cập nhật GlobalSequence
            String prefix = "CUS-";
            int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
            String newNextID = prefix + String.format("%06d", nextIDNum);

            String jpqlUpdate = "UPDATE GlobalSequence gs SET gs.nextID = :newNextID WHERE gs.tableName = :tableName";
            em.createQuery(jpqlUpdate)
                    .setParameter("newNextID", newNextID)
                    .setParameter("tableName", "Customer")
                    .executeUpdate();

            // Lưu Customer vào cơ sở dữ liệu
            em.persist(customer);
            tx.commit();
            System.out.println("Thêm mới Customer thành công: " + customer.getCustomerCode());

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
    }

    // Cập nhật Customer
    public static void update(Customer customer) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.merge(customer);
                tx.commit();
                System.out.println("Cập nhật Customer thành công: " + customer.getCustomerCode());
            } catch (Exception e) {
                e.printStackTrace();
                tx.rollback();
            }
        }
    }

    // Xóa Customer (chuyển trạng thái INACTIVE)
    public static void delete(String customerID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Customer customer = findById(customerID);
                if (customer != null) {
                    customer.setIsActivate(ObjectStatus.INACTIVE);
                    em.merge(customer);
                }
                tx.commit();
                System.out.println("Xóa Customer thành công: " + customerID);
            } catch (Exception e) {
                e.printStackTrace();
                tx.rollback();
            }
        }
    }

    // Lấy top 3 Customer ID mới nhất
    public static List<String> getTopThreeID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<String> query = em.createQuery(
                    """
                    SELECT c.customerCode FROM Customer c 
                    WHERE c.isActivate = :status
                    ORDER BY c.customerCode DESC
                    """, String.class);
            query.setParameter("status", ObjectStatus.ACTIVE);
            query.setMaxResults(3);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Tìm kiếm Customer theo ID chứa từ khóa
    public static List<Customer> findDataByContainsId(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    """
                    SELECT c FROM Customer c 
                    WHERE LOWER(c.customerCode) LIKE :input 
                    AND c.isActivate = :status
                    """, Customer.class);
            query.setParameter("input", "%" + input.toLowerCase() + "%");
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Lấy Customer theo ID Card Number
    public static Customer getDataByIDCardNumber(String idCardNumber) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    """
                    SELECT c FROM Customer c 
                    WHERE c.idCardNumber = :idCardNumber 
                    AND c.isActivate = :status
                    """, Customer.class);
            query.setParameter("idCardNumber", idCardNumber);
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm kiếm Customer theo nhiều tiêu chí
    public static List<Customer> searchCustomer(String customerID, String fullName, String phoneNumber, String email,
                                                String address, Gender gender, String idCardNumber, LocalDate dob) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    """
                    SELECT c FROM Customer c 
                    WHERE (:customerID IS NULL OR c.customerCode LIKE CONCAT('%', :customerID, '%'))
                    AND (:fullName IS NULL OR c.fullName LIKE CONCAT('%', :fullName, '%'))
                    AND (:phoneNumber IS NULL OR c.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))
                    AND (:address IS NULL OR c.address LIKE CONCAT('%', :address, '%'))
                    AND (:gender IS NULL OR c.gender = :gender)
                    AND (:idCardNumber IS NULL OR c.idCardNumber LIKE CONCAT('%', :idCardNumber, '%'))
                    AND (:dob IS NULL OR c.dob = :dob)
                    AND c.isActivate = :status
                    """, Customer.class);

            query.setParameter("customerID", customerID);
            query.setParameter("fullName", fullName);
            query.setParameter("phoneNumber", phoneNumber);
            query.setParameter("address", address);
            query.setParameter("gender", gender);
            query.setParameter("idCardNumber", idCardNumber);
            query.setParameter("dob", dob);
            query.setParameter("status", ObjectStatus.ACTIVE);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 1:08 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public class CustomerDAOImpl extends UnicastRemoteObject implements CustomerDAO {
    public CustomerDAOImpl() throws RemoteException {
    }

    public List<Customer> findAll() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c WHERE c.isActivate = :status", Customer.class);
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Customer findById(String customerID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c WHERE c.customerCode = :customerID", Customer.class);
            query.setParameter("customerID", customerID);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNextCustomerID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = :tableName";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            query.setParameter("tableName", "Customer");
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "CUS-000001";
        }
    }

    public void updateNextCustomerID(String currentNextID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String prefix = "CUS-";
            int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
            String newNextID = prefix + String.format("%06d", nextIDNum);

            String jpql = "UPDATE GlobalSequence gs SET gs.nextID = :newNextID WHERE gs.tableName = :tableName";
            em.getTransaction().begin();
            em.createQuery(jpql)
                    .setParameter("newNextID", newNextID)
                    .setParameter("tableName", "Customer")
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật ID tiếp theo");
        }
    }

    public void create(Customer customer) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Lấy ID mới
            String nextID = getNextCustomerID();
            customer.setCustomerCode(nextID);
            updateNextCustomerID(nextID);

            em.persist(customer);
            tx.commit();
            System.out.println("Thêm mới Customer thành công: " + customer.getCustomerCode());
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(Customer customer) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Customer existingCustomer = findById(customer.getCustomerCode());
            if (existingCustomer != null) {
                existingCustomer.setFullName(customer.getFullName());
                existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                existingCustomer.setAddress(customer.getAddress());
                existingCustomer.setGender(customer.getGender());
                existingCustomer.setIdCardNumber(customer.getIdCardNumber());
                existingCustomer.setDob(customer.getDob());
                existingCustomer.setIsActivate(customer.getIsActivate());

                em.merge(existingCustomer);
                tx.commit();
                System.out.println("Cập nhật Customer thành công: " + customer.getCustomerCode());
            } else {
                System.out.println("Không tìm thấy Customer: " + customer.getCustomerCode());
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(String customerID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Customer customer = findById(customerID);
            if (customer != null) {
                customer.setIsActivate(ObjectStatus.INACTIVE);
                em.merge(customer);
                System.out.println("Xóa Customer thành công: " + customerID);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


    public List<String> getTopThreeID() {
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

    public List<Customer> findDataByContainsId(String input) {
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

    public Customer getDataByIDCardNumber(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Customer> query = em.createQuery(
                    """
                    SELECT c FROM Customer c 
                    WHERE LOWER(c.idCardNumber) = :input 
                    AND c.isActivate = :status
                    """, Customer.class);
            query.setParameter("input", input.toLowerCase());
            query.setParameter("status", ObjectStatus.ACTIVE);

            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Customer> searchCustomer(
            String customerID, String fullName, String phoneNumber,
            String address, Gender gender, String idCardNumber, LocalDate dob
    ) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
            SELECT c FROM Customer c 
            WHERE (:customerID IS NULL OR c.customerCode LIKE CONCAT('%', :customerID, '%'))
            AND (:fullName IS NULL OR c.fullName LIKE CONCAT('%', :fullName, '%'))
            AND (:phoneNumber IS NULL OR c.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))
            AND (:address IS NULL OR c.address LIKE CONCAT('%', :address, '%'))
            AND (:idCardNumber IS NULL OR c.idCardNumber LIKE CONCAT('%', :idCardNumber, '%'))
            AND (:gender IS NULL OR c.gender = :gender)
            AND (:dob IS NULL OR c.dob = :dob)
            AND c.isActivate = :status
            """;

            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);

            query.setParameter("customerID", customerID);
            query.setParameter("fullName", fullName);
            query.setParameter("phoneNumber", phoneNumber);
            query.setParameter("address", address);
            query.setParameter("idCardNumber", idCardNumber);
            query.setParameter("gender", gender);
            query.setParameter("dob", dob);
            query.setParameter("status", ObjectStatus.ACTIVE);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

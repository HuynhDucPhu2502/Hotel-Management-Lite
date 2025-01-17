package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;

public class CustomerDAO {
    // Note: Customer không có ID, nhận ID từ bảng cha là
    // bảng Persons, mà có một trường Unique trong bảng
    // Customer là customer_code.
    // => Ngầm hiểu customer_code là ID của Customer.

    public static void create(Customer customer) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.persist(customer);
                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {
            resourceException.printStackTrace();
        }
    }

    public static List<Customer> findAll() {

        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            TypedQuery<Customer> query = em.createQuery("select c from Customer c", Customer.class);
            return query.getResultList();

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return Collections.emptyList();

        }
    }

    public static Customer findById(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            TypedQuery<Customer> query = em.createQuery (
                    "SELECT c FROM Customer c WHERE c.customerCode = :code",
                    Customer.class
            );
            query.setParameter("code", id);

            return query.getResultStream().findFirst().orElse(null);

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return null;

        }
    }

    public static void update(Customer customer) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.merge(customer);
                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

    public static void delete(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();

                TypedQuery<Customer> query = em.createQuery(
                        "SELECT c FROM Customer c WHERE c.customerCode = :code",
                        Customer.class
                );
                query.setParameter("code", id);

                query.getResultStream().findFirst().ifPresent(em::remove);

                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

}

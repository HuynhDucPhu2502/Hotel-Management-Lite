package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

public class EmployeeDAO {
    // Note: Employee không có ID, nhận ID từ bảng cha là
    // bảng Persons, mà có một trường Unique trong bảng
    // Customer là employee_code.
    // => Ngầm hiểu employee_code là ID của Customer.

    public static void create(Employee employee) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.persist(employee);
                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

    public static List<Employee> findAll() {

        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            TypedQuery<Employee> query = em.createQuery("select e from Employee e", Employee.class);
            return query.getResultList();

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return Collections.emptyList();

        }
    }

    public static Employee findById(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            TypedQuery<Employee> query = em.createQuery (
                    "SELECT e FROM Employee e WHERE e.employeeCode = :code",
                    Employee.class
            );
            query.setParameter("code", id);

            return query.getResultStream().findFirst().orElse(null);

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return null;

        }
    }

    public static void update(Employee employee) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.merge(employee);
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

                TypedQuery<Employee> query = em.createQuery (
                        "SELECT e FROM Employee e WHERE e.employeeCode = :code",
                        Employee.class
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

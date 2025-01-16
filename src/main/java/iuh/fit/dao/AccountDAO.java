package iuh.fit.dao;

import iuh.fit.models.Account;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

/**
 * Admin 1/16/2025
 **/
public class AccountDAO {
    public static void create(Account account) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.persist(account);
                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

    public static List<Account> findAll() {

        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            TypedQuery<Account> query = em.createQuery("select a from Account a", Account.class);
            return query.getResultList();

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return Collections.emptyList();

        }
    }

    public static Account findById(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            return em.find(Account.class, id);

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return null;

        }
    }

    public static void update(Account account) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.merge(account);
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

                Account account = em.find(Account.class, id);
                if (account != null) em.remove(account);

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

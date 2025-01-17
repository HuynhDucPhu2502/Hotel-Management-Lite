package iuh.fit.dao;

import iuh.fit.models.Account;
import iuh.fit.models.ReservationForm;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

public class ReservationFormDAO {
    public static void create(ReservationForm reservationForm) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.persist(reservationForm);
                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

    public static List<ReservationForm> findAll() {

        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            TypedQuery<ReservationForm> query = em.createQuery(
                    "select rf from ReservationForm rf",
                    ReservationForm.class
            );
            return query.getResultList();

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return Collections.emptyList();

        }
    }

    public static ReservationForm findById(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            return em.find(ReservationForm.class, id);

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return null;

        }
    }

    public static void update(ReservationForm reservationForm) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.merge(reservationForm);
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

                ReservationForm reservationForm = em.find(ReservationForm.class, id);
                if (reservationForm != null) em.remove(reservationForm);

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

package iuh.fit.dao;

import iuh.fit.models.HistoryCheckIn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HistoryCheckInDAO {
    public static boolean create(EntityManager em, HistoryCheckIn hci){
        try{
            em.getTransaction().begin();
            em.persist(hci);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(EntityManager em, String id){
        int n = 0;
        try{
            em.getTransaction().begin();
            Query query = em.createQuery(
                    "delete from HistoryCheckIn " +
                            "where roomHistoryCheckinID = :id "
            );
            query.setParameter("id", id);
            n = query.executeUpdate();
            em.getTransaction().commit();
            return n > 0;
        }catch (Exception e){
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    public static boolean update(EntityManager em, HistoryCheckIn newInfor){
        try{
            em.getTransaction().begin();
            em.merge(newInfor);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    public static List<HistoryCheckIn> findAll(EntityManager em){
        try{
            TypedQuery<HistoryCheckIn> query = em.createQuery(
                    "select hci from HistoryCheckIn hci",
                    HistoryCheckIn.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static HistoryCheckIn findById(EntityManager em, String id){
        try {
            return em.find(
                    HistoryCheckIn.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

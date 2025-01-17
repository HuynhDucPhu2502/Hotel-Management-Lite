package iuh.fit.dao;

import iuh.fit.models.HistoryCheckOut;
import iuh.fit.models.ServiceCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HistoryCheckOutDAO {
    public static boolean create(EntityManager em, HistoryCheckOut hco){
        try{
            em.getTransaction().begin();
            em.persist(hco);
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
                    "delete from HistoryCheckOut " +
                            "where roomHistoryCheckOutID = :id "
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

    public static boolean update(EntityManager em, HistoryCheckOut newInfor){
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

    public static List<HistoryCheckOut> findAll(EntityManager em){
        try{
            TypedQuery<HistoryCheckOut> query = em.createQuery(
                    "select hco from HistoryCheckOut hco",
                    HistoryCheckOut.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static HistoryCheckOut findById(EntityManager em, String id){
        try {
            return em.find(
                    HistoryCheckOut.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

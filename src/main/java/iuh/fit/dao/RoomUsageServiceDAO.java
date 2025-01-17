package iuh.fit.dao;

import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RoomUsageServiceDAO {
    public static boolean create(EntityManager em, RoomUsageService rus){
        try{
            em.getTransaction().begin();
            em.persist(rus);
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
                    "delete from RoomUsageService " +
                            "where roomUsageServiceID = :id "
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

    public static boolean update(EntityManager em, RoomUsageService newInfor){
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

    public static List<RoomUsageService> findAll(EntityManager em){
        try{
            TypedQuery<RoomUsageService> query = em.createQuery(
                    "select rus from RoomUsageService rus",
                    RoomUsageService.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static RoomUsageService findById(EntityManager em, String id){
        try {
            return em.find(
                    RoomUsageService.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

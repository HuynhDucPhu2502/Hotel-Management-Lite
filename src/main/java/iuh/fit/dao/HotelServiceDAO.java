package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.Room;
import iuh.fit.models.ServiceCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HotelServiceDAO {
    public static boolean create(EntityManager em, HotelService hs){
        try{
            em.getTransaction().begin();
            em.persist(hs);
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
                    "delete from HotelService " +
                            "where serviceID = :id "
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

    public static boolean update(EntityManager em, HotelService newInfor){
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

    public static List<HotelService> findAll(EntityManager em){
        try{
            TypedQuery<HotelService> query = em.createQuery(
                    "select hs from HotelService hs",
                    HotelService.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static HotelService findById(EntityManager em, String id){
        try {
            return em.find(
                    HotelService.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

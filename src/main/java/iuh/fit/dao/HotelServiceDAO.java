package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.Room;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HotelServiceDAO {
    public static boolean create(HotelService hs){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.persist(hs);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(String id){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            HotelService hs = em.find(
                    HotelService.class,
                    id
            );
            if(hs == null)
                return false;
            em.remove(hs);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean update(HotelService newInfor){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.merge(newInfor);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static List<HotelService> findAll(){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            TypedQuery<HotelService> query = em.createQuery(
                    "select hs from HotelService hs",
                    HotelService.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static HotelService findById(String id){
        try(EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.find(
                    HotelService.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

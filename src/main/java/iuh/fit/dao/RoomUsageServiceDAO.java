package iuh.fit.dao;

import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RoomUsageServiceDAO {
    public static boolean create(RoomUsageService rus){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.persist(rus);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(String id){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            RoomUsageService rus = em.find(
                    RoomUsageService.class,
                    id
            );
            if(rus == null)
                return false;
            em.remove(rus);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean update(RoomUsageService newInfor){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.merge(newInfor);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static List<RoomUsageService> findAll(){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            TypedQuery<RoomUsageService> query = em.createQuery(
                    "select rus from RoomUsageService rus",
                    RoomUsageService.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static RoomUsageService findById(String id){
        try(EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.find(
                    RoomUsageService.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

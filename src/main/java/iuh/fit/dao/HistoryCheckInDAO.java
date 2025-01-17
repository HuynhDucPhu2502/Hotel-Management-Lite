package iuh.fit.dao;

import iuh.fit.models.HistoryCheckIn;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HistoryCheckInDAO {
    public static boolean create(HistoryCheckIn hci){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.persist(hci);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(String id){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            HistoryCheckIn hci = em.find(
                    HistoryCheckIn.class,
                    id
            );
            if(hci == null)
                return false;
            em.remove(hci);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean update(HistoryCheckIn newInfor){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.merge(newInfor);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static List<HistoryCheckIn> findAll(){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            TypedQuery<HistoryCheckIn> query = em.createQuery(
                    "select hci from HistoryCheckIn hci",
                    HistoryCheckIn.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static HistoryCheckIn findById(String id){
        try (EntityManager em = EntityManagerUtil.getEntityManager()){
            return em.find(
                    HistoryCheckIn.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

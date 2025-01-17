package iuh.fit.dao;

import iuh.fit.models.HistoryCheckOut;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HistoryCheckOutDAO {
    public static boolean create(HistoryCheckOut hco){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.persist(hco);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(String id){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            HistoryCheckOut hco = em.find(
                    HistoryCheckOut.class,
                    id
            );
            if(hco == null)
                return false;
            em.remove(hco);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean update(HistoryCheckOut newInfor){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.merge(newInfor);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static List<HistoryCheckOut> findAll(){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            TypedQuery<HistoryCheckOut> query = em.createQuery(
                    "select hco from HistoryCheckOut hco",
                    HistoryCheckOut.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static HistoryCheckOut findById(String id){
        try (EntityManager em = EntityManagerUtil.getEntityManager()){
            return em.find(
                    HistoryCheckOut.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

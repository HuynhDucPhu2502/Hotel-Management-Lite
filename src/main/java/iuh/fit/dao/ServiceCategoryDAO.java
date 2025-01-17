package iuh.fit.dao;

import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.security.spec.ECField;
import java.util.List;

public class ServiceCategoryDAO {
    public static boolean create(ServiceCategory sc){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.persist(sc);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(String id){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            ServiceCategory sc = em.find(
                    ServiceCategory.class,
                    id
            );
            if(sc == null)
                return false;
            em.remove(sc);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean update(ServiceCategory newInfor){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            em.getTransaction().begin();
            em.merge(newInfor);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static List<ServiceCategory> findAll(){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            TypedQuery<ServiceCategory> query = em.createQuery(
                    "select sc from ServiceCategory sc",
                        ServiceCategory.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static ServiceCategory findById(String id){
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.find(
                    ServiceCategory.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

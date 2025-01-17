package iuh.fit.dao;

import iuh.fit.models.ServiceCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.security.spec.ECField;
import java.util.List;

public class ServiceCategoryDAO {
    public static boolean create(EntityManager em, ServiceCategory sc){
        try{
            em.getTransaction().begin();
            em.persist(sc);
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
                    "delete from ServiceCategory " +
                            "where serviceCategoryID = :id "
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

    public static boolean update(EntityManager em, ServiceCategory newInfor){
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

    public static List<ServiceCategory> findAll(EntityManager em){
        try{
            TypedQuery<ServiceCategory> query = em.createQuery(
                    "select sc from ServiceCategory sc",
                        ServiceCategory.class
            );

            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static ServiceCategory findById(EntityManager em, String id){
        try {
            return em.find(
                    ServiceCategory.class,
                    id
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

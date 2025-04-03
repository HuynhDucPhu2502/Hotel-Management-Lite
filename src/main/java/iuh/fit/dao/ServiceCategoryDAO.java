package iuh.fit.dao;

import iuh.fit.models.ServiceCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServiceCategoryDAO {

    public static boolean createData(ServiceCategory serviceCategory) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(serviceCategory);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteData(String serviceCategoryID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            ServiceCategory sc = em.find(ServiceCategory.class, serviceCategoryID);
            if (sc == null) return false;
            sc.setIsActivate(ObjectStatus.INACTIVE);
            em.merge(sc);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateData(ServiceCategory serviceCategory) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(serviceCategory);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<ServiceCategory> findAll() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<ServiceCategory> query = em.createQuery(
                    "SELECT sc FROM ServiceCategory sc WHERE sc.isActivate = 'ACTIVE'", ServiceCategory.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ServiceCategory findById(String serviceCategoryID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.find(ServiceCategory.class, serviceCategoryID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isServiceCategoryInUse(String serviceCategoryID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Long> query = em.createQuery(
                    """
                    SELECT COUNT(r.id) FROM Room r 
                    JOIN r.roomCategory rc 
                    WHERE rc.roomCategoryID = :serviceCategoryID 
                    AND r.roomStatus IN ('ON_USE', 'OVERDUE')
                    """, Long.class);
            query.setParameter("serviceCategoryID", serviceCategoryID);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find ServiceCategories by partial ID
    public static List<ServiceCategory> findDataByContainsId(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<ServiceCategory> query = em.createQuery(
                    "SELECT sc FROM ServiceCategory sc WHERE LOWER(sc.serviceCategoryID) LIKE :input AND sc.isActivate = 'ACTIVATE'",
                    ServiceCategory.class);
            query.setParameter("input", "%" + input.toLowerCase() + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get the top 3 ServiceCategory IDs
    public static List<String> getTopThreeID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<String> query = em.createQuery(
                    "SELECT sc.serviceCategoryID FROM ServiceCategory sc WHERE sc.isActivate = 'ACTIVATE' ORDER BY sc.serviceCategoryID DESC",
                    String.class);
            query.setMaxResults(3);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get the next ServiceCategory ID from GlobalSequence
    public static String getNextServiceCategoryID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String queryStr = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'ServiceCategory'";
            TypedQuery<String> query = em.createQuery(queryStr, String.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "SC-000001";
        }
    }

    // Get list of ServiceCategory names
    public static List<String> getServiceCategoryNames() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<String> query = em.createQuery(
                    "SELECT sc.serviceCategoryName FROM ServiceCategory sc WHERE sc.isActivate = 'ACTIVATE'",
                    String.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

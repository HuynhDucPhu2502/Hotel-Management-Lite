package iuh.fit.dao;

import iuh.fit.models.RoomCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 16/01/2025 - 5:44 PM
 * @project Hotel-Management-Plus
 * @package iuh.fit.dao
 */
public class RoomCategoryDAO {

    public static List getAll(){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            return em.createQuery(
                    "select rc from RoomCategory rc where rc.isActivate = 'ACTIVATE'"
            ).getResultList();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static RoomCategory getById(String id){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            Query query = em.createQuery(
                    "select rc from RoomCategory rc where rc.roomCategoryID = :id and rc.isActivate = 'ACTIVATE'"
            ).setParameter("id", id);

            return (RoomCategory) query.getSingleResult();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //*******************************************************
    //************** Can cai thien them *********************
    //*******************************************************
    public static void create(RoomCategory roomCategory){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            try{
                em.getTransaction().begin();
                em.persist(roomCategory);
                em.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void update(RoomCategory roomCategory) {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
        ) {
            try{
                em.getTransaction().begin();
                em.merge(roomCategory);
                em.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(RoomCategory roomCategory) {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
        ) {
            try {
                em.getTransaction().begin();
                em.remove(roomCategory);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(String id) {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
        ) {
            try {
                em.getTransaction().begin();
                RoomCategory roomCategory = em.find(RoomCategory.class, id);
                em.remove(roomCategory);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

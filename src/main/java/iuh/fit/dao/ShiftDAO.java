package iuh.fit.dao;

import iuh.fit.models.Shift;
import iuh.fit.models.ShiftAssignment;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 17/01/2025 - 9:36 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.dao
 */

public class ShiftDAO {

    public static List<Shift> findAll(){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            return (List<Shift>) em.createQuery(
                    "select s from Shift s"
            ).getResultList();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<ShiftAssignment> findAllShiftAssignment(Shift shift){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){

            return (List<ShiftAssignment>) em.createQuery(
                    "select sa from ShiftAssignment sa where sa.shift = :shift");

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Shift findById(String id){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            return em.find(Shift.class, id);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static void create(Shift shift){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            try{
                em.getTransaction().begin();
                em.persist(shift);
                em.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void update(Shift shift){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            try{
                em.getTransaction().begin();
                em.merge(shift);
                em.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void delete(String id){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            try{
                em.getTransaction().begin();
                Shift shift = em.find(Shift.class, id);
                em.remove(shift);
                em.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

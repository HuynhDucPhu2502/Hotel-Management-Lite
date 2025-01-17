package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.ShiftAssignment;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 17/01/2025 - 11:13 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.dao
 */
public class ShiftAssignmentDAO {

    public static List<ShiftAssignment> findAll() {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            return (List<ShiftAssignment>) em.createQuery(
                    "select sa from ShiftAssignment sa"
            ).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ShiftAssignment findById(String id) {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            return em.find(ShiftAssignment.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void create(ShiftAssignment shiftAssignment, Shift shift, Employee employee) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try {
                em.getTransaction().begin();

                // Đồng nhất Shift, Employee để đảm bảo trong cùng một phiên
                shift = em.merge(shift);
                employee = em.merge(employee);

                em.persist(shiftAssignment);

                // Thêm shiftAssignment vào Shift và Employee trong cùng phiên
                shift.getShiftAssignments().add(shiftAssignment);
                employee.getShiftAssignments().add(shiftAssignment);

                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }
    }


    public static void update(ShiftAssignment shiftAssignment) {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            try {
                em.getTransaction().begin();
                em.merge(shiftAssignment);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }
    }

    public static void delete(String id) {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            try {
                em.getTransaction().begin();
                ShiftAssignment shiftAssignment = em.find(ShiftAssignment.class, id);
                em.remove(shiftAssignment);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }
    }

    public static void deleteByShift(Shift shift) {
        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
        ){
            try {
                em.getTransaction().begin();

                List<ShiftAssignment> shiftAssignments = em.createQuery(
                        "select sa from ShiftAssignment sa where sa.shift = :shift"
                ).setParameter("shift", shift).getResultList();

                for (ShiftAssignment shiftAssignment : shiftAssignments) {
                    em.remove(shiftAssignment);
                }
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }
    }

    public static void updateWhenAddingShiftAssignment(ShiftAssignment shiftAssignment, Shift shift, Employee employee){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            try{
                em.getTransaction().begin();

                shift.getShiftAssignments().add(shiftAssignment);
                ShiftDAO.update(shift);
                employee.getShiftAssignments().add(shiftAssignment);
                EmployeeDAO.update(employee);

                em.getTransaction().commit();
            }catch (Exception e){
                em.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }
}

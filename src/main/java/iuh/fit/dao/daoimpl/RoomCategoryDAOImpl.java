package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.RoomCategoryDAO;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RoomCategoryDAOImpl extends UnicastRemoteObject implements RoomCategoryDAO {

    public RoomCategoryDAOImpl() throws RemoteException {
    }

    @Override
    public List<RoomCategory> getRoomCategory() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = "SELECT rc FROM RoomCategory rc WHERE rc.isActivate = :status";
            TypedQuery<RoomCategory> query = em.createQuery(jpql, RoomCategory.class);
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        }
    }

    @Override
    public RoomCategory getDataByID(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.find(RoomCategory.class, id);
        }
    }

    @Override
    public void createData(RoomCategory rc) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            String jpql = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'RoomCategory'";
            String currentId = em.createQuery(jpql, String.class).getSingleResult();
            rc.setRoomCategoryID(currentId);

            em.persist(rc);

            // Tăng nextID
            String prefix = "RC-";
            int next = Integer.parseInt(currentId.substring(prefix.length())) + 1;
            String newId = prefix + String.format("%06d", next);

            em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newId WHERE gs.tableName = 'RoomCategory'")
                    .setParameter("newId", newId)
                    .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateData(RoomCategory rc) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(rc);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteData(String id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            RoomCategory rc = em.find(RoomCategory.class, id);
            if (rc != null) {
                rc.setIsActivate(ObjectStatus.INACTIVE);
                em.merge(rc);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<RoomCategory> findDataByContainsId(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = "SELECT rc FROM RoomCategory rc WHERE LOWER(rc.roomCategoryID) LIKE :input AND rc.isActivate = :status";
            TypedQuery<RoomCategory> query = em.createQuery(jpql, RoomCategory.class);
            query.setParameter("input", "%" + input.toLowerCase() + "%");
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        }
    }

    @Override
    public boolean checkAllowUpdateOrDelete(String roomCategoryID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        String jpql = "SELECT COUNT(r.roomID) FROM Room r WHERE r.roomCategory.roomCategoryID = :id AND (r.roomStatus = 'ON_USE' OR r.roomStatus = 'OVERDUE') AND r.isActivate = 'ACTIVE'";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("id", roomCategoryID)
                .getSingleResult();
        em.close();
        return count == 0;
    }

    @Override
    public List<String> getTopThreeID() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        String jpql = "SELECT rc.roomCategoryID FROM RoomCategory rc WHERE rc.isActivate = :status ORDER BY rc.roomCategoryID DESC";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter("status", ObjectStatus.ACTIVE);
        query.setMaxResults(3);
        List<String> result = query.getResultList();
        em.close();
        return result;
    }

    @Override
    public String getNextRoomCategoryID() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        String jpql = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'RoomCategory'";
        String id = em.createQuery(jpql, String.class).getSingleResult();
        em.close();
        return id;
    }
}

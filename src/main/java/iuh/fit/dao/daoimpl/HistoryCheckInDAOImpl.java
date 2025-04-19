package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.HistoryCheckInDAO;
import iuh.fit.models.HistoryCheckIn;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryCheckInDAOImpl extends UnicastRemoteObject implements HistoryCheckInDAO {
    public HistoryCheckInDAOImpl() throws RemoteException {
    }

    public List<HistoryCheckIn> getAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();

        TypedQuery<HistoryCheckIn> query = em.createQuery("""
            SELECT h FROM HistoryCheckIn h
            JOIN FETCH h.reservationForm rf
            JOIN FETCH rf.room r
            JOIN FETCH rf.customer c
            JOIN FETCH rf.employee e
            JOIN FETCH r.roomCategory
        """, HistoryCheckIn.class);

        return query.getResultList();
    }

    public HistoryCheckIn getByID(String historyCheckInID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.find(HistoryCheckIn.class, historyCheckInID);
    }

    public String getNextID() {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String nextID = em.createQuery("""
            SELECT gs.nextID FROM GlobalSequence gs
            WHERE gs.tableName = 'HistoryCheckin'
        """, String.class).getSingleResult();

        int numeric = Integer.parseInt(nextID.substring(4)) + 1;
        String newNextID = "HCI-" + String.format("%06d", numeric);

        em.getTransaction().begin();
        em.createQuery("""
            UPDATE GlobalSequence gs
            SET gs.nextID = :newID
            WHERE gs.tableName = 'HistoryCheckin'
        """)
                .setParameter("newID", newNextID)
                .executeUpdate();
        em.getTransaction().commit();

        return nextID;
    }

    public LocalDateTime getActualCheckInDate(String reservationFormID) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        TypedQuery<LocalDateTime> query = em.createQuery("""
            SELECT h.checkInDate FROM HistoryCheckIn h
            WHERE h.reservationForm.reservationID = :reservationFormID
        """, LocalDateTime.class);

        query.setParameter("reservationFormID", reservationFormID);

        List<LocalDateTime> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}

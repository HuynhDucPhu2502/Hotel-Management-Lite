package iuh.fit.dao;

import iuh.fit.models.HistoryCheckOut;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class HistoryCheckOutDAO {

    public static List<HistoryCheckOut> getHistoryCheckOut() {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT h FROM HistoryCheckOut h
            WHERE h.reservationForm IS NOT NULL
        """;

        return em.createQuery(jpql, HistoryCheckOut.class).getResultList();
    }

    public static HistoryCheckOut getDataByID(String historyCheckOutID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.find(HistoryCheckOut.class, historyCheckOutID);
    }

    public static LocalDateTime getActualCheckOutDate(String reservationFormID) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT h.dateOfCheckingOut FROM HistoryCheckOut h
            WHERE h.reservationForm.reservationID = :reservationFormID
        """;

        TypedQuery<LocalDateTime> query = em.createQuery(jpql, LocalDateTime.class);
        query.setParameter("reservationFormID", reservationFormID);

        return query.getResultStream().findFirst().orElse(null);
    }

    public static void incrementAndUpdateNextID() {
        EntityManager em = EntityManagerUtil.getEntityManager();

        em.getTransaction().begin();

        String currentNextID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'HistoryCheckOut'", String.class)
                .getSingleResult();

        String prefix = "HCO-";
        int numericPart = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
        String updatedNextID = prefix + String.format("%06d", numericPart);

        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'HistoryCheckOut'")
                .setParameter("newID", updatedNextID)
                .executeUpdate();

        em.getTransaction().commit();
    }
}

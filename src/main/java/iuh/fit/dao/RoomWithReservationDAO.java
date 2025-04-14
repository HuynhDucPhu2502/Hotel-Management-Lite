package iuh.fit.dao;

import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class RoomWithReservationDAO {

    public static List<RoomWithReservation> getRoomWithReservation() {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            LEFT JOIN r.roomCategory rc
            LEFT JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE r.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
              AND (rf IS NULL OR hco IS NULL AND :now BETWEEN rf.approxcheckInDate AND rf.approxcheckOutTime)
              AND r.roomStatus <> iuh.fit.models.enums.RoomStatus.UNAVAILABLE
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("now", LocalDateTime.now());

        return query.getResultList();
    }

    public static RoomWithReservation getRoomWithReservationByRoomId(String roomId) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            LEFT JOIN r.roomCategory rc
            LEFT JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE r.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
              AND r.roomID = :roomId
              AND (rf IS NULL OR hco IS NULL AND :now BETWEEN rf.approxcheckInDate AND rf.approxcheckOutTime)
              AND r.roomStatus <> iuh.fit.models.enums.RoomStatus.UNAVAILABLE
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("roomId", roomId);
        query.setParameter("now", LocalDateTime.now());

        return query.getSingleResult();
    }

    public static RoomWithReservation getRoomWithReservationByID(String reservationId, String roomId) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            JOIN r.roomCategory rc
            JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE rf.reservationID = :reservationId
              AND r.roomID = :roomId
              AND hco IS NULL
              AND r.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
              AND r.roomStatus <> iuh.fit.models.enums.RoomStatus.UNAVAILABLE
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("reservationId", reservationId);
        query.setParameter("roomId", roomId);

        return query.getSingleResult();
    }

    public static List<RoomWithReservation> getRoomOverDueWithLatestReservation() {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            JOIN r.roomCategory rc
            JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE r.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
              AND r.roomStatus IN (iuh.fit.models.enums.RoomStatus.OVER_DUE, iuh.fit.models.enums.RoomStatus.IN_USE)
              AND hco IS NULL
              AND rf.approxcheckOutTime < :now
              AND r.roomStatus <> iuh.fit.models.enums.RoomStatus.UNAVAILABLE
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("now", LocalDateTime.now());

        return query.getResultList();
    }
}

package iuh.fit.dao;

import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class RoomWithReservationDAO {

    public static List<RoomWithReservation> getRoomWithReservation() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            LEFT JOIN r.roomCategory rc
            LEFT JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE r.isActivate = :activeStatus
              AND rc.isActivate = :activeStatus
              AND r.roomStatus <> :unavailableStatus
              AND (
                  rf IS NULL
                  OR (
                      rf.historyCheckIn IS NOT NULL
                      AND hco IS NULL
                      AND rf.approxcheckInDate <= :now
                      AND rf.approxcheckOutTime >= :now
                  )
              )
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("activeStatus", ObjectStatus.ACTIVE);
        query.setParameter("unavailableStatus", RoomStatus.UNAVAILABLE);
        query.setParameter("now", now);

        return query.getResultList();
    }

    public static RoomWithReservation getRoomWithReservationByRoomId(String roomId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            LEFT JOIN r.roomCategory rc
            LEFT JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE r.roomID = :roomId
              AND r.isActivate = :activeStatus
              AND rc.isActivate = :activeStatus
              AND r.roomStatus <> :unavailableStatus
              AND (
                  rf IS NULL
                  OR (
                      rf.historyCheckIn IS NOT NULL
                      AND hco IS NULL
                      AND rf.approxcheckInDate <= :now
                      AND rf.approxcheckOutTime >= :now
                  )
              )
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("roomId", roomId);
        query.setParameter("activeStatus", ObjectStatus.ACTIVE);
        query.setParameter("unavailableStatus", RoomStatus.UNAVAILABLE);
        query.setParameter("now", now);

        return query.getSingleResult();
    }

    public static RoomWithReservation getRoomWithReservationByID(String reservationFormID, String roomID) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            LEFT JOIN r.roomCategory rc
            LEFT JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE rf.reservationID = :reservationID
              AND r.roomID = :roomID
              AND r.isActivate = :activeStatus
              AND rc.isActivate = :activeStatus
              AND rf.customer.isActivate = :activeStatus
              AND rf.employee.isActivate = :activeStatus
              AND r.roomStatus <> :unavailableStatus
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("reservationID", reservationFormID);
        query.setParameter("roomID", roomID);
        query.setParameter("activeStatus", ObjectStatus.ACTIVE);
        query.setParameter("unavailableStatus", RoomStatus.UNAVAILABLE);

        return query.getSingleResult();
    }

    public static List<RoomWithReservation> getRoomOverDueWithLatestReservation() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();

        String jpql = """
            SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
            FROM Room r
            LEFT JOIN r.roomCategory rc
            JOIN ReservationForm rf ON rf.room = r
            LEFT JOIN rf.historyCheckOut hco
            WHERE rf.approxcheckOutTime < :now
              AND hco IS NULL
              AND r.roomStatus IN (:overdue, :inUse)
              AND r.isActivate = :activeStatus
              AND rc.isActivate = :activeStatus
              AND rf.customer.isActivate = :activeStatus
              AND rf.employee.isActivate = :activeStatus
              AND r.roomStatus <> :unavailableStatus
        """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("now", now);
        query.setParameter("overdue", RoomStatus.OVER_DUE);
        query.setParameter("inUse", RoomStatus.IN_USE);
        query.setParameter("activeStatus", ObjectStatus.ACTIVE);
        query.setParameter("unavailableStatus", RoomStatus.UNAVAILABLE);

        return query.getResultList();
    }
}

package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.RoomWithReservationDAO;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class RoomWithReservationDAOImpl extends UnicastRemoteObject implements RoomWithReservationDAO {

    public RoomWithReservationDAOImpl() throws RemoteException {
    }

    @Override
    public List<RoomWithReservation> getRoomWithReservation() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus2Hours = now.plusHours(2);

        String jpql = """
        SELECT new iuh.fit.models.wrapper.RoomWithReservation(r, rf)
        FROM Room r
        LEFT JOIN r.roomCategory rc
        LEFT JOIN ReservationForm rf ON rf.room = r
            AND rf.historyCheckOut IS NULL
            AND :now BETWEEN rf.approxcheckInDate AND :nowPlus2Hours
        WHERE r.isActivate = :activeStatus
          AND rc.isActivate = :activeStatus
          AND r.roomStatus <> :unavailableStatus
    """;

        TypedQuery<RoomWithReservation> query = em.createQuery(jpql, RoomWithReservation.class);
        query.setParameter("now", now);
        query.setParameter("nowPlus2Hours", nowPlus2Hours);
        query.setParameter("activeStatus", ObjectStatus.ACTIVE);
        query.setParameter("unavailableStatus", RoomStatus.UNAVAILABLE);
        query.setParameter("now", now);

        return query.getResultList();
    }

    @Override
    public RoomWithReservation getRoomWithReservationByRoomId(String roomId) {
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
                      hco IS NULL
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

    @Override
    public RoomWithReservation getRoomWithReservationByID(String reservationFormID, String roomID) {
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

    @Override
    public List<RoomWithReservation> getRoomOverDueWithLatestReservation() {
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
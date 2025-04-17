package iuh.fit.dao;

import iuh.fit.models.ReservationRoomDetail;

import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin 4/16/2025
 **/
public class ReservationRoomDetailDAO {
    public static List<ReservationRoomDetail> getAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TypedQuery<ReservationRoomDetail> query = em.createQuery("SELECT r FROM ReservationRoomDetail r", ReservationRoomDetail.class);
        return query.getResultList();
    }

    public static List<ReservationRoomDetail> getByReservationFormID(String reservationFormID) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        TypedQuery<ReservationRoomDetail> query = em.createQuery("""
            SELECT r FROM ReservationRoomDetail r
            WHERE r.reservationForm.reservationID = :reservationFormID
              AND r.room.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
              AND r.reservationForm.employee.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
        """, ReservationRoomDetail.class);

        query.setParameter("reservationFormID", reservationFormID);
        return query.getResultList();
    }

    public static void createData(ReservationRoomDetail detail) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        String nextID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'ReservationRoomDetail'", String.class).getSingleResult();
        int numeric = Integer.parseInt(nextID.substring(4)) + 1;
        String newNextID = "RRD-" + String.format("%06d", numeric);

        detail.setReservationRoomDetailID(nextID);

        em.getTransaction().begin();
        em.persist(detail);
        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'ReservationRoomDetail'")
                .setParameter("newID", newNextID)
                .executeUpdate();
        em.getTransaction().commit();
    }

    public static void changingRoom(String currentRoomID, String newRoomID, String reservationFormID, String employeeID) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        ReservationForm form = em.find(ReservationForm.class, reservationFormID);
        if (form == null || form.getApproxcheckOutTime().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("ROOM_CHANGING_RESERVATION_NOT_FOUND_OR_EXPIRED");

        Room currentRoom = em.find(Room.class, currentRoomID);
        Room newRoom = em.find(Room.class, newRoomID);
        if (newRoom == null || newRoom.getRoomStatus() != RoomStatus.AVAILABLE || !newRoom.getIsActivate().equals(iuh.fit.models.enums.ObjectStatus.ACTIVE))
            throw new IllegalArgumentException("ROOM_CHANGING_NEW_ROOM_NOT_AVAILABLE");

        String nextID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'ReservationRoomDetail'", String.class).getSingleResult();
        int numeric = Integer.parseInt(nextID.substring(4)) + 1;
        String newNextID = "RRD-" + String.format("%06d", numeric);

        em.getTransaction().begin();

        form.setRoom(newRoom);
        currentRoom.setRoomStatus(RoomStatus.AVAILABLE);
        newRoom.setRoomStatus(RoomStatus.IN_USE);

        ReservationRoomDetail detail = new ReservationRoomDetail();
        detail.setReservationRoomDetailID(nextID);
        detail.setDateChanged(LocalDateTime.now());
        detail.setRoom(newRoom);
        detail.setReservationForm(form);

        em.persist(detail);
        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'ReservationRoomDetail'")
                .setParameter("newID", newNextID)
                .executeUpdate();

        em.getTransaction().commit();
    }

    public static String roomCheckingIn(String reservationFormID, String employeeID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();

        // 👉 Tính trước giới hạn thời gian
        LocalDateTime checkInWindowStart;
        LocalDateTime checkInWindowEnd;

        TypedQuery<ReservationForm> query = em.createQuery("""
        SELECT rf FROM ReservationForm rf
        WHERE rf.reservationID = :id
        AND :now BETWEEN rf.approxcheckInDate AND :checkInDeadline
        AND rf.historyCheckIn IS NULL
    """, ReservationForm.class);

        // 👇 Tạm thời phải tìm approxcheckInDate để tính cộng 2h
        ReservationForm tempForm = em.find(ReservationForm.class, reservationFormID);
        if (tempForm == null) return "ROOM_CHECKING_IN_INVALID_RESERVATION";
        checkInWindowStart = tempForm.getApproxcheckInDate();
        checkInWindowEnd = checkInWindowStart.plusHours(2);

        query.setParameter("id", reservationFormID);
        query.setParameter("now", now);
        query.setParameter("checkInDeadline", checkInWindowEnd);

        List<ReservationForm> results = query.getResultList();
        if (results.isEmpty()) return "ROOM_CHECKING_IN_INVALID_RESERVATION";

        ReservationForm form = results.get(0);
        Room room = form.getRoom();

        // ✅ Đoạn còn lại giữ nguyên như bạn đã viết
        em.getTransaction().begin();

        String nextID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'HistoryCheckin'", String.class).getSingleResult();
        int nextNumeric = Integer.parseInt(nextID.substring(4)) + 1;
        String newNextID = "HCI-" + String.format("%06d", nextNumeric);

        HistoryCheckIn checkIn = new HistoryCheckIn();
        checkIn.setRoomHistoryCheckinID(nextID);
        checkIn.setCheckInDate(now);
        checkIn.setReservationForm(form);

        form.setHistoryCheckIn(checkIn);
        room.setRoomStatus(RoomStatus.IN_USE);

        em.persist(checkIn);
        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'HistoryCheckin'")
                .setParameter("newID", newNextID)
                .executeUpdate();

        String nextRRDID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'ReservationRoomDetail'", String.class).getSingleResult();
        int nextRRD = Integer.parseInt(nextRRDID.substring(4)) + 1;
        String newRRDID = "RRD-" + String.format("%06d", nextRRD);

        ReservationRoomDetail rrd = new ReservationRoomDetail();
        rrd.setReservationRoomDetailID(nextRRDID);
        rrd.setDateChanged(now);
        rrd.setRoom(room);
        rrd.setReservationForm(form);

        em.persist(rrd);
        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'ReservationRoomDetail'")
                .setParameter("newID", newRRDID)
                .executeUpdate();

        em.getTransaction().commit();

        return "ROOM_CHECKING_IN_SUCCESS";
    }


    public static String roomEarlyCheckingIn(String reservationFormID, String employeeID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();

        ReservationForm form = em.find(ReservationForm.class, reservationFormID);
        if (form == null || form.getHistoryCheckIn() != null)
            return "ROOM_CHECKING_IN_INVALID_RESERVATION";

        LocalDateTime checkInTime = form.getApproxcheckInDate();
        LocalDateTime earlyStart = checkInTime.minusMinutes(30);
        LocalDateTime earlyEnd = checkInTime.minusSeconds(10);

        if (now.isBefore(earlyStart) || now.isAfter(earlyEnd))
            return "ROOM_CHECKING_IN_TIME_INVALID";

        em.getTransaction().begin();

        // HistoryCheckIn
        String nextHCI = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'HistoryCheckin'", String.class).getSingleResult();
        int nextHCINum = Integer.parseInt(nextHCI.substring(4)) + 1;
        String newHCIID = "HCI-" + String.format("%06d", nextHCINum);

        HistoryCheckIn checkIn = new HistoryCheckIn();
        checkIn.setRoomHistoryCheckinID(nextHCI);
        checkIn.setCheckInDate(now);
        checkIn.setReservationForm(form);

        em.persist(checkIn);
        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'HistoryCheckin'")
                .setParameter("newID", newHCIID)
                .executeUpdate();

        // ReservationRoomDetail
        String nextRRD = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'ReservationRoomDetail'", String.class).getSingleResult();
        int nextRRDNum = Integer.parseInt(nextRRD.substring(4)) + 1;
        String newRRDID = "RRD-" + String.format("%06d", nextRRDNum);

        ReservationRoomDetail rrd = new ReservationRoomDetail();
        rrd.setReservationRoomDetailID(nextRRD);
        rrd.setDateChanged(now);
        rrd.setRoom(form.getRoom());
        rrd.setReservationForm(form);

        em.persist(rrd);
        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'ReservationRoomDetail'")
                .setParameter("newID", newRRDID)
                .executeUpdate();

        // Update trạng thái
        form.setHistoryCheckIn(checkIn);
        form.setApproxcheckInDate(now);
        form.setBookingDeposit(form.getBookingDeposit() - 50000);
        form.getRoom().setRoomStatus(RoomStatus.IN_USE);

        em.getTransaction().commit();

        return "ROOM_CHECKING_IN_SUCCESS";
    }

}

package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.ReservationStatus;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


import java.time.LocalDateTime;
import java.util.List;

public class ReservationFormDAO {


    public static String createReservationForm(ReservationForm form) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        TypedQuery<Long> overlapQuery = em.createQuery("""
                SELECT COUNT(rf) FROM ReservationForm rf
                WHERE rf.room.roomID = :roomID
                AND :checkIn < rf.approxcheckOutTime
                AND :checkOut > rf.approxcheckInDate
            """, Long.class);
        overlapQuery.setParameter("roomID", form.getRoom().getRoomID());
        overlapQuery.setParameter("checkIn", form.getApproxcheckInDate());
        overlapQuery.setParameter("checkOut", form.getApproxcheckOutTime());
        if (overlapQuery.getSingleResult() > 0)
            return "CREATING_RESERVATION_FORM_CHECK_DATE_OVERLAP";

        TypedQuery<Long> idCardCheck = em.createQuery("""
                SELECT COUNT(rf) FROM ReservationForm rf
                JOIN rf.customer c
                WHERE c.customerCode = :customerID
                AND :checkIn < rf.approxcheckOutTime
                AND :checkOut > rf.approxcheckInDate
            """, Long.class);
        idCardCheck.setParameter("customerID", form.getCustomer().getCustomerCode());
        idCardCheck.setParameter("checkIn", form.getApproxcheckInDate());
        idCardCheck.setParameter("checkOut", form.getApproxcheckOutTime());
        if (idCardCheck.getSingleResult() > 0)
            return "CREATING_RESERVATION_FORM_ID_CARD_NUMBER_OVERLAP";

        try {
            em.getTransaction().begin();

            // Lấy nextID từ GlobalSequence
            String jpqlSelect = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = :tableName";
            TypedQuery<String> querySelect = em.createQuery(jpqlSelect, String.class);
            querySelect.setParameter("tableName", "ReservationForm");
            String currentNextID = querySelect.getSingleResult();

            form.setReservationID(currentNextID);
            form.setReservationDate(LocalDateTime.now());
            form.setReservationStatus(ReservationStatus.RESERVATION);

            // Cập nhật nextID
            String prefix = "RF-";
            int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
            String newNextID = prefix + String.format("%06d", nextIDNum);

            String jpqlUpdate = "UPDATE GlobalSequence gs SET gs.nextID = :newNextID WHERE gs.tableName = :tableName";
            em.createQuery(jpqlUpdate)
                    .setParameter("newNextID", newNextID)
                    .setParameter("tableName", "ReservationForm")
                    .executeUpdate();

            // Lưu reservation form và cập nhật trạng thái phòng
            em.persist(form);
            form.getRoom().setRoomStatus(RoomStatus.IN_USE);
            em.getTransaction().commit();

            return "CREATING_RESERVATION_FORM_SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return e.getMessage();
        }
    }


//    public static String changeRoom(String currentRoomID, String newRoomID, String reservationFormID, String employeeID) {
//        EntityManager em = EntityManagerUtil.getEntityManager();
//        ReservationForm form = em.find(ReservationForm.class, reservationFormID);
//        if (form == null || form.getApproxcheckOutTime().isBefore(LocalDateTime.now()))
//            return "ROOM_CHANGING_RESERVATION_NOT_FOUND_OR_EXPIRED";
//
//        Room newRoom = em.find(Room.class, newRoomID);
//        if (newRoom == null || newRoom.getRoomStatus() != RoomStatus.AVAILABLE || newRoom.getIsActivate() != ObjectStatus.ACTIVE)
//            return "ROOM_CHANGING_NEW_ROOM_NOT_AVAILABLE";
//
//        em.getTransaction().begin();
//
//        form.getRoom().setRoomStatus(RoomStatus.AVAILABLE);
//        form.setRoom(newRoom);
//        newRoom.setRoomStatus(RoomStatus.IN_USE);
//
//        RoomReservationDetailDAO.createTransferDetail(form, employeeID, currentRoomID, newRoomID);
//
//        em.getTransaction().commit();
//
//        return "ROOM_CHANGING_SUCCESS";
//    }

    public static List<ReservationForm> getUpcomingReservations(String roomID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);

        TypedQuery<ReservationForm> query = em.createQuery("""
                SELECT rf FROM ReservationForm rf
                WHERE rf.room.roomID = :roomID
                AND rf.approxcheckInDate >= :timeThreshold
                AND rf.historyCheckIn IS NULL
                ORDER BY rf.approxcheckInDate
            """, ReservationForm.class);

        query.setParameter("roomID", roomID);
        query.setParameter("timeThreshold", twoHoursAgo);
        return query.getResultList();
    }

    public static List<ReservationForm> getReservationFormByCustomerID(String customerID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TypedQuery<ReservationForm> query = em.createQuery("""
                SELECT rf FROM ReservationForm rf
                WHERE rf.customer.customerCode = :customerID
            """, ReservationForm.class);
        query.setParameter("customerID", customerID);
        return query.getResultList();
    }

    public static List<ReservationForm> getReservationsWithinLastMonth(String roomID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        TypedQuery<ReservationForm> query = em.createQuery("""
                SELECT rf FROM ReservationForm rf
                WHERE rf.room.roomID = :roomID
                AND rf.approxcheckInDate >= :threshold
                ORDER BY rf.approxcheckInDate
            """, ReservationForm.class);
        query.setParameter("roomID", roomID);
        query.setParameter("threshold", oneMonthAgo);
        return query.getResultList();
    }

    public static ReservationForm getDataByID(String id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.find(ReservationForm.class, id);
    }

    public static void deleteData(String id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        ReservationForm form = em.find(ReservationForm.class, id);
        if (form == null) return;
        em.getTransaction().begin();
        em.remove(form);
        em.getTransaction().commit();
    }

    public static void updateData(ReservationForm form) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(form);
        em.getTransaction().commit();
    }
}

package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.HotelService;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.RoomUsageService;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RoomUsageServiceDAO {

    public static List<RoomUsageService> getByReservationFormID(String reservationFormID) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        TypedQuery<RoomUsageService> query = em.createQuery("""
            SELECT r FROM RoomUsageService r
            WHERE r.reservationForm.reservationID = :reservationFormID
              AND r.hotelService.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
              AND r.hotelService.serviceCategory.isActivate = iuh.fit.models.enums.ObjectStatus.ACTIVE
        """, RoomUsageService.class);

        query.setParameter("reservationFormID", reservationFormID);
        return query.getResultList();
    }

    public static String serviceOrdering(RoomUsageService roomUsageService) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        ReservationForm form = em.find(ReservationForm.class, roomUsageService.getReservationForm().getReservationID());

        if (form == null || form.getApproxcheckOutTime().isBefore(LocalDateTime.now())) {
            return "ROOM_SERVICE_ORDERING_RESERVATION_NOT_FOUND_OR_EXPIRED";
        }

        if (roomUsageService.getQuantity() <= 0) {
            return "SERVICE_ORDERING_INVALID_QUANTITY";
        }

        String nextID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'RoomUsageService'", String.class).getSingleResult();
        int nextNum = Integer.parseInt(nextID.substring(4)) + 1;
        String newNextID = "RUS-" + String.format("%06d", nextNum);

        em.getTransaction().begin();
        roomUsageService.setRoomUsageServiceID(nextID);
        em.persist(roomUsageService);

        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'RoomUsageService'")
                .setParameter("newID", newNextID)
                .executeUpdate();

        em.getTransaction().commit();

        return "SERVICE_ORDERING_SUCCESS";
    }
}

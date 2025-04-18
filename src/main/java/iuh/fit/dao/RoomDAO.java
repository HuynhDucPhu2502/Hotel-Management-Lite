package iuh.fit.dao;

import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.*;

public class RoomDAO {

    public static List<Room> getRoom() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r WHERE r.isActivate = :status", Room.class);
        query.setParameter("status", ObjectStatus.ACTIVE);
        return query.getResultList();
    }

    public static Room getDataByID(String roomID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.find(Room.class, roomID);
    }

    public static void createData(Room room) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
    }

    public static void deleteData(String roomID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        Room room = em.find(Room.class, roomID);
        if (room != null) {
            room.setIsActivate(ObjectStatus.INACTIVE);
            em.merge(room);
        }
        em.getTransaction().commit();
    }

    public static void updateData(String oldRoomID, String oldCategory, Room newRoom) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        Room oldRoom = em.find(Room.class, oldRoomID);
        if (oldRoom != null && oldRoom.getRoomCategory().getRoomCategoryID().equals(oldCategory)) {
            em.remove(oldRoom);
            em.persist(newRoom);
        }
        em.getTransaction().commit();
    }

    public static List<Room> findDataByAnyContainsId(String input) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TypedQuery<Room> query = em.createQuery(
                "SELECT r FROM Room r WHERE LOWER(r.roomID) LIKE :input AND r.isActivate = :status", Room.class);
        query.setParameter("input", "%" + input.toLowerCase() + "%");
        query.setParameter("status", ObjectStatus.ACTIVE);
        return query.getResultList();
    }

    public static String roomIDGenerate(int floorNumb, RoomCategory roomCategory) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        String prefix = roomCategory.getRoomCategoryName().contains("Thường") ? "T" : "V";
        String numbOfBed = String.valueOf(roomCategory.getNumberOfBed());
        String floorStr = String.valueOf(floorNumb);

        TypedQuery<String> query = em.createQuery(
                "SELECT r.roomID FROM Room r WHERE SUBSTRING(r.roomID, 3, 1) = :floor", String.class);
        query.setParameter("floor", floorStr);

        int maxNumb = query.getResultList().stream()
                .map(id -> Integer.parseInt(id.substring(id.length() - 2)))
                .max(Integer::compare).orElse(0);

        if (maxNumb >= 99) throw new RuntimeException("Đã vượt quá số phòng trong tầng!");
        int next = maxNumb + 1;
        return String.format("%s%s%s%02d", prefix, numbOfBed, floorStr, next);
    }

    public static void updateRoomStatus(String roomID, RoomStatus newStatus) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        Room room = em.find(Room.class, roomID);
        if (room != null && room.getIsActivate() == ObjectStatus.ACTIVE) {
            room.setRoomStatus(newStatus);
            em.merge(room);
        }
        em.getTransaction().commit();
    }

    public static List<Room> getAvailableRoomsUntil(String roomID, String roomCategoryID, LocalDateTime checkout) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        LocalDateTime checkoutPlus2Hours = checkout.plusHours(2);

        String jpql = """
        SELECT r FROM Room r
        WHERE r.roomID != :rid
          AND r.roomCategory.roomCategoryID = :rcid
          AND r.isActivate = :status
          AND NOT EXISTS (
            SELECT 1 FROM ReservationForm rf
            WHERE rf.room.roomID = r.roomID
              AND rf.approxcheckInDate < :checkout
              AND rf.approxcheckOutTime > :now
          )
    """;

        TypedQuery<Room> query = em.createQuery(jpql, Room.class);
        query.setParameter("rid", roomID);
        query.setParameter("rcid", roomCategoryID);
        query.setParameter("status", ObjectStatus.ACTIVE);
        query.setParameter("checkout", checkout);
        query.setParameter("now", checkoutPlus2Hours);

        return query.getResultList();
    }


//    public static List<Room> getAvailableRoomsInDateRange(LocalDateTime checkIn, LocalDateTime checkOut) {
//        EntityManager em = EntityManagerUtil.getEntityManager();
//        TypedQuery<Room> query = em.createQuery("""
//            SELECT r FROM Room r
//            WHERE r.isActivate = :status AND NOT EXISTS (
//                SELECT 1 FROM ReservationForm rf
//                WHERE rf.room.roomID = r.roomID
//                  AND :checkOut > rf.checkInDate
//                  AND :checkIn < rf.checkOutDate
//            )
//        """, Room.class);
//        query.setParameter("status", ObjectStatus.ACTIVE);
//        query.setParameter("checkIn", checkIn);
//        query.setParameter("checkOut", checkOut);
//        return query.getResultList();
//    }

    public static Map<RoomStatus, Long> getRoomStatusCount() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TypedQuery<Object[]> query = em.createQuery("""
            SELECT r.roomStatus, COUNT(r) FROM Room r GROUP BY r.roomStatus
        """, Object[].class);

        Map<RoomStatus, Long> result = new HashMap<>();
        for (Object[] row : query.getResultList()) {
            result.put((RoomStatus) row[0], (Long) row[1]);
        }
        return result;
    }

    public static List<Room> searchRooms(
            String roomID, String roomStatus,
            LocalDateTime lowerBoundDate, LocalDateTime upperBoundDate,
            String roomCategoryID
    ) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
            SELECT r FROM Room r
            WHERE (:roomID IS NULL OR r.roomID LIKE CONCAT('%', :roomID, '%'))
            AND (:roomStatus IS NULL OR r.roomStatus = :roomStatus)
            AND (:lowerDate IS NULL OR r.dateOfCreation >= :lowerDate)
            AND (:upperDate IS NULL OR r.dateOfCreation <= :upperDate)
            AND (
                :roomCategoryID IS NULL
                OR :roomCategoryID = 'ALL'
                OR (r.roomCategory.roomCategoryID = :roomCategoryID)
                OR (:roomCategoryID = 'NULL' AND r.roomCategory IS NULL)
            )
            AND r.isActivate = :status
        """;

            TypedQuery<Room> query = em.createQuery(jpql, Room.class);

            query.setParameter("roomID", roomID);
            query.setParameter("roomStatus", roomStatus != null ? RoomStatus.valueOf(roomStatus) : null);
            query.setParameter("lowerDate", lowerBoundDate);
            query.setParameter("upperDate", upperBoundDate);
            query.setParameter("roomCategoryID", roomCategoryID);
            query.setParameter("status", ObjectStatus.ACTIVE);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}

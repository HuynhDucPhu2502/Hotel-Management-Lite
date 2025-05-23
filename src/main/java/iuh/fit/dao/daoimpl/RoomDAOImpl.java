package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.RoomDAO;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.*;

public class RoomDAOImpl extends UnicastRemoteObject implements RoomDAO {

    public RoomDAOImpl() throws RemoteException {
    }

    @Override
    public List<Room> getRoom() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r WHERE r.isActivate = :status", Room.class);
        query.setParameter("status", ObjectStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public Room getDataByID(String roomID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.find(Room.class, roomID);
    }

    @Override
    public void createData(Room room) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
    }

    @Override
    public void deleteData(String roomID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        Room room = em.find(Room.class, roomID);
        if (room != null) {
            room.setIsActivate(ObjectStatus.INACTIVE);
            em.merge(room);
        }
        em.getTransaction().commit();
    }

    @Override
    public void updateData(String oldRoomID, String oldCategory, Room newRoom) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        Room oldRoom = em.find(Room.class, oldRoomID);
        if (oldRoom != null && oldRoom.getRoomCategory().getRoomCategoryID().equals(oldCategory)) {
            em.remove(oldRoom);
            em.persist(newRoom);
        }
        em.getTransaction().commit();
    }

    @Override
    public List<Room> findDataByAnyContainsId(String input) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TypedQuery<Room> query = em.createQuery(
                "SELECT r FROM Room r WHERE LOWER(r.roomID) LIKE :input AND r.isActivate = :status", Room.class);
        query.setParameter("input", "%" + input.toLowerCase() + "%");
        query.setParameter("status", ObjectStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public String roomIDGenerate(int floorNumb, RoomCategory roomCategory) {
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

    @Override
    public void updateRoomStatus(String roomID, RoomStatus newStatus) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        em.getTransaction().begin();
        Room room = em.find(Room.class, roomID);
        if (room != null && room.getIsActivate() == ObjectStatus.ACTIVE) {
            room.setRoomStatus(newStatus);
            em.merge(room);
        }
        em.getTransaction().commit();
    }

    @Override
    public List<Room> getAvailableRoomsUntil(String roomID, String roomCategoryID, LocalDateTime desiredCheckOut) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
        SELECT r FROM Room r
        WHERE r.roomID != :rid
          AND r.roomCategory.roomCategoryID = :rcid
          AND r.isActivate = :status
          AND NOT EXISTS (
              SELECT 1 FROM ReservationForm rf
              WHERE rf.room.roomID = r.roomID
                AND rf.historyCheckIn IS NULL
                AND rf.approxcheckInDate < :desiredCheckOut
                AND rf.approxcheckOutTime > :now
          )
    """;

        TypedQuery<Room> query = em.createQuery(jpql, Room.class);
        query.setParameter("rid", roomID);
        query.setParameter("rcid", roomCategoryID);
        query.setParameter("status", ObjectStatus.ACTIVE);
        query.setParameter("desiredCheckOut", desiredCheckOut);
        query.setParameter("now", LocalDateTime.now());

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

    @Override
    public Map<RoomStatus, Long> getRoomStatusCount() {
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

    @Override
    public List<Room> searchRooms(
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

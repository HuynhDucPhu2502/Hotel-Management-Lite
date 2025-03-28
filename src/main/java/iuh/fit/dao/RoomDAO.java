package iuh.fit.dao;

import iuh.fit.models.Account;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class RoomDAO {

    public static void create(Room room) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.persist(room);
                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

    public static List<Room> findAll() {

        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            TypedQuery<Room> query = em.createQuery("select r from Room r", Room.class);
            return query.getResultList();

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return Collections.emptyList();

        }
    }

    public static Room findById(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            return em.find(Room.class, id);

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return null;

        }
    }

    public static void update(Room room) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();
                em.merge(room);
                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

    public static void delete(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            try{

                em.getTransaction().begin();

                em.createQuery("update Room r set r.isActivate = 'INACTIVE' where r.roomID = :id")
                        .setParameter("id", id)
                        .executeUpdate();

                em.getTransaction().commit();

            }catch(Exception transactionException) {

                transactionException.printStackTrace();
                em.getTransaction().rollback();

            }
        }  catch (Exception resourceException) {

            resourceException.printStackTrace();

        }
    }

    public static Room search(
            String roomID, String roomStatus,
            LocalDateTime lowerBoundDate, LocalDateTime upperBoundDate,
            String roomCategoryID
    ){
        try(EntityManager em = EntityManagerUtil.getEntityManager()){
            String newId = "%" + roomID + "%";
            Query query = em.createQuery(
                    """
            select r from Room r
            left join RoomCategory rc on r.roomCategory = rc
            where (r.roomID like :newId or :id is null)
            and (r.roomStatus = :roomStatus or :roomStatus is null)
            and (r.dateOfCreation >= :lowerBoundDate or :lowerBoundDate is null)
            and (r.dateOfCreation <= :upperBoundDate or :upperBoundDate is null)
            and ((:roomCategoryID = 'ALL') OR (rc.roomCategoryID = ? OR (? = 'NULL' AND rc.roomCategoryID IS NULL)))
            and r.isActivate = 'ACTIVE'
            """
            );
            query.setParameter("newId", newId);
            query.setParameter("id", roomID);
            query.setParameter("roomStatus", roomStatus);
            query.setParameter("lowerBoundDate", lowerBoundDate);
            query.setParameter("upperBoundDate", upperBoundDate);
            query.setParameter("roomCategoryID", roomCategoryID);


            return (Room) query.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String generateID(int floorNumb, RoomCategory roomCategory){
        String newRoomID = "";
        int[] maxIDNumb = {0};
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            Query query = em.createQuery("""
            select r.roomID from Room r where substring(r.roomID, 3, 1) = :floorNumb
            """);
            query.setParameter("floorNumb", floorNumb);
            List<String> roomIDs = query.getResultList();

            roomIDs.stream()
                    .map(roomID -> roomID.substring(roomID.length() - 2))
                    .forEach(roomNumb -> {
                        int numb = Integer.parseInt(roomNumb);
                        maxIDNumb[0] = Math.max(maxIDNumb[0], numb);
                    });
            
            if (maxIDNumb[0] >= 99) {
                throw new IllegalArgumentException("Số phòng trong 1 tầng đã đạt giới hạn 99 phòng. Không thể tạo thêm phòng mới.");
            }

            int nextIDNumb = maxIDNumb[0] + 1;
            String roomCategoryChar = roomCategory.getRoomCategoryName().contains("Phòng Thường") ? "T" : "V";
            String numbOfBedStr = String.valueOf(roomCategory.getNumberOfBed());
            String floorNumbStr = String.valueOf(floorNumb);

            newRoomID = String.format("%s%s%s%02d", roomCategoryChar, numbOfBedStr, floorNumbStr, nextIDNumb);
        }catch(Exception e){
            e.printStackTrace();
        }
        return newRoomID;
    }
}

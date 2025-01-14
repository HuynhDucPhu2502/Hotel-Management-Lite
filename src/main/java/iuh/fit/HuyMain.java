package iuh.fit;

import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.RoomStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Le Tran Gia Huy
 * @created 14/01/2025 - 10:51 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit
 */
public class HuyMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql");
        EntityManager em = emf.createEntityManager();

        // Faker để tạo dữ liệu giả
        Faker faker = new Faker();

        try{
            em.getTransaction().begin();

            Set<RoomCategory> roomCategories = new HashSet<>();
            for(int i = 0; i < 10; i++) {
                RoomCategory roomCategory = new RoomCategory();

                roomCategory.setRoomCategoryID("RC-" + String.format("%06d", (i + 1)));
                roomCategory.setRoomCategoryName(faker.team().name());
                roomCategory.setNumberOfBed(faker.number().numberBetween(1, 4));
                roomCategory.setHourlyPrice(faker.number().randomDouble(2, 100, 500));
                roomCategory.setDailyPrice(faker.number().randomDouble(2, 1000, 5000));
                roomCategory.setIsActivate(faker.bool().bool() ? ObjectStatus.ACTIVE : ObjectStatus.INACTIVE);

                em.persist(roomCategory);
                roomCategories.add(roomCategory);
            }

            for(int i = 0; i < 20; i++){
                Room room = new Room();

                room.setRoomID("R-" + String.format("%06d", (i + 1)));
                room.setRoomStatus(faker.bool().bool() ? RoomStatus.AVAILABLE : RoomStatus.UNAVAILABLE);
                room.setIsActivate(faker.bool().bool() ? ObjectStatus.ACTIVE : ObjectStatus.INACTIVE);
                room.setDateOfCreation(LocalDateTime.now());
                room.setRoomCategory(roomCategories.stream().skip(faker.number().numberBetween(0, 10)).findFirst().get());

                em.persist(room);
            }

            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            em.close();
            emf.close();
        }
    }
}

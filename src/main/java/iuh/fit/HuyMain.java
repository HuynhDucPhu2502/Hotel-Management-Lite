package iuh.fit;

import iuh.fit.models.RoomCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

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

            Set<RoomCategory> roomCategory = new HashSet<>();
            for(int i = 0; i < 10; i++) {
                System.out.println("Hello World!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

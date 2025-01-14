package iuh.fit;

import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.models.enums.ObjectStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

import java.util.List;
import java.util.Random;

/**
 * Admin 1/13/2025
 **/
public class Main {
    public static void main(String[] args) {
//        Application.main(args);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql");
        EntityManager em = emf.createEntityManager();

        EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("maria");
        EntityManager em2 = emf2.createEntityManager();

        Random random = new Random();

        Faker faker = new Faker();
        em.getTransaction().begin();
        em2.getTransaction().begin();
        for(String x : List.of("an uong", "choi", "gai")){
            ServiceCategory serviceCategory = new ServiceCategory();
            String id = faker.name().fullName();

            serviceCategory.setServiceCategoryID(id); // Gán id cho serviceCategoryID
            serviceCategory.setServiceCategoryName(x); // Gán name cho serviceCategoryName
            serviceCategory.setIsActivate(ObjectStatus.ACTIVE);

            em.persist(serviceCategory);
            em2.persist(serviceCategory);

            for (int y = 0; y < 10; y++) {
                HotelService hs = new HotelService();

                String hotelid = faker.name().fullName();
                String hotelsName = faker.name().fullName();
                String desc = faker.lorem().sentence();
                double price = random.nextDouble();

                hs.setServiceID(hotelid);
                hs.setServiceName(hotelsName);
                hs.setDescription(desc);
                hs.setServicePrice(price);
                hs.setIsActivate(ObjectStatus.ACTIVE);
                hs.setServiceCategory(serviceCategory);

                em.persist(hs);
                em2.persist(hs);
            }
        }

        em.getTransaction().commit();
        em2.getTransaction().commit();

    }
}

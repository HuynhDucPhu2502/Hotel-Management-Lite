package iuh.fit.dao;

import iuh.fit.models.RoomCategory;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;

/**
 * Admin 1/16/2025
 **/
public class RoomCategoryDAO {

    public static RoomCategory findById(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            return em.find(RoomCategory.class, id);

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return null;

        }
    }
}

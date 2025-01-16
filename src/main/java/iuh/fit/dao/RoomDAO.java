package iuh.fit.dao;

import iuh.fit.models.Room;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class RoomDAO {

    public static Room findById(String id) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            return em.find(Room.class, id);

        }  catch (Exception resourceException) {

            resourceException.printStackTrace();
            return null;

        }
    }


    public static List<Room> getData(EntityManager em){
        try{
            Query query = em.createQuery(
                    "select r from Room r"
            );
            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

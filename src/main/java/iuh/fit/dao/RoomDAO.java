package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class RoomDAO {
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

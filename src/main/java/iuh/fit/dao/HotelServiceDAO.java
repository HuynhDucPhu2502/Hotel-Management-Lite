package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class HotelServiceDAO {
    public static List<HotelService> getData(EntityManager em){
        try{
            Query query = em.createQuery(
                    "select hs from HotelService hs"
            );
            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

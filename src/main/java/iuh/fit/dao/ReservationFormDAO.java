package iuh.fit.dao;

import iuh.fit.models.ReservationForm;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class ReservationFormDAO {
    public static List<ReservationForm> getData(EntityManager em){
        try{
            Query query = em.createQuery(
                    "select rf from ReservationForm  rf"
            );
            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

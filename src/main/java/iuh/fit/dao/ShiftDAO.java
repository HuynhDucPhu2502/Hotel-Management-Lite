package iuh.fit.dao;

import iuh.fit.models.Shift;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 17/01/2025 - 9:36 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.dao
 */

public class ShiftDAO {

    public static List<Shift> getAll(){
        try(
                EntityManager em = EntityManagerUtil.getEntityManager()
                ){
            return (List<Shift>) em.createQuery(
                    "select s from Shift s"
            ).getResultList();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


}

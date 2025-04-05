package iuh.fit.dao;

import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Admin 4/5/2025
 **/
public class GlobalSequenceDAO {
    public static String getNextID(String tableName) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<String> query = em.createQuery(
                    "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = :tableName",
                    String.class
            );
            query.setParameter("tableName", tableName);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}

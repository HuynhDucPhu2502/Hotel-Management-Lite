package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.GlobalSequenceDAO;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Admin 4/5/2025
 **/
public class GlobalSequenceDAOImpl extends UnicastRemoteObject implements GlobalSequenceDAO {
    public GlobalSequenceDAOImpl() throws RemoteException {
    }

    public String getNextID(String tableName) {
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

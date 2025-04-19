package iuh.fit.dao.daointerface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 1:27 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface GlobalSequenceDAO extends Remote {
    String getNextID(String tableName)throws RemoteException;
}

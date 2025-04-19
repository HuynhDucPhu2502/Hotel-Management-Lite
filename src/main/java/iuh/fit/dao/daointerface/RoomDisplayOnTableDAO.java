package iuh.fit.dao.daointerface;

import iuh.fit.models.wrapper.RoomDisplayOnTable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 3:59 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface RoomDisplayOnTableDAO extends Remote {
    List<RoomDisplayOnTable> getAllData()throws RemoteException;

    List<RoomDisplayOnTable> getDataThreeYearsLatest()throws RemoteException;
}

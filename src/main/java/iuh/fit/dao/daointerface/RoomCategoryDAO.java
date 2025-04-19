package iuh.fit.dao.daointerface;

import iuh.fit.models.RoomCategory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 3:50 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface RoomCategoryDAO extends Remote {
    List<RoomCategory> getRoomCategory()throws RemoteException;

    RoomCategory getDataByID(String id)throws RemoteException;

    void createData(RoomCategory rc)throws RemoteException;

    void updateData(RoomCategory rc)throws RemoteException;

    void deleteData(String id)throws RemoteException;

    List<RoomCategory> findDataByContainsId(String input)throws RemoteException;

    boolean checkAllowUpdateOrDelete(String roomCategoryID)throws RemoteException;

    List<String> getTopThreeID()throws RemoteException;

    String getNextRoomCategoryID()throws RemoteException;
}

package iuh.fit.dao.daointerface;

import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.RoomStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 3:54 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface RoomDAO extends Remote {
    List<Room> getRoom()throws RemoteException;

    Room getDataByID(String roomID)throws RemoteException;

    void createData(Room room)throws RemoteException;

    void deleteData(String roomID)throws RemoteException;

    void updateData(String oldRoomID, String oldCategory, Room newRoom)throws RemoteException;

    List<Room> findDataByAnyContainsId(String input)throws RemoteException;

    String roomIDGenerate(int floorNumb, RoomCategory roomCategory)throws RemoteException;

    void updateRoomStatus(String roomID, RoomStatus newStatus)throws RemoteException;

    List<Room> getAvailableRoomsUntil(String roomID, String roomCategoryID, LocalDateTime checkout)throws RemoteException;

    Map<RoomStatus, Long> getRoomStatusCount()throws RemoteException;

    List<Room> searchRooms(
            String roomID, String roomStatus,
            LocalDateTime lowerBoundDate, LocalDateTime upperBoundDate,
            String roomCategoryID
    )throws RemoteException;
}

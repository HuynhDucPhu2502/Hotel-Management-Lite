package iuh.fit.dao.daointerface;

import iuh.fit.models.wrapper.RoomWithReservation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 4:05 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface RoomWithReservationDAO extends Remote {
    List<RoomWithReservation> getRoomWithReservation()throws RemoteException;

    RoomWithReservation getRoomWithReservationByRoomId(String roomId)throws RemoteException;

    RoomWithReservation getRoomWithReservationByID(String reservationFormID, String roomID)throws RemoteException;

    List<RoomWithReservation> getRoomOverDueWithLatestReservation()throws RemoteException;
}

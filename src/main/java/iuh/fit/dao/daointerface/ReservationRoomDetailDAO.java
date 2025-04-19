package iuh.fit.dao.daointerface;

import iuh.fit.models.ReservationRoomDetail;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 3:48 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface ReservationRoomDetailDAO extends Remote {
    List<ReservationRoomDetail> getAll()throws RemoteException;

    List<ReservationRoomDetail> getByReservationFormID(String reservationFormID)throws RemoteException;

    void createData(ReservationRoomDetail detail)throws RemoteException;

    void changingRoom(String currentRoomID, String newRoomID, String reservationFormID, String employeeID)throws RemoteException;

    String roomCheckingIn(String reservationFormID, String employeeID)throws RemoteException;

    String roomEarlyCheckingIn(String reservationFormID, String employeeID)throws RemoteException;
}

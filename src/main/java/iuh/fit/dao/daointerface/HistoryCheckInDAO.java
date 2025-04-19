package iuh.fit.dao.daointerface;

import iuh.fit.models.HistoryCheckIn;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 1:34 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface HistoryCheckInDAO extends Remote {
    List<HistoryCheckIn> getAll()throws RemoteException;

    HistoryCheckIn getByID(String historyCheckInID)throws RemoteException;

    String getNextID()throws RemoteException;

    LocalDateTime getActualCheckInDate(String reservationFormID)throws RemoteException;
}

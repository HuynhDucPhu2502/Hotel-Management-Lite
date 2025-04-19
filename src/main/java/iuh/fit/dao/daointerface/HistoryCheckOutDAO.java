package iuh.fit.dao.daointerface;

import iuh.fit.models.HistoryCheckOut;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 1:38 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface HistoryCheckOutDAO extends Remote {
    List<HistoryCheckOut> getHistoryCheckOut()throws RemoteException;
    HistoryCheckOut getDataByID(String historyCheckOutID)throws RemoteException;
    LocalDateTime getActualCheckOutDate(String reservationFormID)throws RemoteException;
    void incrementAndUpdateNextID()throws RemoteException;

}

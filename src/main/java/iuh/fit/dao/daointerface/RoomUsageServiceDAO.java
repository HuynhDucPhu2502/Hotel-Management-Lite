package iuh.fit.dao.daointerface;

import iuh.fit.models.RoomUsageService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 4:01 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface RoomUsageServiceDAO extends Remote {
    List<RoomUsageService> getByReservationFormID(String reservationFormID)throws RemoteException;

    String serviceOrdering(RoomUsageService roomUsageService)throws RemoteException;
}

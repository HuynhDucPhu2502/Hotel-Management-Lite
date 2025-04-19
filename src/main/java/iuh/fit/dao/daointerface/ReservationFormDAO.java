package iuh.fit.dao.daointerface;

import iuh.fit.models.ReservationForm;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 3:46 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface ReservationFormDAO extends Remote {
    String createReservationForm(ReservationForm form)throws RemoteException;

    List<ReservationForm> getUpcomingReservations(String roomID)throws RemoteException;

    List<ReservationForm> getReservationFormByCustomerID(String customerID)throws RemoteException;

    List<ReservationForm> getReservationsWithinLastMonth(String roomID)throws RemoteException;

    ReservationForm getDataByID(String id)throws RemoteException;

    void deleteData(String id)throws RemoteException;

    void updateData(ReservationForm form)throws RemoteException;
}

package iuh.fit.dao.daointerface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 6:35 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daointerface
 */
public interface AllDAO extends Remote {
    AccountDAO getAccountDAO() throws RemoteException;
    CustomerDAO getCustomerDAO() throws RemoteException;
    EmployeeDAO getEmployeeDAO() throws RemoteException;
    GlobalSequenceDAO getGlobalSequenceDAO() throws RemoteException;
    HistoryCheckInDAO getHistoryCheckInDAO() throws RemoteException;
    HistoryCheckOutDAO getHistoryCheckOutDAO() throws RemoteException;
    HotelServiceDAO getHotelServiceDAO() throws RemoteException;
    InvoiceDAO getInvoiceDAO() throws RemoteException;
    InvoiceDisplayOnTableDAO getInvoiceDisplayOnTableDAO() throws RemoteException;
    ReservationFormDAO getReservationFormDAO() throws RemoteException;
    ReservationRoomDetailDAO getReservationRoomDetailDAO() throws RemoteException;
    RoomCategoryDAO getRoomCategoryDAO() throws RemoteException;
    RoomDAO getRoomDAO() throws RemoteException;
    RoomDisplayOnTableDAO getRoomDisplayOnTableDAO() throws RemoteException;
    RoomUsageServiceDAO getRoomUsageServiceDAO() throws RemoteException;
    RoomWithReservationDAO getRoomWithReservationDAO() throws RemoteException;
    ServiceCategoryDAO getServiceCategoryDAO() throws RemoteException;
    ServiceDisplayOnTableDAO getServiceDisplayOnTableDAO() throws RemoteException;
}

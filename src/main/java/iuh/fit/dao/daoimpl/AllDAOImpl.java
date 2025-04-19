package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.*;
import lombok.Getter;
import lombok.Setter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 6:44 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
@Getter
public class AllDAOImpl extends UnicastRemoteObject implements AllDAO {
    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    private final GlobalSequenceDAO globalSequenceDAO = new GlobalSequenceDAOImpl();
    private final HistoryCheckInDAO historyCheckInDAO = new HistoryCheckInDAOImpl();
    private final HistoryCheckOutDAO historyCheckOutDAO = new HistoryCheckOutDAOImpl();
    private final HotelServiceDAO hotelServiceDAO = new HotelServiceDAOImpl();
    private final InvoiceDAO invoiceDAO = new InvoiceDAOImpl();
    private final InvoiceDisplayOnTableDAO invoiceDisplayOnTableDAO = new InvoiceDisplayOnTableDAOImpl();
    private final ReservationFormDAO reservationFormDAO = new ReservationFormDAOImpl();
    private final ReservationRoomDetailDAO reservationRoomDetailDAO = new ReservationRoomDetailDAOImpl();
    private final RoomCategoryDAO roomCategoryDAO = new RoomCategoryDAOImpl();
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final RoomDisplayOnTableDAO roomDisplayOnTableDAO = new RoomDisplayOnTableDAOImpl();
    private final RoomUsageServiceDAO roomUsageServiceDAO = new RoomUsageServiceDAOImpl();
    private final RoomWithReservationDAO roomWithReservationDAO = new RoomWithReservationDAOImpl();
    private final ServiceCategoryDAO serviceCategoryDAO = new ServiceCategoryDAOImpl();
    private final ServiceDisplayOnTableDAO serviceDisplayOnTableDAO = new ServiceDisplayOnTableDAOImpl();

    public AllDAOImpl() throws RemoteException {}
}

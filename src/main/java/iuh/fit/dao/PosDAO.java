package iuh.fit.dao;

import iuh.fit.dto.InvoiceInfoDTO;
import iuh.fit.models.Customer;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.Gender;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 17/04/2025 - 2:42 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao
 */
public interface PosDAO extends Remote {

    List<Customer> searchCustomer (
            String info
    ) throws RemoteException;

    List<InvoiceInfoDTO> searchCustomerInvoice (
            String info
    ) throws RemoteException;

    List<RoomCategory> getCategoryForPOS () throws RemoteException;

    List<Room> getAvailableRoomByRoomCategoryForPOS (RoomCategory category) throws RemoteException;

}

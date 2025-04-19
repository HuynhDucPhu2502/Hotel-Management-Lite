package iuh.fit.dao.daointerface;

import iuh.fit.models.Invoice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 1:44 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface InvoiceDAO extends Remote {
    List<Invoice> getAllInvoices()throws RemoteException;

    Invoice getInvoiceByInvoiceID(String invoiceID)throws RemoteException;

    Invoice getInvoiceByReservationFormID(String reservationFormID)throws RemoteException;

    String roomCheckingOut(String reservationFormID, double roomCharge, double serviceCharge)throws RemoteException;

    String roomCheckingOutEarly(String reservationFormID, double roomCharge, double serviceCharge)throws RemoteException;
}

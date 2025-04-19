package iuh.fit.dao.daointerface;

import iuh.fit.models.wrapper.InvoiceDisplayOnTable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 3:44 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface InvoiceDisplayOnTableDAO extends Remote {
    List<InvoiceDisplayOnTable> getAllData()throws RemoteException;

    List<InvoiceDisplayOnTable> getDataThreeYearsLatest()throws RemoteException;
}

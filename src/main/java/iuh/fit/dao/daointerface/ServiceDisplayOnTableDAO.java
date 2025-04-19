package iuh.fit.dao.daointerface;

import iuh.fit.models.wrapper.ServiceDisplayOnTable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 4:11 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface ServiceDisplayOnTableDAO extends Remote {
    List<ServiceDisplayOnTable> getAllData()throws RemoteException;

    List<ServiceDisplayOnTable> getDataThreeYearsLatest()throws RemoteException;
}

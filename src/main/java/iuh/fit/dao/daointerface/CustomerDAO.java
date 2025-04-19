package iuh.fit.dao.daointerface;

import iuh.fit.models.Customer;
import iuh.fit.models.enums.Gender;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface CustomerDAO extends Remote {
    List<Customer> findAll()throws RemoteException;

    Customer findById(String customerID)throws RemoteException;

    String getNextCustomerID()throws RemoteException;

    void updateNextCustomerID(String currentNextID)throws RemoteException;

    void create(Customer customer)throws RemoteException;

    void update(Customer customer)throws RemoteException;

    void delete(String customerID)throws RemoteException;

    List<String> getTopThreeID()throws RemoteException;

    List<Customer> findDataByContainsId(String input)throws RemoteException;

    Customer getDataByIDCardNumber(String input)throws RemoteException;

    List<Customer> searchCustomer(
            String customerID, String fullName, String phoneNumber,
            String address, Gender gender, String idCardNumber, LocalDate dob
    )throws RemoteException;

}

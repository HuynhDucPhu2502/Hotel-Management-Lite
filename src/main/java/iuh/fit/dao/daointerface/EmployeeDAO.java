package iuh.fit.dao.daointerface;

import iuh.fit.models.Employee;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
*@created 19/04/2025 - 1:14 PM
*@project Hotel-Management-Lite
*@package iuh.fit.dao.daoimpl
*@author Le Tran Gia Huy
*/
public interface EmployeeDAO extends Remote {
    List<Employee> getEmployees()throws RemoteException;
    Employee getEmployeeByEmployeeCode(String employeeCode)throws RemoteException;
    String getNextEmployeeID()throws RemoteException;
    void updateNextEmployeeID(String currentNextID)throws RemoteException;
    void createData(Employee employee)throws RemoteException;
    void updateData(Employee employee)throws RemoteException;
    void deleteData(String employeeID)throws RemoteException;
    List<String> getTopThreeID()throws RemoteException;
    List<Employee> findDataByContainsId(String input)throws RemoteException;
    List<Employee> searchEmployee(
            String employeeID, String fullName, String phoneNumber,
            String address, Gender gender, String idCardNumber,
            LocalDate dob, Position position
    )throws RemoteException;
}

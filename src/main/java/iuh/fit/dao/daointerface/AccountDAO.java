package iuh.fit.dao.daointerface;

import iuh.fit.models.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AccountDAO  extends Remote {

    // Lấy danh sách tất cả tài khoản
    List<Account> getAccount()throws RemoteException;

    // Lấy tài khoản theo ID
    Account getDataByID(String accountID)throws RemoteException;

    // Lấy tài khoản khi đăng nhập
    Account getLogin(String username, String password)throws RemoteException;

    // Tạo mới tài khoản
    void createData(Account account)throws RemoteException;

    // Xóa tài khoản
    void deleteData(String accountID)throws RemoteException;

    // Cập nhật tài khoản
    void updateData(Account account)throws RemoteException;

    // Lấy ID tiếp theo từ GlobalSequence
    String getNextAccountID()throws RemoteException;

    // Cập nhật ID tiếp theo
    void updateNextAccountID(String currentNextID)throws RemoteException;

    // Lấy tài khoản theo mã nhân viên
    Account getAccountByEmployeeID(String employeeID)throws RemoteException;

    // Tìm tài khoản theo ID chứa từ khóa
    List<Account> findDataByContainsEmployeeCode(String input)throws RemoteException;

}

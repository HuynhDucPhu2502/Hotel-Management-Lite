package iuh.fit.dao.daointerface;

import iuh.fit.models.ServiceCategory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 4:08 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface ServiceCategoryDAO extends Remote {
    // Tạo mới ServiceCategory với ID từ GlobalSequence
    void createData(ServiceCategory serviceCategory)throws RemoteException;

    // Cập nhật ServiceCategory
    void updateData(ServiceCategory serviceCategory)throws RemoteException;

    // Xóa ServiceCategory (chuyển trạng thái INACTIVE)
    boolean deleteData(String serviceCategoryID)throws RemoteException;

    // Lấy tất cả các ServiceCategory đang hoạt động
    List<ServiceCategory> findAll()throws RemoteException;

    // Tìm ServiceCategory theo ID
    ServiceCategory findById(String serviceCategoryID)throws RemoteException;

    // Kiểm tra ServiceCategory đang được sử dụng
    boolean isServiceCategoryInUse(String serviceCategoryID)throws RemoteException;

    // Tìm kiếm ServiceCategory theo ID chứa từ khóa
    List<ServiceCategory> findDataByContainsId(String input)throws RemoteException;

    // Lấy top 3 ServiceCategory ID
    List<String> getTopThreeID()throws RemoteException;

    // Lấy ID tiếp theo từ GlobalSequence
    String getNextServiceCategoryID()throws RemoteException;

    // Lấy danh sách tên ServiceCategory
    List<String> getServiceCategoryNames()throws RemoteException;
}

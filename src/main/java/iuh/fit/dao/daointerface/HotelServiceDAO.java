package iuh.fit.dao.daointerface;

import iuh.fit.models.HotelService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 1:40 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao.daoimpl
 */
public interface HotelServiceDAO extends Remote {
    List<HotelService> getHotelService()throws RemoteException;

    HotelService getDataByID(String hotelServiceId)throws RemoteException;

    boolean createData(HotelService hotelService)throws RemoteException;

    // Xóa HotelService (chuyển trạng thái INACTIVE)
    boolean deleteData(String hotelServiceId)throws RemoteException;

    // Cập nhật HotelService
    boolean updateData(HotelService hotelService)throws RemoteException;

    // Tìm kiếm HotelService theo ID chứa từ khóa
    List<HotelService> findDataByContainsId(String input)throws RemoteException;

    // Lấy top 3 HotelService ID
    List<String> getTopThreeID()throws RemoteException;

    // Lấy ID tiếp theo từ GlobalSequence
    String getNextHotelServiceID()throws RemoteException;

    List<HotelService> searchHotelServices(
            String hotelServiceID, String serviceName,
            Double minPrice, Double maxPrice, String serviceCategory)throws RemoteException;
}

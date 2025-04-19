package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.ServiceDisplayOnTableDAO;
import iuh.fit.models.wrapper.ServiceDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceDisplayOnTableDAOImpl extends UnicastRemoteObject implements ServiceDisplayOnTableDAO {
    public ServiceDisplayOnTableDAOImpl() throws RemoteException {
    }

    @Override
    public List<ServiceDisplayOnTable> getAllData(){
        List<ServiceDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT " +
                "rus.room_usage_service_id, " +
                "hs.service_name, " +
                "sc.service_category_name, " +
                "p.full_name, " +
                "rus.day_added, " +
                "rus.quantity, " +
                "rus.unit_price " +
                "FROM reservation_forms rf " +
                "JOIN room_usage_services rus ON rf.reservation_id = rus.reservation_id " +
                "JOIN hotel_services hs ON rus.service_id = hs.service_id " +
                "JOIN service_categories sc ON hs.service_category_id = sc.service_category_id " +
                "JOIN employees e ON rf.employee_code = e.employee_code " +
                "JOIN persons p ON e.person_id = p.person_id";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                ServiceDisplayOnTable serviceDisplayOnTable = new ServiceDisplayOnTable();

                serviceDisplayOnTable.setServiceId(rs.getString(1));
                serviceDisplayOnTable.setServiceName(rs.getString(2));
                serviceDisplayOnTable.setServiceCategory(rs.getString(3));
                serviceDisplayOnTable.setEmployeeName(rs.getString(4));
                serviceDisplayOnTable.setDateAdded(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                serviceDisplayOnTable.setQuantity(rs.getInt(6));
                serviceDisplayOnTable.setUnitPrice(rs.getDouble(7));
                double totalMoney = rs.getInt(6) * rs.getDouble(7);
                serviceDisplayOnTable.setTotalMoney(totalMoney);
                data.add(serviceDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    @Override
    public List<ServiceDisplayOnTable> getDataThreeYearsLatest(){
        List<ServiceDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT " +
                "rus.room_usage_service_id, " +
                "hs.service_name, " +
                "sc.service_category_name, " +
                "p.full_name, " +
                "rus.day_added, " +
                "rus.quantity, " +
                "rus.unit_price " +
                "FROM reservation_forms rf " +
                "JOIN room_usage_services rus ON rf.reservation_id = rus.reservation_id " +
                "JOIN hotel_services hs ON rus.service_id = hs.service_id " +
                "JOIN service_categories sc ON hs.service_category_id = sc.service_category_id " +
                "JOIN employees e ON rf.employee_code = e.employee_code " +
                "JOIN persons p ON e.person_id = p.person_id\n" +
                "WHERE DATEPART(YEAR, rus.day_added) >= DATEPART(YEAR, GETDATE()) - 2 ";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                ServiceDisplayOnTable serviceDisplayOnTable = new ServiceDisplayOnTable();

                serviceDisplayOnTable.setServiceId(rs.getString(1));
                serviceDisplayOnTable.setServiceName(rs.getString(2));
                serviceDisplayOnTable.setServiceCategory(rs.getString(3));
                serviceDisplayOnTable.setEmployeeName(rs.getString(4));
                serviceDisplayOnTable.setDateAdded(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                serviceDisplayOnTable.setQuantity(rs.getInt(6));
                serviceDisplayOnTable.setUnitPrice(rs.getDouble(7));
                double totalMoney = rs.getInt(6) * rs.getDouble(7);
                serviceDisplayOnTable.setTotalMoney(totalMoney);
                data.add(serviceDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}

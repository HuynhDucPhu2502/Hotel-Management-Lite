package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.InvoiceDisplayOnTableDAO;
import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDisplayOnTableDAOImpl extends UnicastRemoteObject implements InvoiceDisplayOnTableDAO {
    public InvoiceDisplayOnTableDAOImpl() throws RemoteException {
    }

    @Override
    public List<InvoiceDisplayOnTable> getAllData(){
        List<InvoiceDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT i.invoice_id, cp.full_name AS customer_name, r.room_id, ep.full_name AS employee_name, i.invoice_date, rs.booking_deposit, i.service_charges, i.room_charges, i.total_due\n" +
                "FROM invoices i\n" +
                "JOIN reservation_forms rs ON i.reservation_id = rs.reservation_id\n" +
                "JOIN customers c ON c.customer_code = rs.customer_code\n" +
                "JOIN persons cp ON c.person_id = cp.person_id\n" +  // Join để lấy full_name của khách hàng
                "JOIN employees e ON e.employee_code = rs.employee_code\n" +
                "JOIN persons ep ON e.person_id = ep.person_id\n" +  // Join để lấy full_name của nhân viên
                "JOIN rooms r ON r.room_id = rs.room_id";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                InvoiceDisplayOnTable invoiceDisplayOnTable = new InvoiceDisplayOnTable();

                invoiceDisplayOnTable.setInvoiceID(rs.getString(1));
                invoiceDisplayOnTable.setCusName(rs.getString(2));
                invoiceDisplayOnTable.setRoomID(rs.getString(3));
                invoiceDisplayOnTable.setEmpName(rs.getString(4));
                invoiceDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                invoiceDisplayOnTable.setDeposit(rs.getDouble(6));
                invoiceDisplayOnTable.setServiceCharge(rs.getDouble(7));
                invoiceDisplayOnTable.setRoomCharge(rs.getDouble(8));
                invoiceDisplayOnTable.setNetDue(rs.getDouble(9));

                data.add(invoiceDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    @Override
    public List<InvoiceDisplayOnTable> getDataThreeYearsLatest(){
        List<InvoiceDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT i.invoice_id, cp.full_name AS customer_name, r.room_id, ep.full_name AS employee_name, i.invoice_date, rs.booking_deposit, i.service_charges, i.room_charges, i.total_due\n" +
                "FROM invoices i\n" +
                "JOIN reservation_forms rs ON i.reservation_id = rs.reservation_id\n" +
                "JOIN customers c ON c.customer_code = rs.customer_code\n" +
                "JOIN persons cp ON c.person_id = cp.person_id\n" +  // Join để lấy full_name của khách hàng
                "JOIN employees e ON e.employee_code = rs.employee_code\n" +
                "JOIN persons ep ON e.person_id = ep.person_id\n" +  // Join để lấy full_name của nhân viên
                "JOIN rooms r ON r.room_id = rs.room_id\n" +
                "WHERE DATEPART(YEAR, i.invoice_date) >= DATEPART(YEAR, GETDATE()) - 2 ";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                InvoiceDisplayOnTable invoiceDisplayOnTable = new InvoiceDisplayOnTable();

                invoiceDisplayOnTable.setInvoiceID(rs.getString(1));
                invoiceDisplayOnTable.setCusName(rs.getString(2));
                invoiceDisplayOnTable.setRoomID(rs.getString(3));
                invoiceDisplayOnTable.setEmpName(rs.getString(4));
                invoiceDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                invoiceDisplayOnTable.setDeposit(rs.getDouble(6));
                invoiceDisplayOnTable.setServiceCharge(rs.getDouble(7));
                invoiceDisplayOnTable.setRoomCharge(rs.getDouble(8));
                invoiceDisplayOnTable.setNetDue(rs.getDouble(9));

                data.add(invoiceDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}

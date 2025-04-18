package iuh.fit.dao;

import iuh.fit.models.wrapper.RoomDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDisplayOnTableDAO {

    public static List<RoomDisplayOnTable> getAllData(){
        List<RoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT " +
                "r.room_id, " +
                "rc.room_category_name, " +
                "rc.number_of_bed, " +
                "rf.reservation_date, " +
                "rf.approx_check_in_date, " +
                "rf.approx_check_out_date, " +
                "i.room_charges " +
                "FROM reservation_forms rf " +
                "JOIN invoices i ON rf.reservation_id = i.reservation_id " +
                "JOIN rooms r ON rf.room_id = r.room_id " +
                "JOIN room_categories rc ON r.room_category_id = rc.room_category_id " +
                "WHERE r.is_activate = 'ACTIVE' " +
                "AND rc.is_activate = 'ACTIVE' ";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                RoomDisplayOnTable roomDisplayOnTable = new RoomDisplayOnTable();

                roomDisplayOnTable.setRoomID(rs.getString(1));
                roomDisplayOnTable.setRoomCategory(rs.getString(2));
                roomDisplayOnTable.setNumOfPeople(rs.getInt(3));
                roomDisplayOnTable.setBookingDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                roomDisplayOnTable.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                roomDisplayOnTable.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(6)));
                roomDisplayOnTable.setTotalMoney(rs.getDouble(7));
                data.add(roomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<RoomDisplayOnTable> getDataThreeYearsLatest(){
        List<RoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT " +
                "r.room_id, " +
                "rc.room_category_name, " +
                "rc.number_of_bed, " +
                "rf.reservation_date, " +
                "rf.approx_check_in_date, " +
                "rf.approx_check_out_date, " +
                "i.room_charges " +
                "FROM reservation_forms rf " +
                "JOIN invoices i ON rf.reservation_id = i.reservation_id " +
                "JOIN rooms r ON rf.room_id = r.room_id " +
                "JOIN room_categories rc ON r.room_category_id = rc.room_category_id " +
                "WHERE r.is_activate = 'ACTIVE' " +
                "AND rc.is_activate = 'ACTIVE' " +
                "AND DATEPART(YEAR, i.invoice_date) >= DATEPART(YEAR, GETDATE()) - 2;";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                RoomDisplayOnTable roomDisplayOnTable = new RoomDisplayOnTable();

                roomDisplayOnTable.setRoomID(rs.getString(1));
                roomDisplayOnTable.setRoomCategory(rs.getString(2));
                roomDisplayOnTable.setNumOfPeople(rs.getInt(3));
                roomDisplayOnTable.setBookingDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                roomDisplayOnTable.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                roomDisplayOnTable.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(6)));
                roomDisplayOnTable.setTotalMoney(rs.getDouble(7));
                data.add(roomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}

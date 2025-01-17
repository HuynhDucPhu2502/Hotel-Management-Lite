package iuh.fit.utils;

import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Admin 1/16/2025
 **/
public class RoomChargesCalculate {

    public static double calculateRoomCharges(
            LocalDateTime checkInTime,
            LocalDateTime checkOutTime,
            Room room
    )  {
        RoomCategory roomCategory = RoomCategoryDAO.getById(room.getRoomCategory().getRoomCategoryID());

        double hourlyPrice = roomCategory.getHourlyPrice();
        double dailyPrice = roomCategory.getDailyPrice();

        Map<String, Double> info = calculateStayLengthToDouble(checkInTime, checkOutTime);

        String type = info.get("type").toString();
        double value = info.get("value");

        if (type.equals("0.0")) {
            return value * hourlyPrice;
        } else {
            return value * dailyPrice;
        }
    }

    public static String calculateStayLengthToString(
            LocalDateTime checkInTime,
            LocalDateTime checkOutTime
    ) {
        Map<String, Double> info = calculateStayLengthToDouble(checkInTime, checkOutTime);

        String type = info.get("type").toString();
        double value = info.get("value");

        if (type.equals("0.0")) {
            return value + " giờ";
        } else {
            return value + " ngày";
        }
    }

    public static Map<String, Double> calculateStayLengthToDouble(
            LocalDateTime checkInTime,
            LocalDateTime checkOutTime
    ) {
        Map<String, Double> res = new HashMap<>();

        double hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        double days = hours / 24.0;

        if (hours < 12) {
            res.put("type", 0.0);
            res.put("value", hours);
        } else {
            res.put("type", 1.0);
            res.put("value", Math.ceil(days * 2) / 2.0);
        }

        return res;
    }
}

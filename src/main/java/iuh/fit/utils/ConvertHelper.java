package iuh.fit.utils;

import iuh.fit.models.enums.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConvertHelper {
    // ==================================================================================================================
    // Database to Application Converter
    // ==================================================================================================================

    public static LocalDateTime localDateTimeConverter(Timestamp input) {
        return input.toLocalDateTime();
    }

}

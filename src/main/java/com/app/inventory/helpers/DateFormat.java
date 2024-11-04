package com.app.inventory.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateFormat {

    public static String ByPattern(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        if (!"now".equals(pattern)) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
        return localDateTime.format(formatter);
    }

    public static Date StrToDate(String dateInString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            // e.printStackTrace();
        }
        return date;
    }

}

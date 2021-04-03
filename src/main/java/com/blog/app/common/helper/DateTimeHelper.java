package com.blog.app.common.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {

    private static final String[] dateFormats = {
            "dd-MM-yyyy",
            "dd/MM/yyyy",
            "dd-MMM-yyyy",
            "MM/dd/yyyy",
            "yyyy-MM-dd",
            "ddMMyyyy"
    };

    public static boolean isDateFormatMatch(String dateString) {
        return dateString.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})");
    }

    public static Date convertDateStringToDate(String dateStr) {
        Date date = null;
        if (!dateStr.isEmpty()) {
            for (String dFormat : dateFormats) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(dFormat);
                    date = sdf.parse(dateStr);
                    break;
                } catch (ParseException e) {
                    //e.printStackTrace();
                }
            }
        }
        return date;
    }
}

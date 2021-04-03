package com.blog.app.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String EMPTY_STRING = "";
    public static String NEW_LINE = "\n";

    private static final String DB_NULL_STRING = "null";

    public static boolean isNullDB(String value) {
        return value.equals(DB_NULL_STRING) ? true : false;
    }

    public static String returnDBValue(String value) {
        return value != null ? (value.equals(DB_NULL_STRING) ? EMPTY_STRING : value) : EMPTY_STRING;
    }

    public static boolean isEmpty(Object value) {
        return value == null;
    }
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
    public static boolean isEmpty(Long value) {
        return value == null || value.equals(new Long(0) == 0);
    }
    public static boolean isEmpty(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) == 0;
    }


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static String convertDateToString(Date today) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(today);
        return strDate;
    }

    public static boolean isNull(Integer value) {
        return value == null || value.intValue() ==0;
    }

    public static final boolean isNull(Double value) {
        return value == null || value.intValue() ==0;
    }

    public static final Timestamp getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    //ORACLE NVL FUNCTION
    public static String NVL(String value, String replaceValue) { return value != null ? (!value.equalsIgnoreCase("") ? value : replaceValue) : replaceValue; }
    public static Integer NVL(Integer value, Integer replaceValue) { return value != null ? value : replaceValue; }
    public static Boolean NVL(Boolean value, Boolean replaceValue) { return value != null ? value : replaceValue; }
    //public static Double NVL(Double value, Double replaceValue) { return value != null ? value : replaceValue; }
    //public static Float NVL(Float value, Float replaceValue) { return value != null ? value : replaceValue; }
    public static Long NVL(Long value, Long replaceValue) { return value != null ? value : replaceValue; }
    public static BigDecimal NVL(BigDecimal value, BigDecimal replaceValue) { return value != null ? value : replaceValue; }

    public static MathContext MATH_CONTEXT_4() { return new MathContext(4, RoundingMode.HALF_UP); }

    public static String toString(Integer value) { return value != null ? (Utils.EMPTY_STRING) + value : Utils.EMPTY_STRING; }
}

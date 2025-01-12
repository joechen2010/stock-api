package com.stock.api.util;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class ApiUtils {

    public static final DateFormat PLUS_DATE_FORMATOR = new SimpleDateFormat("yyyy/MM/dd");
    public static String getCurrentPlusDay() {
        return PLUS_DATE_FORMATOR.format(new Date());
    }


    public static String getCurrentPlusDay(int days) {
        long result = addDays(System.currentTimeMillis(), days);
        return PLUS_DATE_FORMATOR.format(result);
    }

    public static long addDays(long source, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        calendar.add(5, days);
        return calendar.getTimeInMillis();
    }

    public static double round(double source) {
        return round(source, 2);
    }

    public static double round(double source, int count) {
        BigDecimal bigDecimal = new BigDecimal(source);
        return bigDecimal.setScale(count, RoundingMode.HALF_UP).doubleValue();
    }

    public static LocalDateTime getFutureDate(int months) {
        return LocalDateTime.now().plusMonths(months);
    }

}

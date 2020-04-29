package utils;

import exception.LogicException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String yyyy = "yyyy";
    public static final String yyyy_MM = "yyyy-MM";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HHmmss = "yyyy-MM-dd HH:mm:ss";

    public DateUtils() {
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            Date result = parseDate(dateStr, new String[]{pattern});
            return result;
        } catch (ParseException e) {
            throw new LogicException(-255, "时间[" + dateStr + "]格式[" + pattern + "]错误");
        }
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String format_yMdHMS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String format_yyyyMMdd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String format_yyyyMM(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }

    public static String format_yyyy(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    public static int calcDays(Date start, Date end) {
        int days = (int)((end.getTime() - start.getTime()) / 86400000L);
        return days;
    }

    public static int calcYear(Date start, Date end) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(start);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(end);
        return c2.get(1) - c1.get(1);
    }

    public static boolean isSameYear(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return c1.get(1) == c2.get(1);
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        if (c1.get(1) != c2.get(1)) {
            return false;
        } else {
            return c1.get(2) == c2.get(2);
        }
    }

    public static int calcMonths(Date start, Date end) {
        System.err.println("---" + format_yyyyMMdd(start));
        System.err.println("---" + format_yyyyMMdd(end));
        int ratio = 1;
        if (end.before(start)) {
            Date tmp = end;
            end = start;
            start = tmp;
            ratio = -1;
        }

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        int startYear = startCal.get(Calendar.YEAR);
        int endYear = endCal.get(Calendar.YEAR);
        int startMonth = startCal.get(Calendar.MONTH);
        int endMonth = endCal.get(Calendar.MONTH);
        int startDay = startCal.get(Calendar.DATE);
        int endDay = startCal.get(Calendar.DATE);
        int offsetYear = endYear - startYear;
        int offsetMonth;
        if (startMonth <= endMonth && (startMonth != endMonth || startDay >= endDay)) {
            offsetMonth = endMonth - startMonth;
        } else {
            --offsetYear;
            offsetMonth = 12 - startMonth + endMonth;
        }

        return ratio * (offsetYear * 12 + offsetMonth);
    }

    public static Date getFirstTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        Date date = new Date(1532275199000L);
        System.out.println(format(date, yyyy_MM_dd_HHmmss));
    }
}

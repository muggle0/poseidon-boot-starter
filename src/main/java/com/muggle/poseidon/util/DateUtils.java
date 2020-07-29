package com.muggle.poseidon.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/7/29
 **/
public class DateUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN1 = "yyyy_MM_dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_PATTERN = "HH:mm:ss";

    public DateUtils() {
    }

    public static String format(long millis, String pattern) {
        return format(new Date(millis), pattern);
    }

    public static long getDateMinus(String startDate, String endDate, String pattern) {
        SimpleDateFormat myFormatter = new SimpleDateFormat(pattern);

        try {
            Date date = myFormatter.parse(startDate);
            Date mydate = myFormatter.parse(endDate);
            long day = (mydate.getTime() - date.getTime()) / 60000L;
            return day;
        } catch (ParseException var8) {
            var8.printStackTrace();
            return 0L;
        }
    }

    public static String addDay(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(5, n);
            return sdf.format(cd.getTime());
        } catch (Exception var4) {
            return null;
        }
    }

    public static int getDaysByYearMonth(String dateString) {
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(5, 7));
        Calendar a = Calendar.getInstance();
        a.set(1, year);
        a.set(2, month - 1);
        a.set(5, 1);
        a.roll(5, -1);
        int maxDate = a.get(5);
        return maxDate;
    }

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        int lastDay = cal.getActualMaximum(5);
        cal.set(5, lastDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    public static Date ConverToDate(String strDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return df.parse(strDate);
        } catch (ParseException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static List<Date> findDates(String startTime, String endTime) {
        Date startDate = ConverToDate(startTime);
        Date endDate = ConverToDate(endTime);
        List<Date> lDate = new ArrayList();
        lDate.add(startDate);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);

        while (endDate.after(calBegin.getTime())) {
            calBegin.add(5, 1);
            lDate.add(calBegin.getTime());
        }

        return lDate;
    }

    public static int getWeek(Date date) {
        int[] weeks = new int[]{0, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(7) - 1;
        if (week_index < 0) {
            week_index = 0;
        }

        return weeks[week_index];
    }

    public static String getTimeDifference(String startTime, String endTime) {
        SimpleDateFormat dfs = new SimpleDateFormat("HH:mm:ss");
        long between = 0L;

        try {
            Date begin = dfs.parse(startTime);
            Date end = dfs.parse(endTime);
            between = end.getTime() - begin.getTime();
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        long day = between / 86400000L;
        long hour = between / 3600000L - day * 24L;
        long min = between / 60000L - day * 24L * 60L - hour * 60L;
        long s = between / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
        String date = day + "天" + hour + "小时" + min + "分" + s + "秒";
        if (day == 0L && hour != 0L) {
            date = hour + "小时" + min + "分" + s + "秒";
        } else if (day == 0L && hour == 0L && min != 0L) {
            date = min + "分" + s + "秒";
        } else if (day == 0L && hour == 0L && min == 0L) {
            date = s + "秒";
        }

        return date;
    }

    public static int getNextDaySec() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(6, 1);
        cal.set(11, 9);
        cal.set(12, 0);
        cal.set(13, 0);
        Long tm = cal.getTimeInMillis() - date.getTime();
        return (int) (tm / 1000L);
    }

    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(7) - 1;
        if (week_index < 0) {
            week_index = 0;
        }

        return weekOfDays[week_index];
    }

    public static String format(Date date, String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String formatDate(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String formatTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    public static String formatDateTime(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatCurrent(String pattern) {
        return format(new Date(), pattern);
    }

    public static String formatCurrentDate() {
        return format(new Date(), "yyyy-MM-dd");
    }

    public static String formatCurrentTime() {
        return format(new Date(), "HH:mm:ss");
    }

    public static String formatCurrentDateTime() {
        return format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static Date getTheDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static int compareDate(Date start, Date end) {
        if (start == null && end == null) {
            return 0;
        } else if (end == null) {
            return 1;
        } else {
            if (start == null) {
                start = new Date();
            }

            start = getTheDate(start);
            end = getTheDate(end);
            return start.compareTo(end);
        }
    }

    public static Date stringToDate(String dateString, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        try {
            return formatter.parse(dateString);
        } catch (ParseException var4) {
            throw new RuntimeException(var4);
        }
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("日期对象不允许为null!");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    public static Date getDateAfterNatureDays(Date date, int days, int hours) {
        if (date == null) {
            date = new Date();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, days);
        c.add(10, hours);
        return c.getTime();
    }

    public static Date getDateAfterWorkDays(Date date, int days, int hours) {
        if (date == null) {
            date = new Date();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, days);
        c.add(10, hours);
        return c.getTime();
    }

    public static String dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        String res = String.valueOf(ts);
        return res;
    }

    public static String stampToDate(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {
        String s = stampToDate("1502964348398");
        System.out.println(s);
    }
}



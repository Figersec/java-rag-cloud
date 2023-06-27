package com.kailin.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangqianjin
 */
@Slf4j
public class DateUtils {

    /**
     * 计算两个日期间隔天数
     *
     * @param start 开始时间
     * @param end 结束时间
     */
    public static Integer getDayInterval(Date start, Date end) {
        if (start != null && end != null) {
            final long nd = 1000 * 24 * 60 * 60L;
            Date startDay = new Date(start.getTime() - start.getTime() % nd);
            Date endDay = new Date(end.getTime() - end.getTime() % nd);
            return (int) ((endDay.getTime() - startDay.getTime()) / nd);
        } else {
            return 0;
        }
    }

    /**
     * 获取两个日期间隔月份及天数信息
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     */
    public static String getMonthDayDiff(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            //将Date转为LocalDate
            Instant instant = startDate.toInstant();
            Instant instant2 = endDate.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
            LocalDate start = instant.atZone(zoneId).toLocalDate();
            LocalDate end = instant2.atZone(zoneId).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (!start.isBefore(end)) {
                throw new IllegalArgumentException("结束时间必须大于开始时间!");
            }
            Period period = Period.between(start, end);
            int years = period.getYears();
            int months = period.getMonths();
            int days = period.getDays();
            int[] diff = new int[]{years * 12 + months, days};
            String monthDayDiff = diff[0] + "月" + diff[1] + "天";
            //如果月份为0
            if (diff[0] == 0) {
                monthDayDiff = monthDayDiff.substring(monthDayDiff.indexOf("月") + 1, monthDayDiff.length());
            } else if (diff[1] == 0) {
                //如果天数为0
                monthDayDiff = monthDayDiff.substring(0, monthDayDiff.indexOf("月") + 1);
            }
            return monthDayDiff;
        } else {
            return "0";
        }
    }

    /**
     * 获取两个日期周期
     *
     * @param certificateStartTime 开始时间
     * @param certificateStopTime 结束时间
     */
    public static StringBuilder getCycle(Date certificateStartTime, Date certificateStopTime) {
        StringBuilder certificateCycle = new StringBuilder();
        Integer dayInterval = DateUtils.getDayInterval(certificateStartTime, certificateStopTime);
        //如果间隔天数小于30天
        if (dayInterval < 30) {
            certificateCycle.append(dayInterval).append("天");
        } else {
            certificateCycle.append(dayInterval / 30).append("月");
            //如果余数不为0
            if (dayInterval % 30 != 0) {
                certificateCycle.append(dayInterval % 30 + "天");
            }
        }
        return certificateCycle;
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param dayl 为增加的天数
     * @param currdate 指定时间
     */
    public static Date plusDay(int dayl, Date currdate) {
        long day = (int) dayl;
        long time = currdate.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        Date newDate = new Date(time);// 将毫秒数转换成日期
        return newDate;
    }

    /**
     * 指定日期减上天数后的日期
     *
     * @param dayl 为增加的天数
     * @param currdate 指定时间
     */
    public static Date subDay(int dayl, Date currdate) {
        long day = (int) dayl;
        long time = currdate.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time -= day; // 相加得到新的毫秒数
        Date newDate = new Date(time);// 将毫秒数转换成日期
        return newDate;
    }


    /**
     * String转化Calendar
     *
     * @param dateStr 传入时间字符串
     */
    public static Calendar stringToCalendar(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Date转化String
     *
     * @param date 传入时间
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * String转化Date
     *
     * @param dateStr 传入时间字符串
     */
    public static Date stringToDate(String dateStr) {
        SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = YYYY_MM_DD.parse(dateStr);
        } catch (ParseException e) {
            log.error("日期转换异常");
        }
        return date;
    }


}

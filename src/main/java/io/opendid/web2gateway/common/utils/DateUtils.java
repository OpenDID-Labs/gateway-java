/*
 * Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
 * (https://www.mozilla.org/en-US/MPL/2.0/)
 */

package io.opendid.web2gateway.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * date util
 *
 * @author Dong-Jianguo
 * @version 1.0.0
 * @Date: 2021/9/23
 * @history date, modifier,and description
 **/
public class DateUtils {

  public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static SimpleDateFormat dateFormatDD = new SimpleDateFormat("yyyy-MM-dd");

  public static String timestampToDateString(long timestamp) {
    Date date = new Date(timestamp * 1000);
    String result = dateFormat.format(date);
    return result;
  }

  public static String timestampToDateDdString(long timestamp) {
    Date date = new Date(timestamp * 1000);
    String result = dateFormatDD.format(date);
    return result;
  }

  public static Date timestampToDate(long timestamp) {
    Date date = new Date(timestamp * 1000);
    return date;
  }

  public static String formatDefaultString(Date date) {
    String format = dateFormat.format(date);
    return format;
  }

  public static Date addDay(Date date, int addDays) {
    final Date days = org.apache.commons.lang3.time.DateUtils.addDays(date, addDays);
    return days;
  }

  public static Date addMinutes(Date date, int minutes) {
    final Date days = org.apache.commons.lang3.time.DateUtils.addMinutes(date, minutes);
    return days;
  }

  public static Date addSeconds(Date date, int seconds) {
    final Date days = org.apache.commons.lang3.time.DateUtils.addSeconds(date, seconds);
    return days;
  }

  public static Date addHours(Date date, int hours) {
    final Date days = org.apache.commons.lang3.time.DateUtils.addHours(date, hours);
    return days;
  }

  /**
   * method description Seconds remaining until the transit of the next day
   *
   * @param date
   * @return long
   * @date 2021/9/23
   */
  public static long offsetNextDaySecond(Date date) {
    Calendar calendar = Calendar.getInstance();
    // Adjust the date to zero hour of the next day, that is, the current date plus one day,
    // and the hour, minute, second, and millisecond are all set to zero.
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    long second = ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
    return second;
  }

  /**
   * Get current date timestamp(millisecond)
   *
   * @param
   * @return long
   * @date 2021/9/16
   */
  public static long getCurrentTimestamp() {
    return System.currentTimeMillis();
  }

  /**
   * Get current date timestamp(second)
   *
   * @param
   * @return long
   * @date 2021/9/16
   */
  public static long getCurrentTimestamps() {
    return System.currentTimeMillis()/1000;
  }

  public static long dateToTimestamp(Date date) {
    return date.getTime()/1000;
  }

  public static Date getDateFromGmt(String timeZone){
    Date date = new Date();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    df.setTimeZone(TimeZone.getTimeZone(timeZone));
    try {
      return dateFormat.parse(df.format(date));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * String to Date
   *
   * @date 2022/2/24
   * @param dateStr
   * @return java.util.Date
   */
  public static Date stringToDate(String dateStr) {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = format.parse(dateStr);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get the time difference from the current time (unit: minutes)
   *
   * @date 2022/7/21
   * @param date
   * @return long
   */
  public static long getDiffMinute(Date date){
    long diff = System.currentTimeMillis() - date.getTime();
    long diffMinute = diff / (1000 * 60 * 60);
    return diffMinute;
  }

  /**
   * Get the current format time
   * @Date 2023/3/30
   * @Param []
   * @return java.lang.String
  */
  public static String getCurrentFormatDate(){
    Date date = new Date();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String CurrentFormatDate = format.format(date);
    return CurrentFormatDate;
  }

}

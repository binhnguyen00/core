package net.binhnguyen.lib.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static final String date         = "dd-MM-yyyy";
  public static final String timestamp    = "dd-MM-yyyy HH:mm:ss";
  public static final String timestampTz  = "dd-MM-yyyy HH:mm:ssZ";

  public static LocalDate toLocalDate(String target) {
    return LocalDate.parse(target);
  }

  public static LocalDateTime toLocalDateTime(String target) {
    return LocalDateTime.parse(target);
  }

  public static ZonedDateTime toZonedDateTime(String target) {
    return ZonedDateTime.parse(target);
  }

  public static String toTimestampString(LocalDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestamp);
    return target.format(formatter);
  }

  public static String toTimestampTzString(ZonedDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampTz);
    return target.format(formatter);
  }

  public static String toLocalDateString(LocalDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(date);
    return target.format(formatter);
  }
}
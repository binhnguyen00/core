package net.marci.lib.utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static final String date         = "dd-MM-yyyy";
  public static final String timestamp    = "dd-MM-yyyy HH:mm:ss";
  public static final String timestampTz  = "dd-MM-yyyy HH:mm:ssZ";

  public String reformatToTimestamp(LocalDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestamp);
    return target.format(formatter);
  }

  public String reformatToTimestampTz(ZonedDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampTz);
    return target.format(formatter);
  }

  public String reformatToDate(LocalDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(date);
    return target.format(formatter);
  }
}
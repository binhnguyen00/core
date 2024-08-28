package net.binhnguyen.lib.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
public class DateUtils {

  public static final String date         = "dd-MM-yyyy";
  public static final String timestamp    = "dd-MM-yyyy HH:mm:ss";
  public static final String timestampTz  = "dd-MM-yyyy HH:mm:ssZ";

  private static String corrector(String target) {
    if (Objects.isNull(target)) {
      log.warn("Argument is null");
      return null;
    }
    if (target.isEmpty() || target.isBlank()) {
      log.warn("Argument is empty");
      return null;
    }
    if (target.contains("/")) target = target.replaceAll("/", "-");
    return target.trim();
  }

  public static LocalDate toLocalDate(String target) {
    target = corrector(target);
    if (Objects.nonNull(target)) {
      return LocalDate.parse(target);
    } else return null;
  }

  public static LocalDateTime toLocalDateTime(String target) {
    target = corrector(target);
    if (Objects.nonNull(target)) {
      return LocalDateTime.parse(target);
    } else return null;
  }

  public static ZonedDateTime toZonedDateTime(String target) {
    target = corrector(target);
    if (Objects.nonNull(target)) {
      return ZonedDateTime.parse(target);
    } else return null;
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
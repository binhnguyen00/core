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

  public static final String DATE           = "dd-MM-yyyy";
  public static final String TIMESTAMP      = "dd-MM-yyyy HH:mm:ss";
  public static final String TIMESTAMP_TZ   = "dd-MM-yyyy HH:mm:ssZ";

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
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);
      return LocalDate.parse(target, formatter);
    } else return null;
  }

  public static LocalDateTime toLocalDateTime(String target) {
    target = corrector(target);
    if (Objects.nonNull(target)) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP);
      return LocalDateTime.parse(target, formatter);
    } else return null;
  }

  public static ZonedDateTime toZonedDateTime(String target) {
    target = corrector(target);
    if (Objects.nonNull(target)) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_TZ);
      return ZonedDateTime.parse(target, formatter);
    } else return null;
  }

  public static String toTimestampString(LocalDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP);
    return target.format(formatter);
  }

  public static String toTimestampTzString(ZonedDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_TZ);
    return target.format(formatter);
  }

  public static String toLocalDateString(LocalDateTime target) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);
    return target.format(formatter);
  }
}
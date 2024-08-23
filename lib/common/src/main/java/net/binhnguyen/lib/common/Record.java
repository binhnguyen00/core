package net.binhnguyen.lib.common;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author Binh Nguyen
 * @Email jackjack2000.kahp@gmail.com
 */
@Slf4j
public class Record extends LinkedHashMap<String, Object> {

  public Record(String key, Object value) {
    put(key, value);
  }

  public Record() {
    super();
  }

  public String getAsString(String key) {
    return getAsString(key, null);
  }

  public String getAsString(String key, String defaultValue) {
    if (!hasKey(key)) return defaultValue;
    Object value = get(key);
    if (value instanceof String) {
      if (((String) value).isEmpty() && ((String) value).isBlank()) return defaultValue;
      return (String) value;
    }
    else if (Objects.isNull(value)) return defaultValue;
    else return value.toString();
  }

  public Boolean getAsBoolean(String key) {
    return getAsBoolean(key, null);
  }

  public Boolean getAsBoolean(String key, Boolean defaultValue) {
    if (!hasKey(key)) return defaultValue;
    Object value = get(key);
    if (value instanceof Boolean) return (boolean) value;
    else if (value instanceof String) return Boolean.parseBoolean(value.toString());
    else if (Objects.isNull(value)) return defaultValue;
    else {
      log.error("Key {} has Value {} which is not a boolean", key, value);
      return defaultValue;
    }
  }

  public Float getAsFloat(String key) {
    return getAsFloat(key, null);
  }

  public Float getAsFloat(String key, Float defaultValue) {
    if (!hasKey(key)) return defaultValue;
    Object value = get(key);
    if (value instanceof Float) return (Float) value;
    else if (value instanceof String) return Float.parseFloat(value.toString());
    else if (Objects.isNull(value)) return defaultValue;
    else {
      log.error("Key {} has Value {} which is not a float", key, value);
      return defaultValue;
    }
  }

  public Long getAsLong(String key) {
    return getAsLong(key, null);
  }

  public Long getAsLong(String key, Long defaultValue) {
    if (!hasKey(key)) return defaultValue;
    Object value = get(key);
    if (value instanceof Long) return (Long) value;
    else if (value instanceof String) return Long.parseLong(value.toString());
    else if (Objects.isNull(value)) return defaultValue;
    else {
      log.error("Key {} has Value {} which is not a long", key, value);
      return defaultValue;
    }
  }

  public Integer getAsInteger(String key) {
    return getAsInteger(key, null);
  }

  public Integer getAsInteger(String key, Integer defaultValue) {
    if (!hasKey(key)) return defaultValue;
    Object value = get(key);
    if (value instanceof Integer) return (Integer) value;
    else if (value instanceof String) return Integer.parseInt(value.toString());
    else if (Objects.isNull(value)) return defaultValue;
    else {
      log.error("Key {} has Value {} which is not an integer", key, value);
      return defaultValue;
    }
  }

  public Record merge(Record ... records) {
    for (Record record : records) {
      putAll(record);
    }
    return this;
  }

  private boolean hasKey(String key) {
    if (!containsKey(key)) {
      log.error("Key {} not found", key);
      return false;
    } else return true;
  }
}
package net.binhnguyen.lib.utils;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.binhnguyen.lib.common.Record;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
public class RecordUtils {

  public static <T> T convertAsClazz(@NonNull Record singleton, @NonNull Class<T> clazz) {
    final Field[] fields = clazz.getDeclaredFields();
    final List<String> fieldNames = Arrays.stream(fields).map(Field::getName).toList();
    T instance = null;
    try {
      instance = clazz.getDeclaredConstructor().newInstance();
      for (String fieldName : fieldNames) {
        try {
          // Get the field object from the class
          Field field = clazz.getDeclaredField(fieldName);
          field.setAccessible(true); // Make private fields accessible

          // Retrieve the value from the record and set it on the class instance
          Object value = singleton.get(fieldName);
          if (Objects.nonNull(value)) field.set(instance, value);

        } catch (NoSuchFieldException | IllegalAccessException e) {
          log.error("Error setting field value: {}", e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      log.error("Error creating class instance: {}", e.getMessage(), e);
    }

    return instance;
  }

  public static <T> List<T> convertAsClazz(@NonNull List<Record> data, @NonNull Class<T> clazz) {
    List<T> results = new ArrayList<>();
    if (data.isEmpty()) return results;
    data.stream().map(record -> convertAsClazz(record, clazz)).forEach(results::add);
    return results;
  }
}

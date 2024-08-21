package net.marci.lib.utils;

import java.util.Collection;
import java.util.StringJoiner;

public class StringUtils extends org.springframework.util.StringUtils {

  public static String arrayToCommaDelimitedSqlString(Object[] arr) {
    if (arr.length == 0) {
      return "";
    } else if (arr.length == 1) {
      return "'" + arr[0].toString() + "'";
    } else {
      StringJoiner joiner = new StringJoiner(",", "", "");
      for (Object o : arr) {
        joiner.add("'" + o.toString() + "'");
      }
      return joiner.toString();
    }
  }

  public static String collectionToCommaDelimitedSqlString(Collection<?> coll) {
    if (coll.isEmpty()) {
      return "";
    } else if (coll.size() == 1) {
      return "'" + coll.iterator().next().toString() + "'";
    } else {
      StringJoiner joiner = new StringJoiner(",", "", "");
      for (Object o : coll) {
        joiner.add("'" + o.toString() + "'");
      }
      return joiner.toString();
    }
  }
}
package net.binhnguyen.lib.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.binhnguyen.lib.common.Record;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author Bình Nguyễn
 * @Email jackjack2000.kahp@gmail.com
 */

@Slf4j
@Getter
public class DBConnectUtils {

  private final Connection connection;

  public DBConnectUtils(DataSource dataSource) {
    try {
      this.connection = dataSource.getConnection();
      this.connection.setAutoCommit(false);
    } catch (SQLException e) {
      log.error("Failed to connect to database", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public DBConnectUtils(Connection connection) {
    this.connection = connection;
  }

  public void commit() {
    try {
      connection.commit();
    } catch (SQLException e) {
      log.error("Can't Commit", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public void rollback() {
    try {
      connection.rollback();
    } catch (SQLException e) {
      log.error("Can't Rollback", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      log.error("Can't Close connection", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public int executeUpdate(String sql, Record keyValues) {
    sql = assignSqlHolderWithValue(sql, keyValues);
    if (Objects.isNull(sql)) return 0;
    return executeUpdate(sql);
  }

  public int executeUpdate(String sql) {
    try {
      Statement statement = connection.createStatement();
      return statement.executeUpdate(sql);
    } catch (SQLException e) {
      log.error("Can't Execute Update Query", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public void execute(String SQL_QUERY) {
    try {
      Statement statement = connection.createStatement();
      statement.execute(SQL_QUERY);
    } catch (SQLException e) {
      log.error("Can't Execute Query", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public List<Record> execute(String SQL_QUERY_TEMPLATE, Record keyValues) {
    final String SQL_QUERY = assignSqlHolderWithValue(SQL_QUERY_TEMPLATE, keyValues);
    if (Objects.isNull(SQL_QUERY)) return Collections.emptyList();

    List<Record> results;
    try {
      Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet resultSet = stmt.executeQuery(SQL_QUERY);
      results = extractResults(resultSet);
    } catch(SQLException e) {
      log.error("Can't Execute Query", e);
      throw new RuntimeException(e.getMessage());
    }
    return results;
  }

  private List<Record> extractResults(ResultSet resultSet) throws SQLException {
    List<Record> objects = new ArrayList<>();
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();

    while (resultSet.next()) {
      Record row = new Record();
      for (int i = 1; i <= columnCount; i++) {
        String columnName = metaData.getColumnName(i);
        Object columnValue = resultSet.getObject(i);
        row.put(columnName, columnValue);
      }
      objects.add(row);
    }
    return objects;
  }

  /**
   * @param SQL_QUERY Template SQL with keys that defined as `:keyName`
   * @param keyValues A Map of `:keyName` and its value
   * @return A full SQL with actual values
   */
  public String assignSqlHolderWithValue(String SQL_QUERY, Record keyValues) {
    final Record ensured = ensureKeyValues(keyValues);
    System.out.println(DataSerializer.JSON.toString(keyValues));

    if (Objects.isNull(ensured)) return null;

    for (Map.Entry<String, Object> entry : ensured.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      String formatValue;
      switch (value) {
        case Collection<?> collection -> formatValue = StringUtils.collectionToCommaDelimitedSqlString(collection);
        case Object[] objects -> formatValue = StringUtils.arrayToCommaDelimitedSqlString(objects);
        case String str -> {
          if (str.isEmpty() && str.isBlank()) formatValue = "''";
          else formatValue = "'" + value + "'";
        }
        case null, default -> {
          System.out.println(value);
          if (Objects.isNull(value)) formatValue = "NULL";
          else formatValue = String.valueOf(value);
        }
      }

      final String target = ":" + key;
      if (SQL_QUERY.contains(target)) {
        SQL_QUERY = SQL_QUERY.replace(target, formatValue);
      } else {
        log.error("Key {} not found in SQL_QUERY \n{}", target, SQL_QUERY);
        return null;
      }
    }

    log.info("\nExecuted SQL: \n{}", SQL_QUERY);
    return SQL_QUERY;
  }

  private Record ensureKeyValues(Record keyValues) {
    if (Objects.isNull(keyValues)) {
      log.warn("Search KeyValues (Parameters) is null");
      return null;
    } else if (keyValues.isEmpty()) {
      log.warn("Search KeyValues (Parameters) is empty");
      return null;
    } else return keyValues;
  }
}
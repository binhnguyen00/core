package net.binhnguyen.module.deletegraph;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.binhnguyen.lib.common.Record;
import net.binhnguyen.lib.utils.DBConnectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Bình Nguyễn
 * @Email jackjack2000.kahp@gmail.com
 */

@Slf4j
@Getter
@Setter
public class DeleteGraphSQL {

  private String deleteSQL;

  /**
   * In a Query, there are variables highlighted by `:`. e.g :key1, :key2.
   * @see DBConnectUtils#assignSqlHolderWithValue(String, Record) How to replace those variables with actual values? */
  private Record sqlKeyValueMap;

  /** A list of Joined Tables / Child Tables */
  private List<DeleteGraphSQL> preDeleteChildren;

  /** A list of Target Tables / Parent Tables */
  private List<DeleteGraphSQL> postDeleteChildren;

  public DeleteGraphSQL(String deleteSQL, Record sqlKeyValueMap) {
    this.deleteSQL = deleteSQL;
    this.sqlKeyValueMap = sqlKeyValueMap;
  }

  public void addPreDelete(DeleteGraphSQL child) {
    if (Objects.isNull(preDeleteChildren)) preDeleteChildren = new ArrayList<>();
    preDeleteChildren.add(child);
  }

  public void addPostDelete(DeleteGraphSQL child) {
    if (Objects.isNull(postDeleteChildren)) postDeleteChildren = new ArrayList<>();
    postDeleteChildren.add(child);
  }

  public int executeDelete(DBConnectUtils connUtils) {
    if (Objects.nonNull(preDeleteChildren)) {
      for (DeleteGraphSQL child : preDeleteChildren) {
        if (Objects.nonNull(child)) child.executeDelete(connUtils);
      }
    }

    int count = connUtils.executeUpdate(this.deleteSQL, sqlKeyValueMap);
    log.info("EXECUTE DELETE SQL:\n  {}\n\t Removed {} Records", this.deleteSQL, count);
    connUtils.commit();

    if (Objects.nonNull(postDeleteChildren)) {
      for (DeleteGraphSQL child : postDeleteChildren) {
        if (Objects.nonNull(child)) child.executeDelete(connUtils);
      }
    }
    return count;
  }

  public void dumpSql() {
    if (Objects.nonNull(preDeleteChildren)) {
      for (DeleteGraphSQL child : preDeleteChildren) {
        if (Objects.nonNull(child)) child.dumpSql();
      }
    }

    System.out.println(deleteSQL);

    if (Objects.nonNull(postDeleteChildren)) {
      for (DeleteGraphSQL child : postDeleteChildren) {
        if (Objects.nonNull(child)) child.dumpSql();
      }
    }
  }
}
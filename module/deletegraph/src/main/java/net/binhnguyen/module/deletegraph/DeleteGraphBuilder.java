package net.binhnguyen.module.deletegraph;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.binhnguyen.lib.common.Record;
import net.binhnguyen.lib.utils.DBConnectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Bình Nguyễn
 * @Email jackjack2000.kahp@gmail.com
 * @Usage Work like a Binary tree. Each node is a delete query of the target table. <br/>
 *        1. Case OneToOne/OneToMany: Delete from child to parent. <br/>
 *        2. Case ManyToMany: Delete relation table, then delete from child to parent. */
@Slf4j
@Getter
@Setter
public class DeleteGraphBuilder {
  private DBConnectUtils            dbConnectUtils;
  private Class<?>                  entity;
  private String                    table;
  private DeleteGraphJoinType       joinType;
  private String                    joinField;
  private List<Long>                targetIds;
  private List<DeleteGraphBuilder>  childGraphs = new ArrayList<>();


  public DeleteGraphBuilder(DBConnectUtils dbConnectUtils, Class<?> entity, List<Long> candidateIds) {
    this.dbConnectUtils  = dbConnectUtils;
    this.entity          = entity;
    this.targetIds       = candidateIds;

    Table ann = entity.getAnnotation(Table.class);
    if (Objects.nonNull(ann)) table = ann.name();
    buildChildGraph();
  }

  public int doDelete() {
    DeleteGraphSQL graph = buildDeleteGraphSQL();
    if (Objects.isNull(graph)) return 0;
    return graph.executeDelete(dbConnectUtils);
  }

  public void dumpSql() {
    DeleteGraphSQL graph = buildDeleteGraphSQL();
    if (Objects.isNull(graph)) {
      log.error("Can't find Target table with ids: {}", this.targetIds);
      return;
    }
    graph.dumpSql();
  }

  private DeleteGraphSQL buildDeleteGraphSQL() {
    if (targetIds.isEmpty()) return null;
    Record params = new Record("ids", targetIds);
    String delQuery = "DELETE FROM " + table + " WHERE id IN (:ids)";
    if (DeleteGraphJoinType.ManyToMany.equals(this.joinType)) {
      delQuery = "DELETE FROM " + table + " WHERE " + this.joinField + " IN (:ids)";
    }
    DeleteGraphSQL graph = new DeleteGraphSQL(delQuery, params);

    for (DeleteGraphBuilder child : childGraphs) {
      DeleteGraphSQL childGraph = child.buildDeleteGraphSQL();
      if (DeleteGraphJoinType.OneToOne.equals(child.getJoinType())) {
        graph.addPostDelete(childGraph);
      } else {
        graph.addPreDelete(childGraph);
      }
    }

    return graph;
  }

  private void buildChildGraph() {
    DeleteGraphs graphs = entity.getAnnotation(DeleteGraphs.class);
    if (Objects.nonNull(graphs)) {
      for (DeleteGraph deleteGraph : graphs.value()) {
        Class<?> targetCascadeEntity = deleteGraph.target();
        Table tableAnn = targetCascadeEntity.getAnnotation(Table.class);
        String targetCascadeTable;
        if (Objects.isNull(tableAnn)) {
          targetCascadeTable = deleteGraph.table();
        } else targetCascadeTable = tableAnn.name();
        if (!this.targetIds.isEmpty()) {
          Record keyValues = new Record("candidateIds", this.targetIds);
          String SQL_QUERY = buildFindCandidateIdsQuery(deleteGraph, targetCascadeTable);
          List<Record> results = dbConnectUtils.execute(SQL_QUERY, keyValues);
          List<Long> foundIds;
          if (Arrays.asList(DeleteGraphJoinType.OneToOne, DeleteGraphJoinType.ManyToMany).contains(deleteGraph.joinType())) {
            foundIds = extractIdsWithJoinField(results, deleteGraph.joinField());
          } else foundIds = extractIds(results);

          log.info("Execute SQL:\n {}: total {}", SQL_QUERY, foundIds.size());

          DeleteGraphBuilder child = new DeleteGraphBuilder(dbConnectUtils, targetCascadeEntity, foundIds);
          if (Objects.isNull(tableAnn)) {
            child.setTable(deleteGraph.table());
            child.setJoinField(deleteGraph.joinField());
          }
          child.setJoinType(deleteGraph.joinType());
          childGraphs.add(child);
        }
      }
    }
  }

  private String buildFindCandidateIdsQuery(DeleteGraph deleteGraph, String targetCascadeTable) {
    String SQl_QUERY;
    if (DeleteGraphJoinType.OneToOne.equals(deleteGraph.joinType())) {
      SQl_QUERY = "SELECT " + deleteGraph.joinField() + " FROM " + this.table + " WHERE id IN (:candidateIds)";
    } else if(DeleteGraphJoinType.ManyToMany.equals(deleteGraph.joinType())) {
      SQl_QUERY = "SELECT " + deleteGraph.joinField() + " FROM " + targetCascadeTable + " WHERE " + deleteGraph.joinField() + " IN (:candidateIds)" ;
    } else {
      SQl_QUERY = "SELECT id FROM " + targetCascadeTable + " WHERE " + deleteGraph.joinField() + " IN (:candidateIds)" ;
    }
    return SQl_QUERY;
  }

  private List<Long> extractIds(List<Record> results) {
    List<Long> holder = new ArrayList<>(results.size());
    for (Record result : results) {
      Object target = result.get("id");
      if (target instanceof Long) {
        holder.add((Long) target);
      }
    }
    return holder;
  }

  private List<Long> extractIdsWithJoinField(List<Record> results, String joinField) {
    List<Long> holder = new ArrayList<>(results.size());
    for (Record result : results) {
      Object idObject = result.get(joinField);
      if (idObject instanceof Long) {
        holder.add((Long) idObject);
      }
    }
    return holder;
  }
}
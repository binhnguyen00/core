package net.binhnguyen.module.dbConnectService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.binhnguyen.lib.common.Record;
import net.binhnguyen.lib.utils.DBConnectUtils;
import net.binhnguyen.module.deletegraph.DeleteGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Component @Slf4j
public class DBConnectService {

  @Autowired
  private DataSource dataSource;

  private DBConnectUtils connectUtils;

  @PostConstruct
  public void init() {
    this.connectUtils = new DBConnectUtils(dataSource);
  }

  public List<Record> search(String SEARCH_QUERY, Record sqlArgs) {
    return connectUtils.execute(SEARCH_QUERY, sqlArgs);
  }

  public int deleteByIds(Class<?> clazz, List<Long> ids) {
    DeleteGraphBuilder deleteGraphBuilder = new DeleteGraphBuilder(this.connectUtils, clazz, ids);
    int target = deleteGraphBuilder.doDelete();
    if (ids.size() != target) throw new RuntimeException("There were some problems while deleting");
    else return target;
  }

  public <T> T orElse(T comparand, T defaultValue) {
    if (Objects.isNull(comparand)) {
      log.warn("Object is Null. Returning default value: {}", defaultValue);
      return defaultValue;
    } else return comparand;
  }
}
package net.binhnguyen.module.excel.test;

import net.binhnguyen.module.config.ExcelModuleConfig;
import net.binhnguyen.module.excel.ExcelLogic;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.NONE,
  classes = ExcelModuleConfig.class,
  properties = {
    "spring.config.location=classpath:config/application.yaml"
  }
)
@EnableAutoConfiguration(
  exclude = {
    SecurityAutoConfiguration.class
  }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExcelServiceUnitTest {

  @Autowired
  private ExcelLogic logic;

  @Test @Tag("unit")
  public void testAll() throws IOException {
    testCreateWorkbook();
  }

  @Test
  @Tag("unit")
  public void testCreateWorkbook() throws IOException {
    try (Workbook wb = logic.createWorkbook("test.xlsx", "Sheet1")) {
      Assertions.assertNotNull(wb);
      logic.removeWorkbook("test.xlsx");
    }
  }
}
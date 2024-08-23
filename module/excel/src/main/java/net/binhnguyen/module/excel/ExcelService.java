package net.binhnguyen.module.excel;

import net.binhnguyen.lib.common.Record;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service("ExcelService")
public class ExcelService {

  @Autowired
  private ExcelLogic logic;

  @Transactional
  public Workbook createWorkbook(String filePath, String sheetName) throws IOException {
    return logic.createWorkbook(filePath, sheetName);
  }

  @Transactional
  public void writeExcel(List<Record> data, String filePath, String sheetName) throws IOException {
    logic.writeWorkbook(data, filePath, sheetName);
  }

  @Transactional
  public List<Record> readExcel(String filePath, String sheetName) throws IOException {
    return logic.readWorkbook(filePath, sheetName);
  }
}

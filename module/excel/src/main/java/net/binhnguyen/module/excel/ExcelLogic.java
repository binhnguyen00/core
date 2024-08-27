package net.binhnguyen.module.excel;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.binhnguyen.lib.common.Record;
import net.binhnguyen.lib.utils.RecordUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * @author Bình Nguyễn
 * @Email jackjack2000.kahp@gmail.com
 * @NOTICE!!!: Each Workbook can contain as any Sheet as you want. <br/>
 * In each Sheet, the principal is that: <br/>
 *  - The 1st Row should be started with "METADATA", as it contains your information about the sheet. <br/>
 *  - The 2nd Row should be your headers. <br/>
 *  - The rest are your data. <br/>
 * Eg: <br/>
 * [Row 0 : Cell 0] METADATA: Employee Table. Has name, age. <br/>
 * [Row 1 : Cell 0] Jack. <br/>
 * [Row 1 : Cell 1] 24. <br/>
 * [Row 2 : Cell 0] John. <br/>
 * [Row 2 : Cell 1] 25. <br/>
 * ...
 */
@Slf4j
@Component
public class ExcelLogic {

  /**
   * @param pathToSave Path to resources folder. Eg: "workbooks/test.xlsx"
   * @return Apache.poi XSSFWorkbook (.xlsx)
   */
  public Workbook createWorkbook(@NonNull String pathToSave, @NonNull String sheetName) throws IOException {
    try (
      FileOutputStream fos = new FileOutputStream(pathToSave);
      Workbook workbook = new XSSFWorkbook() // workbook .xlsx
    ) {
      workbook.createSheet(sheetName);
      workbook.write(fos);
      return workbook;
    }
  }

  /**
   * @param pathToWorkbook path to resources folder. Eg: "workbooks/test.xlsx"
   * @return Apache.poi Workbook. About the file extension, it depends.
   */
  public Workbook getWorkbook(@NonNull String pathToWorkbook) throws IOException {
    try (
      InputStream is = getClass().getClassLoader().getResourceAsStream(pathToWorkbook);
      Workbook workbook = WorkbookFactory.create(
        Objects.requireNonNull(is, "File not found: " + pathToWorkbook)
      )
    ) {
      return workbook;
    }
  }

  public void removeWorkbook(String pathToWorkbook) {
    File file = new File(pathToWorkbook);
    if (file.exists()) {
      if (file.delete()) {
        log.info("Delete {} successfully.", file.getName());
      } else {
        log.info("Delete {} failed.", file.getName());
      }
    } else {
      log.info("File {} does not exist.", pathToWorkbook);
    }
  }

  public void writeWorkbook(List<Record> data, @NonNull String pathToWorkbook, @NonNull String sheetName) throws IOException {
    try (
      Workbook workbook = createWorkbook(pathToWorkbook, sheetName);
      FileOutputStream fos = new FileOutputStream(pathToWorkbook)
    ) {
      if (data.isEmpty()) return;

      // Get or create the sheet
      Sheet sheet;
      if (sheetName.isEmpty()) {
        sheet = workbook.createSheet();
      } else {
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
          sheet = workbook.createSheet(sheetName);
        }
      }

      // Set headers
      Record firstRecord = data.getFirst();
      Row headerRow = sheet.createRow(0);
      int headerIndex = 0;
      for (String header : firstRecord.keySet()) {
        Cell cell = headerRow.createCell(headerIndex++);
        cell.setCellValue(header);
      }

      // Set data
      int rowIndex = 1; // Start after the header row
      for (Record record : data) {
        Row row = sheet.createRow(rowIndex++);
        int cellIndex = 0;
        for (String header : firstRecord.keySet()) {
          Cell cell = row.createCell(cellIndex++);
          Object value = record.get(header);
          if (value instanceof String) {
            cell.setCellValue((String) value);
          } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
          } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
          } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));
            cell.setCellStyle(dateStyle);
          } else {
            cell.setCellValue(value != null ? value.toString() : "");
          }
        }
      }

      workbook.write(fos);
    }
  }

  public List<Record> readWorkbook(String pathToWorkbook, @NonNull String sheetName) throws IOException {
    List<Record> data = new ArrayList<>();

    try (Workbook workbook = getWorkbook(pathToWorkbook)) {

      // Get the sheet
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
        log.error("Sheet {} does not exist in the file.", sheetName);
        return data;
      }

      // Read header row
      Row firstRow = sheet.getRow(0);
      Row headerRow;
      // Find metadata
      if (getCellValueAsString(firstRow.getCell(firstRow.getFirstCellNum())).startsWith("METADATA")) {
        headerRow = sheet.getRow(firstRow.getFirstCellNum() + 1);
      } else headerRow = firstRow;
      if (headerRow == null) throw new IllegalStateException("The header row is missing.");
      Map<Integer, String> headerMap = new HashMap<>();
      for (Cell cell : headerRow) {
        headerMap.put(cell.getColumnIndex(), cell.getStringCellValue());
      }

      // Read data rows
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue; // Skip empty rows

        Record record = new Record();
        for (Cell cell : row) {
          String header = headerMap.get(cell.getColumnIndex());
          if (header != null) {
            Object value = getCellValueAsString(cell);
            record.put(header, value);
          }
        }
        data.add(record);
      }
    }

    return data;
  }

  public <T> List<T> getDataAsClazz(String pathToWorkbook, String sheetName, Class<T> clazz) throws IOException {
    List<Record> data = readWorkbook(pathToWorkbook, sheetName);
    return RecordUtils.convertAsClazz(data, clazz);
  }



  private String getCellValueAsString(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue();
      case BOOLEAN:
        return Boolean.toString(cell.getBooleanCellValue());
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          return cell.getDateCellValue().toString();
        } else {
          return Double.toString(cell.getNumericCellValue());
        }
      case FORMULA:
        return cell.getCellFormula();
      case BLANK:
        return "";
      default:
        return cell.toString();
    }
  }
}

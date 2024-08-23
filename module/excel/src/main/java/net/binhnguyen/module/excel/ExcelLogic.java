package net.binhnguyen.module.excel;

import lombok.extern.slf4j.Slf4j;
import net.binhnguyen.lib.common.Record;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class ExcelLogic {

  public Workbook createWorkbook(String filePath, String sheetName) throws IOException {
    try (
      FileOutputStream fos = new FileOutputStream(filePath);
      Workbook workbook = new XSSFWorkbook()
    ) {
      if (Objects.isNull(sheetName)) workbook.createSheet();
      else workbook.createSheet(sheetName);
      workbook.write(fos);
      return workbook;
    }
  }

  public void removeWorkbook(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      if (file.delete()) {
        log.info("Delete {} successfully.", file.getName());
      } else {
        log.info("Delete {} failed.", file.getName());
      }
    } else {
      log.info("File {} does not exist.", filePath);
    }
  }

  public void writeWorkbook(List<Record> data, String filePath, String sheetName) throws IOException {
    try (
      Workbook workbook = createWorkbook(filePath, sheetName);
      FileOutputStream fos = new FileOutputStream(filePath)
    ) {
      if (data.isEmpty()) return;

      // Get or create the sheet
      Sheet sheet;
      if (sheetName == null || sheetName.isEmpty()) {
        sheet = workbook.createSheet("Sheet1");
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

  public List<Record> readWorkbook(String filePath, String sheetName) throws IOException {
    List<Record> data = new ArrayList<>();

    try (
      FileInputStream fis = new FileInputStream(filePath);
      Workbook workbook = WorkbookFactory.create(fis) // Open & read the Workbook
    ) {

      // Get the sheet
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
        log.error("Sheet {} does not exist in the file.", sheetName);
        return data;
      }

      // Read header row
      Row headerRow = sheet.getRow(0);
      if (headerRow == null) {
        throw new IllegalStateException("The header row is missing.");
      }

      Map<Integer, String> headerMap = new HashMap<>();
      for (Cell cell : headerRow) {
        headerMap.put(cell.getColumnIndex(), cell.getStringCellValue());
      }

      // Read data rows
      for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
        Row row = sheet.getRow(rowIndex);
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

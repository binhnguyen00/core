package net.binhnguyen.module.excel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.binhnguyen.common.BaseEntity;

@Entity
@Table(name = ExcelWorkbook.TABLE_NAME)
@Getter @Setter @NoArgsConstructor
public class ExcelWorkbook extends BaseEntity {
  static final String TABLE_NAME = "excel_workbook";

  private String filePath;
}

import React from "react";

import "./scss/_table.scss"
import { ColumnDef, Column, Table, VisibilityState, Row } from '@tanstack/react-table'
import { StorageState } from "../Interface";
import { DateUtils } from "../../utilities/DateUtils";
import { DataTableColumn, DataTableProps } from "./UIDataTable";

export function getSelectedIds(table: Table<any>): number[] | undefined {
  try {
    let selectedRowIds = [] as number[]
    const selectedRows: Row<any>[] = table.getSelectedRowModel().rows;
    if (!selectedRows.length) return selectedRowIds;
    else {
      selectedRowIds = table.getSelectedRowModel().rows.map((row) => {
        return row.original.id as number
      });
      return selectedRowIds as number[];
    } 
  } catch (error) {
    console.error(error);
    return;
  }
}

export function getSelectedRows(table: Table<any>): any[] | undefined {
  try {
    let records = [] as any[]
    const selectedRows: Row<any>[] = table.getSelectedRowModel().rows;
    if (!selectedRows.length) return records;
    else {
      records = table.getSelectedRowModel().rows.map((row) => {
        return row.original as any
      });
      return records as any[];
    } 
  } catch (error) {
    console.error(error);
    return;
  }
} 

export function getPinedColumnCSS(column: Column<any>): React.CSSProperties {
  const isPinned = column.getIsPinned();
  return {
    left: isPinned === 'left' ? `${column.getStart('left')}px` : undefined,
    right: isPinned === 'right' ? `${column.getAfter('right')}px` : undefined,
    width: column.getSize(),
    position: isPinned ? 'sticky' : 'relative',
    zIndex: isPinned ? 1 : 0,
  }
}

export function createColumnConfigs({ columns, enableRowSelection }: DataTableProps) {
  try {
    const columnConfigs: ColumnDef<any>[] = columns.map((column: DataTableColumn) => {
      let { customRender, field, header, width = 300 } = column;
      if (!customRender) customRender = (record: any, index: number) => record[field];
      return {
        id: field,
        header: header,
        cell: ({ row }) => (
          customRender(row.original, row.index)
        ),
        size: width,
        // more options...
      } as ColumnDef<any>
    })

    if (enableRowSelection) {
      columnConfigs.unshift({
        id: 'selection',
        header: ({ table }) => {
          return (
          <input type="checkbox"
            checked={table.getIsAllRowsSelected()}
            onChange={table.getToggleAllPageRowsSelectedHandler()}
          />
        )},
        cell: ({ row }) => {
          return(
          <input type="checkbox"
            checked={row.getIsSelected()}
            onChange={row.getToggleSelectedHandler()}
          />
        )},
        maxSize: 30,
      })
    }

    return columnConfigs
  } catch (error) {
    console.error(error);
  }
}

export function processColumnVisibility(columns: DataTableColumn[]): VisibilityState | undefined {
  if (!columns.length) return undefined;
  let columnVisibility = {} as VisibilityState;
  columns.map((column: DataTableColumn) => {
    if (column.hide) columnVisibility[column.field] = false;
    else columnVisibility[column.field] = true;
  })
  return columnVisibility;
}

export function initSqlArgs(
  pattern = "", 
  modifiedTime = DateUtils.subtractMonthsFromNow(3), 
  storageState = [StorageState.ACTIVE]
) {
  return {
    pattern: pattern,
    modifiedTime: modifiedTime,
    storageState: storageState,
  }
}
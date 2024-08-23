import React from "react";
import { BsPlus, BsTrash, BsArchiveFill } from "react-icons/bs";
import { FaListCheck, FaFolderTree, FaRegFolderOpen, FaRegFolderClosed } from "react-icons/fa6"
import { BiSolidArchiveOut } from "react-icons/bi";
import * as Tanstack from '@tanstack/react-table'
import * as TableUtils from "./uitlities";
import * as PopupManager from "../popup/PopupManager";

import { Button, DropdownButton } from "../button/UIButton";
import { SearchBar } from "./UISearchBar";
import { Tooltip } from "../../widget/common/UITooltip";
import { Popover } from "../../widget/popover/UIPopover";

import "./scss/_table.scss"

/**
 * @param selectedRows The Tanstack selected rows object 
 * @param selectedRecords The data that render in table
 * @param selectedRecordsIds The ids of data that render in table
 */
export interface DataTableContext {
  selectedRows: any[]; 
  selectedRecords: any[];
  selectedRecordsIds: number[];
}

export interface DataTableColumn {
  field: string;
  header: string;
  width?: number;
  hide?: boolean;
  customRender?: (record: any, index: number) => any;
}

export interface DataTableProps {
  records: any[];
  columns: DataTableColumn[];
  title?: string;
  height?: number | string;
  debug?: boolean;
  onCreate?: () => void;
  onDelete?: (targetIds: number[]) => void;
  onUseSearch?: (sqlArgs: any) => void;
  onRowSelection?: (selectedRecords: any[]) => void;
  onArchive?: (targetIds: number[]) => void;
  onActive?: (targetIds: number[]) => void;
  enableRowSelection?: boolean;
  toolbarButtons?: React.JSX.Element[];
  footerButtons?: React.JSX.Element[];
  getTableContext?: (tableInstance: DataTableContext) => void;
}

export function DataTable({ 
    title, height = "100%", debug = false, records, columns, enableRowSelection = false, toolbarButtons, footerButtons,
    onCreate, onDelete, onActive, onArchive, onUseSearch, onRowSelection, getTableContext
  }: DataTableProps) {

  const columnConfigs = React.useMemo(
    () => TableUtils.createColumnConfigs({columns, enableRowSelection} as DataTableProps), 
    [columns]
  );
  const [rowSelection, setRowSelection] = React.useState({}) // Row selection state
  const [expandedRows, setExpandedRows] = React.useState({}) // Row expansion state

  const toggleRowExpansion = (rowId) => {
    setExpandedRows((prevState) => ({
      ...prevState,
      [rowId]: !prevState[rowId],
    }));
  };

  const table = Tanstack.useReactTable({
    state: {
      rowSelection,
    },
    data: records,
    columns: columnConfigs,
    columnResizeMode: "onChange",
    columnResizeDirection: "ltr",
    getCoreRowModel: Tanstack.getCoreRowModel(),
    enableRowSelection: enableRowSelection,
    onRowSelectionChange: setRowSelection,
    debugTable: debug,
    debugHeaders: debug,
    debugColumns: debug,
  } as Tanstack.TableOptions<any>)

  // Returns DataTable Context
  React.useEffect(() => {
    if (getTableContext) {
      const tableInstance: DataTableContext = {
        selectedRows: table.getSelectedRowModel().rows || [],
        selectedRecords: TableUtils.getSelectedRecords(table.getSelectedRowModel().rows) || [],
        selectedRecordsIds: TableUtils.getSelectedRecordsIds(table.getSelectedRowModel().rows) || [],
      }
      getTableContext(tableInstance);
    }
  }, [ rowSelection ]);

  return (
    <React.Fragment>

      {/* title */}
      <div className="h5"> {title} </div>

      {/* toolbar */}
      <div className="flex-h justify-content-between">

        {/* toolbar: buttons */}
        <div className="flex-h my-1">
          {onCreate && <Button icon={<BsPlus/>} title="Create" onClick={() => onCreate()} />}
          {onDelete && (
            <Button
              className="ms-1" icon={<BsTrash/>} title="Delete"
              onClick={() => {
                const ids = TableUtils.getSelectedRecordsIds(table.getSelectedRowModel().rows);
                if (!ids?.length) {
                  PopupManager.createWarningPopup(<div> {"Please select at least 1 record"} </div>);
                  return;
                } else onDelete(ids);
              }} />
          )}
          {(() => { // Render Active/Archive buttons
            if (onActive || onArchive) {
              const selectedRecordsIds = TableUtils.getSelectedRecordsIds(table.getSelectedRowModel().rows);
              const activeBtn = onActive ? (
                <div className="clickable text-center" onClick={() => onActive(selectedRecordsIds)}> 
                  <div><BiSolidArchiveOut/></div>
                  {"Active"} 
                </div>
              ) : null;
              const archiveBtn = onArchive ? (
                <div className="clickable text-center" onClick={() => onArchive(selectedRecordsIds)}> 
                  <div><BsArchiveFill/></div>
                  {"Archive"} 
                </div>
              ) : null;
              return <DropdownButton className="ms-1" title="Storage" dropDownItems={[ activeBtn, archiveBtn]}/>
            } else return null;
          })()}
          {(() => { // Render expand all rows Button
            const rows = table.getRowModel().rows;
            const hasChild = rows.some(row => rows.some(childRow => childRow.original["parentId"] === row.original["id"]));
            if (hasChild) {
              return (
                <Tooltip className="ms-1" position="top" content={"Expand All"} tooltip={
                  <div>
                    <FaFolderTree 
                      className="mt-1 mx-1" style={{ cursor: "pointer" }} 
                      onClick={() => {if (rows.length) rows.forEach(row => toggleRowExpansion(row.id))}}/>
                  </div>
                }/>
              )
            } else return null
          })()}
        </div>

        {/* toolbar: search */}
        <div className="flex-h justify-content-end">
          {onUseSearch && <SearchBar onUseSearch={onUseSearch} title={title} />}
        </div>

      </div>

      {/* table */}
      <div style={{ direction: table.options.columnResizeDirection }}>
        <div className="table-container" style={{ height: height }}>

          <table style={{ width: table.getTotalSize() }}>

            {/* table: header */}
            <thead>
              {table.getHeaderGroups().map((headerGroup: Tanstack.HeaderGroup<any>) => (
                <tr key={headerGroup.id}>
                  {headerGroup.headers.map(header => {
                    let { column } = header;
                    const isSelection: boolean = column.id == "selection"
                    return (
                      <th
                        key={header.id}
                        colSpan={header.colSpan}
                        style={{
                          width: header.getSize(),
                          ...TableUtils.getPinedColumnCSS(column) // <-- IMPORTANT: use for Pinning the column 
                        }}
                      >
                        <div className="flex-h justify-content-between">
                          {/* render Header's label */}
                          <div className="whitespace-nowrap">
                            {header.isPlaceholder
                              ? null
                              : Tanstack.flexRender(column.columnDef.header, header.getContext())
                            }
                          </div>
                          <div className="flex-h">
                            {/* pin controller */}
                            {!header.isPlaceholder && column.getCanPin() && !isSelection
                              ? (<>
                                {column.getIsPinned() !== 'left' && !isSelection ? (
                                  <button className="border rounded px-2" onClick={() => column.pin('left')}> {"<"} </button>
                                ) : null
                                }
                                {column.getIsPinned() && !isSelection ? (
                                  <button className="border rounded px-2" onClick={() => column.pin(false)}> {"X"} </button>
                                ) : null
                                }
                                {column.getIsPinned() !== 'right' && !isSelection ? (
                                  <button className="border rounded px-2" onClick={() => column.pin('right')}> {">"} </button>
                                ) : null
                                }
                              </>) : null
                            }
                            {/* resize div */}
                            <div
                              onDoubleClick={() => header.column.resetSize()}
                              onMouseDown={header.getResizeHandler()}
                              onTouchStart={header.getResizeHandler()}
                              className={`resizer ${table.options.columnResizeDirection} ${header.column.getIsResizing() ? 'isResizing' : ''}`}>
                            </div>
                          </div>
                        </div>
                      </th>
                    )
                  })}
                </tr>
              ))}
            </thead>

            {/* table: body */}
            <tbody>
              {(() => {
                  const rows = table.getRowModel().rows;
                  const rootRows = rows.filter(row => !row.original["parentId"]); 

                  const renderRowWithChildren = (row: Tanstack.Row<any>, currentLevel: number) => {
                    const childRows = rows.filter(r => r.original["parentId"] === row.original["id"]);
                    const isExpanded = expandedRows[row.id];
                    return (
                      <React.Fragment key={row.id}>
                        <tr>
                          {row.getVisibleCells().map((cell: Tanstack.Cell<any, unknown>, cellIndex: number) => {
                            const isFirstRootCell = enableRowSelection 
                              ? (cellIndex === 1 && childRows.length > 0) 
                              : (cellIndex === 0 && childRows.length > 0);
                            const isPaddingCell = enableRowSelection ? cellIndex === 1 : cellIndex === 0;
                            return (
                              <td
                                className={isFirstRootCell ? "flex-h justify-content-start" : undefined}
                                key={cell.id}
                                style={{
                                  width: cell.column.getSize(),
                                  paddingLeft: isPaddingCell ? `${currentLevel * 25}px` : undefined, // Only indent the first cell
                                  ...TableUtils.getPinedColumnCSS(cell.column), // <-- IMPORTANT: use for Pinning the column
                                }}
                              >
                                {/* Render the expand/collapse button only in the first cell of the row */}
                                {isFirstRootCell && (
                                  <>{isExpanded 
                                    ? <div>
                                        <FaRegFolderOpen 
                                          style={{ cursor: "pointer" }} className="m-1" 
                                          onClick={(event: any) => toggleRowExpansion(row.id)}/>
                                      </div>
                                    : <div>
                                        <FaRegFolderClosed 
                                          style={{ cursor: "pointer" }} className="m-1" 
                                          onClick={(event: any) => toggleRowExpansion(row.id)}/>
                                      </div> 
                                  }</>
                                )}
                                {Tanstack.flexRender(cell.column.columnDef.cell, cell.getContext())}
                              </td>
                            )}
                          )}
                        </tr>

                        {isExpanded && childRows.map(childRow => renderRowWithChildren(childRow, currentLevel + 1))}
                      </React.Fragment>
                    );
                  };

                  return rootRows.map(rootRow => renderRowWithChildren(rootRow, 0)); // Start with level 0
              })()}
            </tbody>

            {/* table: footer */}
            <tfoot className="py-1">
              {onRowSelection && (
                <Button 
                  title="Select" icon={<div><FaListCheck/></div>}
                  onClick={(event: Event) => {
                    const selectedRecords = TableUtils.getSelectedRecords(table.getSelectedRowModel().rows);
                    if (!selectedRecords.length) {
                      PopupManager.createWarningPopup(<div> {"Please select at least 1 record"} </div>);
                      return;
                    } else onRowSelection(selectedRecords);
                  }}
                />
              )}
              {/* footer: custom buttons */}
              {(() => {
                if (!footerButtons) return null;
                if (footerButtons.length === 0) return null;
                const btns = footerButtons.map((button, index) => (
                  <React.Fragment key={`footer-btn-${index}`}>
                    {button}
                  </React.Fragment>
                ))
                return btns;
              })()}
            </tfoot>

          </table>

        </div>
      </div>
    </React.Fragment>
  )
}
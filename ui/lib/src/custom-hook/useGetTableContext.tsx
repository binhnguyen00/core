import React from "react";
import { Table as TanStackTable } from "@tanstack/react-table";

interface useGetTableContextProps {
  table: TanStackTable<any>;
  getTableContext?: (tableInstance: TanStackTable<any>) => void;
}
export function useGetTableContext({ table, getTableContext }: useGetTableContextProps) {

  React.useEffect(() => {
    if (getTableContext) {
      getTableContext(table);
    }
  }, [getTableContext, table]);
}
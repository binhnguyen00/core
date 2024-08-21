import React from "react";
import { ServerResponse } from "server/Interface";
import { rpc } from "server/RPC";
import * as PopupManager from "../widget/popup/PopupManager";

interface useSearchProps {
  component: string;
  service?: string;
  sqlArgs?: any;
  dependencies?: React.DependencyList;
  updateData:  React.Dispatch<React.SetStateAction<any[]>>;
}
export function useSearch({ component, service = "search", sqlArgs = {}, dependencies = [], updateData }: useSearchProps) {

  const successCB = (response: ServerResponse) => {
    const dataAsArray = response.body as any[];
    updateData(dataAsArray);
    return;
  };

  const failCB = (response: ServerResponse) => {
    PopupManager.createDangerPopup(<div> {response.message} </div>, "Search Error");
    return;
  }

  React.useEffect(() => {
    rpc.call(component, service, { sqlArgs: sqlArgs }, successCB, failCB);
  }, dependencies);
}
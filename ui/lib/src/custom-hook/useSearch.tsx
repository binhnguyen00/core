import React from "react";
import { ServerResponse } from "server/Interface";
import { rpc } from "server/RPC";
import * as PopupManager from "../widget/popup/PopupManager";

type ReturnType = [boolean, any, any[]];

interface useSearchProps {
  component: string;
  service?: string;
  sqlArgs?: any;
  dependencies?: React.DependencyList;
  updateData?:  React.Dispatch<React.SetStateAction<any[]>>;
}
export function useSearch({ component, service = "search", sqlArgs = {}, dependencies = [] }: useSearchProps): ReturnType {
  const [isPending, setIsPending] = React.useState<any>(true);
  const [error, setError] = React.useState<any>(null);
  const [data, setData] = React.useState<any[]>([]);

  const successCB = (response: ServerResponse) => {
    const dataAsArray = response.body as any[];
    setIsPending(false);
    setData(dataAsArray);
  };

  const failCB = (response: ServerResponse) => {
    setError(response);
    setIsPending(false);
    PopupManager.createDangerPopup(<div> {response.message} </div>, "Search Error");
  }

  React.useEffect(() => {
    rpc.call(component, service, { sqlArgs: sqlArgs }, successCB, failCB);
  }, dependencies);


  return [isPending, error, data];
}
import { Api } from "./Api";
import { CallBack, HttpMethod } from "./Interface";

type RPCRequest = {
  version: string,
  component: string,
  service: string,
  parameters: any
}

/**
 * Remote Procedure Call (RPC)
 */
export class RPC extends Api { 

  call(component: string, service: string, params: any, successCB: CallBack, failCB?: CallBack): void {
    const url: string = this.initialUrl("rpc/call");
    const rpcRequest: RPCRequest = {
      version: '1.0',
      component: component,
      service: service,
      parameters: params
    };
    const requestInit: RequestInit = this.initRequest(HttpMethod.POST, rpcRequest);
    this.doFetch(url, requestInit, successCB, failCB);
  }
}

const rpc = new RPC("http://localhost:7080");
export { rpc };
import { Api } from "./Api";
import { CallBack, HttpMethod } from "./Interface";

export class RESTful extends Api {
  
  get(path: string, pathVariables: any, successCB: CallBack, failCB?: CallBack): void {
    var url: string = this.initialUrl(path, pathVariables);
    var requestInit: RequestInit = this.initRequest(HttpMethod.GET);
    this.doFetch(url, requestInit, successCB, failCB);
  }
  
  post(path: string, requestBody: any, successCB: CallBack, failCB?: CallBack): void {
    var url: string = this.initialUrl(path);
    var requestInit: RequestInit = this.initRequest(HttpMethod.POST, requestBody);
    this.doFetch(url, requestInit, successCB, failCB);
  }

  put(path: string, requestBody: any, successCB: CallBack, failCB?: CallBack): void {
    var url: string = this.initialUrl(path);
    var requestInit: RequestInit = this.initRequest(HttpMethod.PUT, requestBody);
    this.doFetch(url, requestInit, successCB, failCB);
  }

  delete(path: string, requestBody: any, successCB: CallBack, failCB?: CallBack): void {
    var url: string = this.initialUrl(path);
    var requestInit: RequestInit = this.initRequest(HttpMethod.DELETE, requestBody);
    this.doFetch(url, requestInit, successCB, failCB);
  }
}

const restful = new RESTful("http://localhost:7080");
export { restful };
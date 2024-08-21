import React from "react";
import { CallBack, HttpMethod, ResponseStatus, ServerResponse } from "./Interface";
import * as PopupManager from "../widget/popup/PopupManager";

export abstract class Api {
  serverUrl: string;
  abortController = new AbortController();

  constructor(serverUrl: string) {
    this.serverUrl = serverUrl;
    this.abortController = new AbortController();
  }

  /**
   * @param path = 'api'
   * @param pathVariables = { search: 'javascript', page: 2, sort: 'desc' }. Usually for HTTP GET
   * @returns https://example.com/api?search=javascript&page=2&sort=desc
   */
  initialUrl(path: string, pathVariables?: any): string {
    if (pathVariables) {
      path = path + '?' + Object.keys(pathVariables).map(function (k) {
        return encodeURIComponent(k) + "=" + encodeURIComponent(pathVariables[k]);
      }).join('&')
    }
    if (path.startsWith('/')) return this.serverUrl + path;
    return this.serverUrl + '/' + path;
  }

  initRequest(method: HttpMethod, requestBody?: any): RequestInit {;
    let body = null;
    if (requestBody) body = JSON.stringify(requestBody);
    let requestInit: RequestInit = {
      method: method,
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        'Access-Control-Allow-Origin': "*"
      },
      mode: 'cors',
      cache: 'no-cache',
      // credentials: 'include',
      redirect: 'follow',
      referrerPolicy: 'no-referrer',
      body: body,
    }
    return requestInit;
  }

  doFetch(url: string, requestInit: RequestInit, successCB: CallBack, failCB?: CallBack) {
    if (!failCB) failCB = (response: ServerResponse) => {
      const errorContent = (<div> {response.message} </div>);
      PopupManager.createDangerPopup(errorContent);
    }

    fetch(url, requestInit)
    .then((response: Response) => {
      return response.json();
    }).then((serverResponse: ServerResponse) => {
      if (serverResponse.status === ResponseStatus.OK) {
        successCB(serverResponse);
      } else {
        if (failCB) failCB(serverResponse);
      }
    }).catch((error: Error) => {
      console.log(`Marci UI Lib Error: \n${error}`);
      alert("Ops! Something wrong with your request :)");
    });
  }
}

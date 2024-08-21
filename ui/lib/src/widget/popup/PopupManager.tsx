import React from "react";
import ReactDomClient from "react-dom/client";
import { Popup } from "./UIPopup";

let activePopups: string[] = []; // Array to track active popup IDs

/**
 * @param {React.ReactElement<any> | string} header 
 * @param {React.ReactElement<any>} body 
 * @usage Create a React root, then render <Popup/>
 */
export function createPopup(header?: React.ReactElement<any> | string, body?: React.ReactElement<any>) {
  const POPUP_MODAL_ID = `popup-modal-${createPopupId()}`; // Generate a short unique ID for each popup
  let popup = document.getElementById(POPUP_MODAL_ID);

  if (!popup) {
    popup = document.createElement('div');
    popup.setAttribute('id', POPUP_MODAL_ID);
    document.body.appendChild(popup);
  }

  const root = ReactDomClient.createRoot(popup);
  root.render(
    <Popup id={POPUP_MODAL_ID} header={header} body={body} />
  );

  activePopups.push(POPUP_MODAL_ID); // Add the new popup ID to the active popups list
}

export function createSuccessPopup(body?: React.ReactElement<any>, title?: string) {
  if (!title) title = "Success";
  createPopup(<strong className="text-success"> {title} </strong>, body);
}

export function createDangerPopup(body?: React.ReactElement<any>, title?: string) {
  if (!title) title = "Fail";
  createPopup(<strong className="text-danger"> {title} </strong>, body);
}

export function createWarningPopup(body?: React.ReactElement<any>, title?: string) {
  if (!title) title = "Warning";
  createPopup(<strong className="text-warning"> {title} </strong>, body);
}

export function closeCurrentPopup(): void {
  if (activePopups.length > 0) {
    const currentPopupId = activePopups[activePopups.length - 1]; // Get the ID of the last popup created
    closePopupById(currentPopupId);
  } else {
    console.log("No Popup to close");
  }
}

export function closePopupById(popupId: string) {
  const popup = document.getElementById(popupId);
  if (popup) {
    const root = ReactDomClient.createRoot(popup);
    root.unmount();
    document.body.removeChild(popup);
    activePopups = activePopups.filter((id: string) => id !== popupId); // Remove the popup ID from the active popups list
  } else {
    console.log("No Popup found");
  }
}

function createPopupId() {
  return Math.random().toString(36).substring(2, 9);
}
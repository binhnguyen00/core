import React from "react";
import * as icon from "react-icons/bs"; 
import * as PopupManager from "./PopupManager";

declare interface PopupProps {
  id: string;
  header?: React.ReactElement | string;  
  body?: React.ReactElement;
  className?: string;
}

export function Popup(props: PopupProps) {
  let { className = "", body, header, id } = props;
  const [ open, setOpen ] = React.useState(true);
  const popupRef = React.useRef<HTMLDialogElement | null>(null);

  React.useEffect(() => {
    if (!open) {
      PopupManager.closePopupById(id);
    } else {
      popupRef.current?.showModal();
    }
  }, [open, id]);

  const handleClose = () => setOpen(false);

  if (header && typeof header === "string") {
    header = (<div className="h5 p-1"> {header} </div>)
  }
  return (
    <dialog ref={popupRef} className="p-0">
      <div className="flex-v px-1">
        <div className="flex-h justify-content-between">
          {header ? header : null}
          <div className="flex-h justify-content-end py-0 px-0">
            <div className="btn p-0">
              <icon.BsXCircleFill size={"1em"} onClick={handleClose}/>
            </div>
          </div>
        </div>
        {body ? <div className={className}> {body} </div> : null}
      </div>
    </dialog>
  )
};
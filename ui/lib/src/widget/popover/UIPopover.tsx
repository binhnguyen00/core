import React from "react";
import * as icon from "react-icons/bs";
import { 
  Overlay as ReactBsOverlay,
  Popover as ReactBsPopover,
} from "react-bootstrap";
import "./scss/_popover.scss"

import { Button } from "widget/button/UIButton";

interface PopoverProps {
  title?: string;
  header: React.ReactElement | any;
  body: React.ReactElement | any;
  placement: "auto" | "top" | "right" | "bottom" | "left";

  contentWidth?: number;
  contentHeight?: number;
  containerPadding?: number;
  className?: string;
}
export function Popover(
  { title, header, body, placement, 
    containerPadding = 0, contentHeight, contentWidth = 200, className = ""
  }: PopoverProps) {

  const [show, setShow] = React.useState(false);
  const [target, setTarget] = React.useState(null);
  const ref = React.useRef(null);

  const handleClick = (event: any) => {
    setShow(!show);
    setTarget(event.target);
  };

  const iconOrientation = {
    "top": <icon.BsCaretUpSquare />,
    "right": <icon.BsCaretRightSquare />,
    "bottom": <icon.BsCaretDownSquare />,
    "left": <icon.BsCaretLeftSquare />,
    "auto": <icon.BsInfoCircle />,
  };
  const popoverIcon = iconOrientation[placement];

  return (
    <div className={className} ref={ref}>
      <Button onClick={handleClick} title={title} icon={popoverIcon}/>

      <ReactBsOverlay
        show={show} target={target} placement={placement} 
        container={ref} containerPadding={0} transition
      >
        
        <ReactBsPopover  style={{ width: contentWidth, minHeight: contentHeight }}>
          <ReactBsPopover.Header 
            style={{ width: contentWidth, minHeight: contentHeight }} as="h5"
          >
            {header}  
          </ReactBsPopover.Header>
          <ReactBsPopover.Body
            style={{ width: contentWidth, minHeight: contentHeight, padding: containerPadding }}
          >
            {body}
          </ReactBsPopover.Body>
        </ReactBsPopover>

      </ReactBsOverlay>
    </div>
  )
}
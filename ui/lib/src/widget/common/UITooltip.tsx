import React from "react";

interface TooltipProps {
  className?: string;
  tooltip: React.ReactElement | any;
  content: React.ReactElement | any;
  position?: "top" | "right" | "bottom" | "left" | "auto";
}

export function Tooltip({ className = "", tooltip = "", content = "", position = "top"}: TooltipProps) {

  const positionMap = {
    top: "tooltip-top",
    right: "tooltip-right",
    bottom: "tooltip-bottom",
    left: "tooltip-left",
  }
  const pos = positionMap[position];
  
  return (
    <div className={`tooltip ${className && className}`}> 
      {tooltip && tooltip}
      {content && <div className={`tooltip-content ${pos}`}> {content} </div>}
    </div>
  )
}
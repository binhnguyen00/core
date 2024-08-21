import React from "react";
import { ButtonType } from "../Interface";

declare interface ButtonProps {
  title?: string;
  type?: ButtonType;
  className?: string;
  onClick?: (event?: any) => void;
  icon?: JSX.Element;
  iconPosition?: "left" | "right";
  disabled?: boolean;
}

export function Button(props: ButtonProps) {

  let { 
    title = "untitled", 
    type = "primary", 
    className = "", 
    disabled = false, 
    iconPosition = "left", 
    onClick, icon, 
  } = props

  if (!onClick) onClick = () => {
    alert("Ops! Developers are working on it :)");
  };

  return (
    <div className={className}>
      <button 
        type="button" style={{ padding: "0.1rem 0.2rem" }}
        className={`flex-h btn btn-${type}`} 
        onClick={onClick} disabled={disabled}> 
        {
          iconPosition === "left" && icon
          ? <div> {icon} </div>
          : null
        }
        <div className="mx-1"> {title} </div>
        {
          iconPosition === "right" && icon
          ? <div> {icon} </div>
          : null
        }
      </button>
    </div>
  )
}
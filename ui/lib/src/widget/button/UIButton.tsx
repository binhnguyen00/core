import React from "react";
import { BsCaretDownFill  } from "react-icons/bs";
import { ButtonType } from "../Interface";

export interface ButtonProps {
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

interface DropdownButtonProps extends ButtonProps {
  dropDownItems?: React.JSX.Element[];
}

export function DropdownButton({ 
  title = "untitled", type = "primary", icon = <BsCaretDownFill/>,
  disabled = false, className = "", dropDownItems }: DropdownButtonProps) {

  return (
    <div className={`${className}`}>

      <button 
        type="button" 
        className={`flex-h btn btn-${type}`} 
        style={{ padding: "0.1rem 0.2rem" }}
        aria-expanded="false" data-bs-toggle="dropdown"
        disabled={disabled}
      >
        <div className="mx-1"> {title} </div>
        <div> {icon} </div>
      </button>

      <ul className="dropdown-menu">
        {dropDownItems.map((item, index) => {
          return (
            <li className="dropdown-item" key={`dropdown-btn-${index}`}>
              {item}
            </li>
          )
        })}
      </ul>

    </div>
  )
}
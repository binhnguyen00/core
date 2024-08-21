import React from "react";

interface UILabelProps {
  className?: string,
  value?: string
}
export function Label({ className = "", value = "untitled label"}: UILabelProps) {
  return (
    <label className={`text-capitalize font-weight-bold ${className}`}> 
      {value} 
    </label>
  )
}
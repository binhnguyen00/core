import React from "react";
import { FieldProps } from "./Field";

interface FieldStringProps extends FieldProps {}
export function FieldString({ 
  bean, field, className, disabled = false, onChange, hide = false, label, placeholder = "Enter text...", required }: FieldStringProps) {
  
  let [ beanState, setBeanState ] = React.useState(bean);

  React.useEffect(() => {
    setBeanState(bean);
  }, [bean]);

  const onInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const rollbackValue = bean[field];
    const newValue = event.target.value;
    setBeanState((previousState: any) => ({
      ...previousState,
      [field]: newValue,
    }));
    if (onChange) onChange(field, newValue, rollbackValue);
  }

  if (hide) return null;
  return (
    <div className="flex-vbox">
      {label && <div className="text-capitalize font-weight-bold"> {label} </div>}
      <input 
        className={`form-control ${className || ""}`}
        type="text"
        value={beanState[field]}
        onChange={onInputChange}
        placeholder={placeholder}
        disabled={disabled}
      />
    </div>
  );
}

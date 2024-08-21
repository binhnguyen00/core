import React from "react";
import { FieldProps } from "./Field";

interface FieldTextProps extends FieldProps {
  height?: number | string;
}

export function FieldText(props: FieldTextProps) {
  let { bean, field, placeholder = "Enter text...", label, className, onChange, disabled = false, hide = false, height } = props;

  let [ beanState, setBeanState ] = React.useState(bean);

  React.useEffect(() => {
    setBeanState(bean);
  }, [bean]);

  const onInputChange = (event: any) => {
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
      <textarea 
        style={{ height: height }}
        className={`form-control ${className || ""}`}
        value={beanState[field]}
        onChange={onInputChange}
        placeholder={placeholder}
        disabled={disabled}/>
    </div>
  );
}
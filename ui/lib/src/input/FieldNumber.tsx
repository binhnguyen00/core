import React from "react";
import { FieldProps } from "./Field";

interface FieldNumberProps extends FieldProps {}

function formatNumber(target: number, precision: number) {
  if (!target) return '0';
  var token = target.toFixed(precision).split('.');
  token[0] = token[0].replace(/\d(?=(\d{3})+$)/g, '$&,');
  return token.join('.');
}

export function FieldNumber(props: FieldNumberProps) {
  let { bean, field, placeholder = "Enter number...", label, className, onChange, disabled = false, hide = false } = props;

  let [beanState, setBeanState] = React.useState(bean);

  React.useEffect(() => {
    setBeanState(bean);
  }, [bean]);

  const onInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const rollbackValue = bean[field];
    const rawValue = event.target.value;
    const newValue = parseFloat(rawValue);
    setBeanState((previousState: any) => ({
      ...previousState,
      [field]: newValue,
    }));
    if (onChange) onChange(field, newValue, rollbackValue);
  };

  if (hide) return null;
  return (
    <div className="flex-vbox">
      {label && <div className="text-capitalize font-weight-bold"> {label} </div>}
      <input 
        className={`form-control ${className}`}
        type="number"
        value={beanState[field]}
        defaultValue={0}
        onChange={onInputChange}
        placeholder={placeholder}
        disabled={disabled}
      />
    </div>
  );
}

import React from "react";
import * as icon from "react-icons/bs";

import * as TableUtils from "./uitlities";
import { StorageState } from "../Interface";
import { Button } from "../button/UIButton";
import { Popover } from "../../widget/popover/UIPopover";
import { Label } from "../common/UILabel";
import { FieldDate, FieldCheckBox, FieldString } from "../../input";

interface SearchBarProps {
  title?: string;
  onUseSearch?: (sqlArgs: any) => void;
}
export function SearchBar({ title, onUseSearch }: SearchBarProps) {
  const [ sqlArgs, setSqlArgs ] = React.useState({
    ...TableUtils.initSqlArgs()
  });

  const handleInputChange = (field: string, newValue: any, rollBackValue: any) => {
    setSqlArgs((prevState: any) => ({
      ...prevState,
      [field]: newValue,
    }));
  }

  const handleCheckboxChange = (field: string, newValue: any, rollBackValue: any, isChecked: boolean) => {
    let results: any[] = sqlArgs.storageState;
    if (isChecked) results.push(newValue);
    else results = results.filter((state: any) => state !== newValue);
    setSqlArgs((prevState: any) => ({
      ...prevState,
      [field]: results,
    }));
  }

  const doSearch = () => {
    if (onUseSearch) onUseSearch(sqlArgs);
    else console.warn("Should implement create useSearch hook in order to search data");
  }

  return (
    <> 
      <div>
        <Popover 
          title="Filters" header="Filters" placement="bottom" contentWidth={400}
          body={
            <div className="p-1">
              <div className="flex-v">
                <Label value="Storage State"/>
                <div>
                  <FieldCheckBox
                    bean={sqlArgs} field="storageState" checked
                    value={StorageState.ACTIVE} label="Active" onChecked={handleCheckboxChange}/>
                  <FieldCheckBox
                    bean={sqlArgs} field="storageState" 
                    value={StorageState.ARCHIVED} label="Archived" onChecked={handleCheckboxChange}/>
                </div>
              </div>
              <FieldDate 
                bean={sqlArgs} field="modifiedTime" label="Modified Time" onChange={handleInputChange}/>
            </div>
          }
        />  
      </div>
      <FieldString 
        bean={sqlArgs} field="pattern" onChange={handleInputChange} placeholder={`Search ${title}...`}/> 
      <Button className="my-1"
        icon={<icon.BsSearch/>} title="Lookup" type="primary" onClick={doSearch}/>
    </>
  )
}
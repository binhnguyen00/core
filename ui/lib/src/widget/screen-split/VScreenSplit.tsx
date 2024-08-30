import React from "react";
import "./scss/_vscreensplit.scss";

interface VScreenSplitProps {
  components: React.ReactNode[];
}

/**
 * @param {React.ReactNode[]} components A list of components. These'll be render from left -> right. 
 * @description Case no components are provided, nothing will be rendered.  
 *              Case 2 components, consider this case as a file explorer HUD. Left pane fixed width, right pane takes up the remaining space.  
 *              Case > 2 components is not supported yet. Feel free to extend it.
 * @returns 
 */
export function VScreenSplit({ components }: VScreenSplitProps) {
  if (!components || !components.length) return null;

  const [leftPaneWidth, setLeftPaneWidth] = React.useState<number>(200);
  const isResizing = React.useRef<boolean>(false);

  const onMouseDown = (e: React.MouseEvent) => {
    isResizing.current = true;
    document.addEventListener("mousemove", onMouseMove);
    document.addEventListener("mouseup", onMouseUp);
  };

  const onMouseMove = (e: MouseEvent) => {
    if (isResizing.current) {
      const newWidth = e.clientX;
      setLeftPaneWidth(newWidth > 100 ? newWidth : 100); // Minimum width of 100px
    }
  };

  const onMouseUp = () => {
    isResizing.current = false;
    document.removeEventListener("mousemove", onMouseMove);
    document.removeEventListener("mouseup", onMouseUp);
  };

  if (components.length === 1) { // handle only one component
    return components[0];
  }

  if (components.length === 2) { // handle file explorer HUD
    return (
      <div className="flex-h h-100">

        {/* left pane */}
        <div
          className="border-end left-pane"
          style={{ width: leftPaneWidth }}
        >
          {components[0]}
        </div>

        {/* resize controller */}
        <div
          className="resize-handle"
          onMouseDown={onMouseDown}
        />

        {/* right pane */}
        <div className="p-2 right-pane" style={{ width: `calc(100% - ${leftPaneWidth}px)` }}>
          {components[1]}
        </div>

      </div>
    )
  }

  // Handle multiple components
  return (
    <div> Unsupported </div>
  )
}
import { useState } from "react"

export default function CollapseContent({trimContent, fullContent}) {
    
    const [isExpanded, setExpanded] = useState(false);
    
    return (
        <div>
            {!isExpanded && 
            <div>{trimContent}</div>
            }
            {isExpanded &&
            <div>{fullContent}</div>
            }
            <button onClick={()=>{setExpanded(!isExpanded)}}>Expand</button>
        </div>
    );

}
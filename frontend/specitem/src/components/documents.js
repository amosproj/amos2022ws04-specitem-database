import '../App.css';
import { useEffect, useState } from 'react';
import SpecItem from './specItem'

export default function Documents({doc, setSelectDocument}) {
    

    const [selectSpecItem, setSelectSpecItem] = useState(false);
    const [specItem, setSpecItem] = useState(null);

    const handleClick = (specitem)=>{ 
        setSelectSpecItem(true);
        setSpecItem(specitem);
        console.log(specitem)
        
    }
    return(
        <div className="Documents-header">
            {
            !selectSpecItem &&
                <div>
                    
                    <div>{'DOC_ID: '+doc.id}</div>
                    <div>{'DOC_Name: '+doc.name}</div>
                    <div>{'DOC_Version: '+doc.commit}</div>
                    <div>
                        {doc.specItems.map((specItem)=>
                            <button onClick={() => handleClick(specItem)}>{specItem.name}</button>
                        )}
                    </div>
                    <div><button onClick={()=>{setSelectDocument(false)}}>Close</button></div>
                    
                </div>
            } {
                selectSpecItem &&
                <SpecItem specItem = {specItem} setSelectSpecItem = {setSelectSpecItem}/>
            }
        </div>
    )
    
}
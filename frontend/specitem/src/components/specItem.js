import '../App.css';
import { useEffect, useState } from 'react';


export default function specItem({specItem, setSelectSpecItem}) {
    
    return(
        <div className="Documents-header">
            <div>{'SpecItem_Name: '+specItem.name}</div>
            <div>{'SpecItem_Content: '+specItem.content}</div>
            <div><button onClick={()=>{setSelectSpecItem(false)}}>Close</button></div>
        </div>
    )
    
}
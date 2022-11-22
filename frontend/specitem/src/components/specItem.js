import '../App.css';
import { useEffect, useState } from 'react';


export default function specItem({specItem, setSelectSpecItem}) {

    async function handleGet(){

        const response = await fetch('http://localhost:8080/get/all' , {
            method: 'GET',
        });
        const responseText = await response.text();
        console.log(responseText)
    }
    
    return(
        <div className="Documents-header">
            <div>{'SpecItem_Name: '+specItem.name}</div>
            <div>{'SpecItem_Content: '+specItem.content}</div>
            <div><button onClick={()=>{setSelectSpecItem(false)}}>Close</button></div>
            <button onClick={handleGet}>he</button>
        </div>
    )
    
}
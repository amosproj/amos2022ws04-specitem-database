import '../App.css';
import { useEffect, useState } from 'react';
import SpecItem from './specItem'

export default function Documents({doc, setSelectDocument}) {
    

    const [selectSpecItem, setSelectSpecItem] = useState(false);
    const [specItem, setSpecItem] = useState(null);

    const handleClick = (specitem)=>{ 
        setSelectSpecItem(true);
        setSpecItem(specitem);
        
    }
    return(
        <div className="App-tb">
            {
            !selectSpecItem &&
                <div className="App-tb">
                    <div>{'DOC_ID: '+doc.id}</div>
                    <div>{'DOC_Name: '+doc.name}</div>
                    <div>{'DOC_Version: '+doc.commit}</div>
                    
                    <table>
                        <tr>
                            <th>Name</th>
                            <th>Content</th>
                        </tr>
                        {doc.specItems.map((val,key) => {
                        return (
                                <tr key={key}>
                                    <td>{val.name}</td>
                                    <td>{val.content}</td>
                                </tr>
                                )
            })}
      </table>
      <div>
                        {doc.specItems.map((specItem)=>
                            <button className='button' onClick={() => handleClick(specItem)}>{specItem.name}</button>
                        )}
                    </div>
                    <div><button className='button-close' onClick={()=>{setSelectDocument(false)}}>Close</button></div>
                    
                </div>
                
            } {
                selectSpecItem &&
                <SpecItem specItem = {specItem} setSelectSpecItem = {setSelectSpecItem}/>
            }
        </div>
    )
    
}
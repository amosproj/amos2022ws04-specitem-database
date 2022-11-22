import Documents from '../components/documents'
import '../App.css';
import { useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import * as ROUTES from '../constants/routes';
import { toast } from "react-toastify";

export default function SpecitemsPage({ exportList, setExportList}) {

    const [specitemsList, setSpecitemsList] = useState([])
    const [message, setMessage] = useState('');
    const [type, setType] = useState('ID');

    const handleChange = event => {
        setMessage(event.target.value);
      };
    const handleTypeChange = event => {
        setType(event.target.value);
      };  

      async function handleFilter(event) {
        if(message == ''){
            const response = await fetch('http://localhost:8080/get/all' , {
                method: 'GET',
            });
            const responseText = await response.text();
            console.log(responseText)
            if(responseText !== ''){setSpecitemsList(JSON.parse(responseText))}
        }
        else{
            if(type === 'ID'){
                const response = await fetch('http://localhost:8080/get/'+message , {
                        method: 'GET',
                    });
            
                    const responseText = await response.text();
                    console.log(responseText)
                    //console.log(specitemsList)
                    if(responseText !== ''){setSpecitemsList([JSON.parse(responseText)])}
                    //console.log(specitemsList)
                }
                else{
                    const response = await fetch('http://localhost:8080/get/cont:'+message , {
                        method: 'GET',
                    });
            
                    const responseText = await response.text();
                    console.log(responseText)
                    //console.log(specitemsList)
                    if(responseText !== ''){setSpecitemsList(JSON.parse(responseText))}
                    //console.log(specitemsList)
                }
        }
        
            
      };  
    
    useEffect(() => {
        async function handleGet(){
            const response = await fetch('http://localhost:8080/get/all' , {
                method: 'GET',
            });
            const responseText = await response.text();
            console.log(responseText)
            if(responseText !== ''){setSpecitemsList(JSON.parse(responseText))}
        }
        handleGet()
        
      }, []);
          
    function appendExportList() {
        if (specitemsList.length == 0) {
            toast.error("There are no Specitems.")
            return;
        }
        let list = exportList;
        specitemsList.forEach(specitem => {
            if(list.filter(s => s.shortName == specitem.shortName).length > 0) {
                toast(`${specitem.shortName} already exists`);
            }
            list.push(specitem);
        })
        setExportList(list);
        toast.success('Saved')
    }

    return(
        <div style={{width: '100%'}}>
                <div className="save-export">
                    <button className='save-export-button' data-testid="saveExport" onClick={() => appendExportList()}>Save to Export</button>
                </div>
                {specitemsList.length !== 0 &&
                <div>
                    <p data-testid="exportList">{exportList}</p>
                    <div>
                        <input onChange={handleChange}
                            value={message}>

                        </input>
                    <button onClick={handleFilter}>Filter</button>
                    <select onChange={event => handleTypeChange(event)}>
                            <option value="ID">ID</option>
                            <option value="Content">Content</option>
                            
                            
                        </select>
                    </div>
                    
                    <table>
                        <tr>
                            
                            <th>ShortName</th>
                            <th>LongName</th>
                            <th>Commit</th>
                            <th>Version</th>
                            <th></th>
                        </tr>
                        {specitemsList.map((val,key) => {
                        return (
                                <tr key={key}>
                                    
                                    <td>{val.shortName}</td>
                                    <td>{val.longName}</td>
                                    <td>{(val.commit? val.commit.id: '')}</td>
                                    <td>{val.version}</td>
                                    <td><Link to={`/specitem/${val.shortName}`}>
                                            <button className='' >     
                                                Select
                                                </button>  
                                                </Link></td>
                                </tr>
                                )
                            })}
                    </table>
                    <div className='App-tb' style={{marginTop: '15px'}}>
                <Link to={ROUTES.DASHBOARD}>
                <button className='button-close' >     
                Back
            </button>  
                </Link>
                </div>
                    </div>
}
            
         {specitemsList.length === 0 &&
            <div className='App-tb' style={{marginTop:'400px'}}> 
            No Items Found 
            <Link to={ROUTES.DASHBOARD}>
                <button className='button-close' >     
                Back
            </button>  
                </Link>
            </div>
         }       
                     
        </div>
    )
    
}
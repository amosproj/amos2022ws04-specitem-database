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
      
    function selectTableColumns() {  
        console.log("hi");
        if (document.getElementById('ShortNameBox').checked) {
            let selects = document.getElementsByClassName("shortNameCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = '';
            }
        } else {
            let selects = document.getElementsByClassName("shortNameCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = 'none';
            }
        }
        if (document.getElementById('LongNameBox').checked) {
            let selects = document.getElementsByClassName("longNameCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = '';
            }
        } else {
            let selects = document.getElementsByClassName("longNameCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = 'none';
            }
        }
        if (document.getElementById('CommitBox').checked) {
            let selects = document.getElementsByClassName("commitCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = '';
            }
        } else {
            let selects = document.getElementsByClassName("commitCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = 'none';
            }
        }
        if (document.getElementById('VersionBox').checked) {
            let selects = document.getElementsByClassName("versionCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = '';
            }
        } else {
            let selects = document.getElementsByClassName("versionCell");
            for(var i =0, il = selects.length;i<il;i++){
               selects[i].style.display = 'none';
            }
        }
    }
    
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

                    <div>
                        <input type="checkbox" id="ShortNameBox" defaultChecked ></input>
                        <label htmlFor="ShortNameBox">ShortName</label>
                        <input type="checkbox" id="LongNameBox" defaultChecked ></input>
                        <label htmlFor="LongNameBox">LongName</label>
                        <input type="checkbox" id="CommitBox" defaultChecked></input>
                        <label htmlFor="CommitBox">Commit</label>
                        <input type="checkbox" id="VersionBox" defaultChecked></input>
                        <label htmlFor="VersionBox">Version</label>
                        <button onClick={selectTableColumns}>Apply</button>
                    </div>

                    <table>
                        <tr>
                            
                            <th class="shortNameCell">ShortName</th>
                            <th class="longNameCell">LongName</th>
                            <th class="commitCell">Commit</th>
                            <th class="versionCell">Version</th>
                            <th>Link</th>
                        </tr>
                        {specitemsList.map((val,key) => {
                        return (
                                <tr key={key}>
                                    
                                    <td class="shortNameCell">{val.shortName}</td>
                                    <td class="longNameCell">{val.longName}</td>
                                    <td class="commitCell">{(val.commit? val.commit.id: '')}</td>
                                    <td class="versionCell">{val.version}</td>
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
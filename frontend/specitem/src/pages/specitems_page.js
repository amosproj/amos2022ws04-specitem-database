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
        else {
            if (type === 'ID') {
                const response = await fetch('http://localhost:8080/get/' + message, {
                    method: 'GET',
                });

                const responseText = await response.text();
                console.log(responseText)
                //console.log(specitemsList)
                if (responseText !== '') {
                    setSpecitemsList([JSON.parse(responseText)])
                }
                //console.log(specitemsList)
            } else {
                const response = await fetch('http://localhost:8080/get/cont:' + message, {
                    method: 'GET',
                });

                const responseText = await response.text();
                console.log(responseText)
                //console.log(specitemsList)
                if (responseText !== '') {
                    setSpecitemsList(JSON.parse(responseText))
                }
                //console.log(specitemsList)
            }
        }
    }
      
    function selectTableColumns() {
        const matches = document.getElementsByClassName("checkboxClass");

        for (let i = 0; i < matches.length; i++) {
            let checkboxIdToCellClass = matches.item(i).id;
            checkboxIdToCellClass = checkboxIdToCellClass.substring(0, checkboxIdToCellClass.length-3);
            checkboxIdToCellClass = checkboxIdToCellClass + "Cell";

            let selects = document.getElementsByClassName(checkboxIdToCellClass);

            for(const item of selects) {
                if(!matches.item(i).checked) {
                    item.style.display = 'none';
                } else {
                    item.style.display = '';
                }
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
        if (specitemsList.length === 0) {
            toast.error("There are no Specitems.")
            return;
        }
        let list = exportList;
        specitemsList.forEach(specitem => {
            if(list.filter(s => s.shortName === specitem.shortName).length > 0) {
                toast(`${specitem.shortName} already exists`);
            }
            list.push(specitem);
        })
        setExportList(list);
        toast.success('Saved')
    }

    function trimLongerStrings(stringToTrim) {
        if(stringToTrim == null || stringToTrim.length <= 15)
            return stringToTrim;
        else if (stringToTrim.length > 15)
            return stringToTrim.substring(0, 15) + "...";
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
                        <input className="checkboxClass" type="checkbox" id="FingerprintBox" defaultChecked></input>
                        <label htmlFor="fingerprintBox">Fingerprint</label>
                        <input className="checkboxClass" type="checkbox" id="CategoryBox" defaultChecked></input>
                        <label htmlFor="CategoryBox">Category</label>
                        <input className="checkboxClass" type="checkbox" id="LcStatusBox" defaultChecked></input>
                        <label htmlFor="LcStatusBox">LcStatus</label>
                        <input className="checkboxClass" type="checkbox" id="UseInsteadBox" defaultChecked ></input>
                        <label htmlFor="UseInsteadBox">UseInstead</label>
                        <input className="checkboxClass" type="checkbox" id="TraceRefsBox" defaultChecked></input>
                        <label htmlFor="TraceRefsBox">traceRefs</label>
                        <input className="checkboxClass" type="checkbox" id="LongNameBox" defaultChecked></input>
                        <label htmlFor="LongNameBox">LongName</label>
                        <input className="checkboxClass" type="checkbox" id="CommitBox" defaultChecked></input>
                        <label htmlFor="CommitBox">Commit</label>
                        <input className="checkboxClass" type="checkbox" id="VersionBox" defaultChecked></input>
                        <label htmlFor="VersionBox">Version</label>
                        <button onClick={selectTableColumns}>Apply</button>
                    </div>

                    <table>
                        <tbody>
                            <tr>
                                <th className="ShortNameCell">ShortName</th>
                                <th className="FingerprintCell">Fingerprint</th>
                                <th className="CategoryCell">Category</th>
                                <th className="LcStatusCell">LcStatus</th>
                                <th className="UseInsteadCell">UseInstead</th>
                                <th className="TraceRefsCell">traceRefs</th>
                                <th className="LongNameCell">LongName</th>
                                <th className="CommitCell">Commit</th>
                                <th className="VersionCell">Version</th>
                                <th className="ContentCell">Content</th>
                                <th>Link</th>
                            </tr>

                            {specitemsList.map((val,key) => {
                            return (
                                    <tr key={key}>

                                        <td className="ShortNameCell">{trimLongerStrings(val.shortName)}</td>
                                        <td className="FingerprintCell">{trimLongerStrings(val.fingerprint)}</td>
                                        <td className="CategoryCell">{val.category}</td>
                                        <td className="LcStatusCell">{val.lcStatus}</td>
                                        <td className="UseInsteadCell">{val.useInstead}</td>
                                        <td className="TraceRefsCell">{trimLongerStrings(val.traceRefs)}</td>
                                        <td className="LongNameCell">{trimLongerStrings(val.longName)}</td>
                                        <td className="CommitCell">{(val.commit? val.commit.id: '')}</td>
                                        <td className="VersionCell">{val.version}</td>
                                        <td className="ContentCell">{trimLongerStrings(val.content)}</td>

                                        <td><Link to={`/specitem/${val.shortName}`}>
                                                <button className='' >
                                                    Select
                                                    </button>
                                                    </Link></td>
                                    </tr>
                                    )
                                })}
                        </tbody>
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
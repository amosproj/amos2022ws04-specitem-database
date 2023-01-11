import Documents from '../components/documents'
import '../App.css';
import { useContext, useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import * as ROUTES from '../constants/routes';
import { toast } from "react-toastify";
import CollapseContent from '../components/collapseContent';
import Context from '../context/Context';
import PageBar from '../components/pageBar';

export default function SpecitemsPage() {

    const [page, setPage] = useState(1);
    const [maxPage, setMaxPage] = useState(1);
    const [specitemsList, setSpecitemsList] = useState([])
    const [message, setMessage] = useState('');
    const [type, setType] = useState('ID');
    const [limitTraceRef, setLimitTraceRef] = useState('')
    const {exportList, setExportList} = useContext(Context);
    //an array contains all specitem shortnames that are expanded
    const [isExpanded, setExpanded] = useState([]);

    useEffect(() => {
        console.log(limitTraceRef)
      }, [limitTraceRef]);

    useEffect(() => {
        handleGet(page)
    }, [page])

    const handleChange = event => {
        setMessage(event.target.value);
    };
    const handleTypeChange = event => {
        setType(event.target.value);
    };

    async function checkRef(ref){
        return false
    }

    async function handleFilter(event) {
        if(message == ''){
            handleGet(1);
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
                setMaxPage(1);
                setPage(1);
            } else {
                const response = await fetch('http://localhost:8080/get/cont:' + encodeURIComponent(message), { //~!*()'
                    method: 'GET',
                });

                const responseText = await response.text();
                console.log(responseText)
                //console.log(specitemsList)
                if (responseText !== '') {
                    setSpecitemsList(JSON.parse(responseText))
                }
                //console.log(specitemsList)
                setMaxPage(1);
                setPage(1);
            }
        }
    }
      
    function toggleExpanded(shortName) {
        //make deep copy
        let n = []
        isExpanded.forEach(specItem => n.push(specItem))
        //check whether to show or hide
        let index = n.indexOf(shortName);
        if(index == -1)
            n.push(shortName)
        else 
            n.splice(index, 1);
        setExpanded(n);
    }

    async function getMaxPage(){
        const response = await fetch(`http://localhost:8080/pageNumber` , {
            method: 'GET',
        });
        const responseText = await response.text()
        if(responseText=='') 
            console.log("Error get /pageNumber")
        setMaxPage(parseInt(responseText));
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
    
    async function handleGet(page){
        const response = await fetch(`http://localhost:8080/get/all?page=${page}` , {
            method: 'GET',
        });
        const responseText = await response.text();
        console.log(responseText)
        if(responseText !== ''){setSpecitemsList(JSON.parse(responseText))}
        setPage(page);
        await getMaxPage();
    }

    useEffect(() => {
        handleGet(1);
      }, []);
          
      function appendExportList() {
        if (specitemsList.length === 0){
            toast.error("There are no Specitems.")
            return;
        }
        let list = exportList;
        specitemsList.forEach(specitem => {
            if(list.filter(s => (s.shortName === specitem.shortName) & (s.time === specitem.time)).length > 0) {
                toast(`${specitem.shortName + ' ' + timeToString(specitem.time)} already in ExportList`);
            } else {
            list.push(specitem);
            }
        })
        setExportList(list);
        toast.success('Success');
    }

    function trimLongerStrings(stringToTrim) {
        if(stringToTrim == null || stringToTrim.length <= 20){
            return stringToTrim;
        } else if (stringToTrim.length > 20) {
            return stringToTrim.substring(0, 20) + "...";
        }
    }
    
    function timeToString(time){
        let date = time[0]+'-'+('0' + time[1]).slice(-2)+'-'+('0' + time[2]).slice(-2);
        let hour = ('0' + time[3]).slice(-2) + ':' + ('0' + time[4]).slice(-2) + ':' + ('0' + time[5]).slice(-2);
        return  date + '\n' + hour;
    }

    return(
        <div style={{width: '100%'}}>
                <div>
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
                        <label htmlFor="TraceRefsBox">TraceRefs</label>
                        <input className="checkboxClass" type="checkbox" id="LongNameBox" defaultChecked></input>
                        <label htmlFor="LongNameBox">LongName</label>
                        <input className="checkboxClass" type="checkbox" id="CommitBox" defaultChecked></input>
                        <label htmlFor="CommitBox">Commit</label>
                        <input className="checkboxClass" type="checkbox" id="VersionBox" defaultChecked></input>
                        <label htmlFor="VersionBox">Version</label>
                        <input className="checkboxClass" type="checkbox" id="TagBox" defaultChecked></input>
                        <label htmlFor="TagBox">Tags</label>
                        <button onClick={selectTableColumns}>Apply</button>
                    </div>
                    <div className="save-export">
                        <button className='save-export-button' onClick={() => appendExportList()}>Save to Export</button>
                    </div>
                    {specitemsList.length !== 0 &&
                        <div>
                            <div>
                                Displaying items {(page-1)*50+1} - {(page-1)*50 + specitemsList.length}
                            </div>
                            <table>
                                <tbody>
                                    <tr>
                                        <th className="ShortNameCell">ShortName</th>
                                        <th className="FingerprintCell">Fingerprint</th>
                                        <th className="CategoryCell">Category</th>
                                        <th className="LcStatusCell">LcStatus</th>
                                        <th className="UseInsteadCell">UseInstead</th>
                                        <th className="TraceRefsCell">TraceRefs</th>
                                        <th className="LongNameCell">LongName</th>
                                        <th className="CommitCell">Commit</th>
                                        <th className="VersionCell">Version</th>
                                        <th className="ContentCell">Content</th>
                                        <th className="TagCell">Tags</th>
                                        <th>Expand</th>
                                    </tr>

                                    {specitemsList.map((val,key) => {
                                        return [
                                            <tr key={key}>
                                                <td className="ShortNameCell">{trimLongerStrings(val.shortName)}</td>
                                                <td className="FingerprintCell">{trimLongerStrings(val.fingerprint)}</td>
                                                <td className="CategoryCell">{val.category}</td>
                                                <td className="LcStatusCell">{val.lcStatus}</td>
                                                <td className="UseInsteadCell">{val.useInstead}</td>
                                                <td className="TraceRefsCell">
                                                    <div>{(limitTraceRef != val.shortName? trimLongerStrings(val.traceRefs[0]+'...'):
                                                        <table border="2"><tbody>
                                                            {val.traceRefs.map((val,key) => {
                                                                return (
                                                                    <tr key={key}> { !specitemsList.map(a => a.shortName).includes(val)?
                                                                        <td width='10px'>{trimLongerStrings(val)}</td>
                                                                        :
                                                                        <Link to={`/specitem/${val}`}>{trimLongerStrings(val)}</Link>
                                                                    }</tr>
                                                                )
                                                            })}
                                                            <button onClick={(val)=>{setLimitTraceRef(''); console.log(limitTraceRef)}}>Close</button>
                                                        </tbody></table>)}
                                                        <div></div>
                                                        {limitTraceRef != val.shortName && <button onClick={()=>{setLimitTraceRef(val.shortName)}}>Expand</button>}
                                                    </div>
                                                </td>
                                                <td className="LongNameCell">{trimLongerStrings(val.longName)}</td>
                                                <td className="CommitCell">{(val.commit? val.commit.id: '')}</td>
                                                <td className="VersionCell">{val.version}</td>
                                                <td className="ContentCell">{trimLongerStrings(val.content)}</td>
                                                <td className="TagCell">{val.tagInfo && val.tagInfo.tags? val.tagInfo.tags: ''}</td>
                                                <td>
                                                    <button onClick={() => toggleExpanded(val.shortName)}>
                                                        {isExpanded.includes(val.shortName)? "Hide" : "Show"}
                                                    </button>
                                                </td>
                                            </tr>,
                                            isExpanded.includes(val.shortName) && (
                                                <tr>
                                                    <td colSpan="20"><CollapseContent specitem={val} specitemsList={specitemsList}></CollapseContent></td>
                                                </tr>
                                            )
                                        ]
                                    })}
                                </tbody>
                            </table>
                            <PageBar page={page} setPage={setPage} maxPage={maxPage}></PageBar>
                        </div>
                    }
                    {specitemsList.length === 0 &&
                        <div className='App-tb' style={{marginTop:'200px'}}> 
                            No Items Found
                        </div>
                    }
                    <div className='App-tb' style={{marginTop: '15px'}}>
                        <Link to={ROUTES.DASHBOARD}>
                        <button className='button-close'>Back</button>  
                        </Link>
                    </div>
                </div>
        </div>
    )
}

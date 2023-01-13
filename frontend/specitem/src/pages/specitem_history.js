import '../App.css';
import { useContext, useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import * as ROUTES from '../constants/routes';
import { toast } from "react-toastify";
import { useParams } from 'react-router-dom'
import CollapseContent from '../components/collapseContent';
import Context from '../context/Context';

export default function SpecitemsPage() {

    const { id } = useParams()
    const [specitemsList, setSpecitemsList] = useState([])
    const [message, setMessage] = useState('');
    const [type, setType] = useState('Content');
    const [limitTraceRef, setLimitTraceRef] = useState('')
    const [renderOutput, setRenderOutput] = useState([]);
    const { exportList, setExportList} = useContext(Context);
    const [isExpanded, setExpanded] = useState([]);
    const [isCompare, setIsCompare] = useState(false)
    const [compList, setCompList] = useState([]);
    const [respList, setRespList] = useState([]);
    const maxStringLength = 20;
    
    useEffect(() => {
        if(respList.length>0){
            setRenderOutput(respList.map(item => <div style={{ marginBottom:'10px'}}> 
            <div>{item.field}</div>
            <div dangerouslySetInnerHTML={{__html: item.markupText}} />
            </div>))
           
        }
      }, [respList]);

    useEffect(() => {
        //console.log(limitTraceRef)
      }, [limitTraceRef,renderOutput,respList,compList]);
      useEffect(() => {
        console.log('girdi');
        console.log(renderOutput)
        if(renderOutput.length>0){
            setIsCompare(true);
           
        }
      }, [renderOutput]);
    const handleChange = event => {
        setMessage(event.target.value);
    };
    const handleTypeChange = event => {
        setType(event.target.value);
    };

    async function handleFilter(event) {
        if(message == ''){
            const response = await fetch('http://localhost:8080/get/history/'+id , {
                method: 'GET',
            });
            const responseText = await response.text();
            console.log(responseText)
            if(responseText !== ''){setSpecitemsList(JSON.parse(responseText))}
        }
        else {
            if(type === 'Content') {
                const response = await fetch(`http://localhost:8080/get/cont:${message}/id:${id}`, {
                    method: 'GET',
                });
                const responseText = await response.text();
                console.log(responseText)
                if (responseText !== '') {
                    let body = JSON.parse(responseText);
                    let tmp = [];
                    for(let i = 0; i < body.length; i++){
                        if (body[i].shortName === id){
                            tmp.push(body[i]);
                        }
                    }
                    setSpecitemsList(tmp.sort(compare))
                }
            }
        }
    }

    function toggleExpanded(time) {
        //make deep copy
        let n = []
        isExpanded.forEach(s => n.push(s))
        //check whether to show or hide
        let index = n.indexOf(time);
        if(index == -1)
            n.push(time)
        else 
            n.splice(index, 1);
        setExpanded(n);
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

    function compare(a, b) {
        if (a.time > b.time){
          return -1;
        }
        if (a.time < b.time){
          return 1;
        }
        console.log(a.time)
        return 0;
    }
       
    useEffect(() => {
        async function handleGet(){
            const response = await fetch('http://localhost:8080/get/history/'+id , {
                method: 'GET',
            });
            const responseText = await response.text();
            console.log(responseText)
            if(responseText !== ''){
                setSpecitemsList(JSON.parse(responseText).sort(compare))
            }
        }
        handleGet()
        
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
        if (stringToTrim == null || stringToTrim.length <= maxStringLength) {
            return stringToTrim;
        } else if (stringToTrim.length > maxStringLength) {
            return stringToTrim.substring(0, maxStringLength) + "...";
        }
    }

    function timeToString(time){
        let date = time[0]+'-'+('0' + time[1]).slice(-2)+'-'+('0' + time[2]).slice(-2);
        let hour = ('0' + time[3]).slice(-2) + ':' + ('0' + time[4]).slice(-2) + ':' + ('0' + time[5]).slice(-2);
        return  date + '\n' + hour;
    }

    async function sendItemsForComparison() {
        let compareCheckboxesList = document.getElementsByClassName("compareCheckbox");
        let checkedSum = 0;
        let itemsForComparisonList = [];
        for (let i = 0; i < compareCheckboxesList.length; i++) {
            if (compareCheckboxesList[i].checked) {
                checkedSum++;
                itemsForComparisonList.push(i);
            }
        }

        if (checkedSum === 2) {
            setCompList(itemsForComparisonList)
            let arrOld = specitemsList[itemsForComparisonList[0]].commit.commitTime
            let arrToDigOld = arrOld.map(num => num.toString().padStart(2, '0'))
            let strOld = arrToDigOld[0]+'-'+arrToDigOld[1]+'-'+arrToDigOld[2]+' ' + arrToDigOld[3]+':'+arrToDigOld[4]+':'+arrToDigOld[5]

            let arrNew = specitemsList[itemsForComparisonList[1]].commit.commitTime
            let arrToDigNew = arrNew.map(num => num.toString().padStart(2, '0'))
            let strNew = arrToDigNew[0]+'-'+arrToDigNew[1]+'-'+arrToDigNew[2]+' ' + arrToDigNew[3]+':'+arrToDigNew[4]+':'+arrToDigNew[5]

            console.log(strNew)
            const response = await fetch('http://localhost:8080/compare/markup/'+id+'?old='+strOld+'&new='+strNew , {
                method: 'GET',
            });
            const responseText = await response.text();
            setRespList(JSON.parse(responseText))

            console.log(renderOutput)
        } else {
            toast.error("Choose only two versions for comparison!")
        }
    }
    //Box 1
    const Box1 = () => { 
        if(compList.length == 2) {
        return (
            <div style={{width: '50%', height: '100%', backgroundColor: 'yellow'}}>
                {specitemsList[compList[0]].commit.commitTime}
            </div>
        );}
        else {
            return (
                <div style={{width: '50%', height: '100%', backgroundColor: 'yellow'}}>
                 No Element
                </div>
            )
        }
    };
    //Box 2
    const Box2 = () => { 
        if(compList.length == 2) {    
            return (
                <div style={{width: '50%', height: '100%', backgroundColor: 'red'}}>
                    {specitemsList[compList[1]].commit.commitTime}
                </div> 
            );}
            else {
                return (
                    <div style={{width: '50%', height: '100%', backgroundColor: 'yellow'}}>
                     No Element
                    </div>
                )
            }
    };

    return(
        <div style={{width: '100%'}}>
            {!isCompare &&
                <div>
                    <div>
                        <input onChange={handleChange}
                            value={message}>
                        </input>
                        <button onClick={handleFilter}>Filter</button>
                        <select onChange={event => handleTypeChange(event)}>
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
                        <input className="checkboxClass" type="checkbox" id="CommitTimeBox" defaultChecked></input>
                        <label htmlFor="CommitTImeBox">Time</label>
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
                        <table>
                            <tbody>
                                <tr>
                                    <th className="CompareCheckboxCell"><button id="compareButton" onClick={()=>{sendItemsForComparison()}}>Compare</button></th>
                                    <th className="ShortNameCell">ShortName</th>
                                    <th className="FingerprintCell">Fingerprint</th>
                                    <th className="CategoryCell">Category</th>
                                    <th className="LcStatusCell">LcStatus</th>
                                    <th className="UseInsteadCell">UseInstead</th>
                                    <th className="TraceRefsCell">TraceRefs</th>
                                    <th className="LongNameCell">LongName</th>
                                    <th className="CommitCell">Commit</th>
                                    <th className="CommitTimeCell">Time</th>
                                    <th className="VersionCell">Version</th>
                                    <th className="ContentCell">Content</th>
                                    <th className="TagCell">Tags</th>
                                    <th>Expand</th>
                                </tr>

                                {specitemsList.map((val,key) => {
                                    return [
                                            <tr key={key}>
                                                <td className="CompareCheckboxCell"><input type="checkbox" id={"chkbox" + key} className="compareCheckbox"></input></td>
                                                <td className="ShortNameCell">{trimLongerStrings(val.shortName)}</td>
                                                <td className="FingerprintCell">{trimLongerStrings(val.fingerprint)}</td>
                                                <td className="CategoryCell">{val.category}</td>
                                                <td className="LcStatusCell">{val.lcStatus}</td>
                                                <td className="UseInsteadCell">{val.useInstead}</td>
                                                <td className="TraceRefsCell">
                                                    <div>{(limitTraceRef != val.shortName? trimLongerStrings(val.traceRefs[0]+'...'): <table border="2" bordercolor="blue"><tbody>
                                                        {val.traceRefs.map((val,key) => {
                                                            return (
                                                            <tr key={key}> { !specitemsList.map(a => a.shortName).includes(val)?
                                                                <td width='10px' >{trimLongerStrings(val)}</td>
                                                                :
                                                                <Link to={`/specitem/${val}`}>{trimLongerStrings(val)}</Link>
                                                            }
                                                            </tr>)
                                                        })}
                                                        <button onClick={(val)=>{setLimitTraceRef(''); console.log(limitTraceRef)}}>Close</button>
                                                    </tbody></table>)}
                                                    <div></div>
                                                    {limitTraceRef != val.shortName && <button onClick={()=>{setLimitTraceRef(val.shortName)}}>Expand</button>}
                                                    </div>
                                                </td>
                                                <td className="LongNameCell">{trimLongerStrings(val.longName)}</td>
                                                <td className="CommitCell">{(val.commit? val.commit.id: '')}</td>
                                                <td className="CommitTimeCell">{val.commit? timeToString(val.commit.commitTime): ''}</td>
                                                <td className="VersionCell">{val.version}</td>
                                                <td className="ContentCell">{trimLongerStrings(val.content)}</td>
                                                <td className="TagCell">{val.tagInfo && val.tagInfo.tags? val.tagInfo.tags: ''}</td>
                                                <td>
                                                    <button onClick={() => toggleExpanded(val.time.join(" "))}>
                                                        {isExpanded.includes(val.time.join(" "))? "Hide" : "Show"}
                                                    </button>
                                                </td>
                                            </tr>,
                                            isExpanded.includes(val.time.join(" ")) && (
                                                <tr>
                                                    <td colSpan="20"><CollapseContent specitem={val} specitemsList={specitemsList}></CollapseContent></td>
                                                </tr>
                                            )
                                    ]
                                })}
                            </tbody>
                        </table>
                    }
                {specitemsList.length === 0 &&
                    <div className='App-tb' style={{marginTop:'200px'}}>
                        No Items Found
                    </div>
                }

                <div className='App-tb' style={{marginTop: '15px'}}>
                    <Link to={ROUTES.DASHBOARD}>
                        <button className='button-close' >
                            Back
                        </button>
                    </Link>
                </div>
            </div>}
            {isCompare &&
                <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '100%', width: '100%', border:'1px solid'}}>
                    <div style={{ }}>
                        <div style={{marginBottom:'15px' }}>Red is old, Green is new Version</div>
                        {respList.length >0 ? renderOutput :null}
                        <div><button className='button-close' onClick={()=> setIsCompare(false)}>
                                Back
                            </button>
                        </div>
                    </div>
                </div>
            }
        </div> 
    )
}
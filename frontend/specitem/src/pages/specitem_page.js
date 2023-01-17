import Documents from '../components/documents'
import '../App.css';
import { useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import * as ROUTES from '../constants/routes';
import { useParams } from 'react-router-dom'
import { toast } from "react-toastify";
import TagsInput from '../components/tagsinput'
import { SERVER_ADRESS } from '../constants/serverAdress';

export default function SpecitemPage() {
    const { id } = useParams()
    const [specitem, setSpecitem] = useState()
    useEffect(() => {
        async function handleGet(){
            const response = await fetch(SERVER_ADRESS+'get/'+id , {
                method: 'GET',
            });
            const responseText = await response.text();
            console.log(responseText)
            if(responseText !== ''){setSpecitem(JSON.parse(responseText))}
        }
        console.log("Sending request to " + SERVER_ADRESS+'get/'+id)
        handleGet()
      }, []);

    function deleteTag(o) {
        let clone = structuredClone(specitem);
        clone.tagInfo.tags = clone.tagInfo.tags.replace(','+o,'');
        clone.tagInfo.tags = clone.tagInfo.tags.replace(o+',','');
        clone.tagInfo.tags = clone.tagInfo.tags.replace(o,'');

        if(clone.tagInfo.tags.length == 0){
            clone.tagInfo.tags = ""
        }
        setSpecitem(clone);
    }

    const saveTags = async (data) => {
       
        const obj = {tagList: specitem.tagInfo.tags, fingerprint: specitem.fingerprint, shortname: specitem.shortName, category: specitem.category, lcStatus: specitem.lcStatus, longname: specitem.longName, content: specitem.content, traceref: specitem.traceRefs, commitHash: specitem.commit.commitHash, commitMsg:specitem.commit.commitMessage, commitTime: specitem.commit.commitTime, commitAuthor: specitem.commit.commitAuthor}
        
        const res = await fetch(SERVER_ADRESS+"post/tags", {
            headers: {
                'Content-Type': 'application/json'
                // 'Content-Type': 'application/x-www-form-urlencoded',
              },
            method: "POST",
            body: JSON.stringify(obj),
        }).then((res) => {
            {if (res.status !== 400){
                console.log(res)
            }
            else{
                console.log(res)
            }}
        });
        window.location.reload(true);
    };
    function addTag(){
        let inputKey = document.getElementById("newKey").value;
        let inputVal = document.getElementById("newValue").value;

        let clone = structuredClone(specitem);

        if(clone.tagInfo.tags == null){
            clone.tagInfo.tags = "";
        }
    
        if(clone.tagInfo.tags.length > 0){
            clone.tagInfo.tags += ", ";
        }
        
        let newTag = inputKey + ':' + inputVal;

        if(inputKey.length < 1){
            toast(`Key cannot be null`);
            return;
        }

        clone.tagInfo.tags = clone.tagInfo.tags + newTag;
        setSpecitem(clone);
        document.getElementById("newKey").value = ""
         document.getElementById("newValue").value = ""
    }
      
    return(
        <div style={{width: '100%'}} className='App-tb'>
        { specitem &&
            <div>
                <div className='history-button-container'> 
                    <Link to={"/specitem/history/" + id}>
                        <button>View History</button>
                    </Link>
                </div>
                <div>
                    ID: {specitem.shortName}
                </div>
                <div>
                    Fingerprint: {specitem.fingerprint}
                </div>
                <div>
                    Category: {specitem.category}
                </div>
                <div>
                    LcStatus: {specitem.lcStatus}
                </div>
                <div>
                    useInstead: {specitem.useInstead}
                </div>
                    traceRefs:
                    {
                        ' ' + specitem.traceRefs.join(", ")
                    }
                <div>
                    Longname: {specitem.longName}
                </div>
                <div>
                    Commit_ID: {specitem.commit? specitem.commit.id : ''}
                </div>
                <div>
                    Version: {specitem.version}
                </div>
                <div>
                    Content: {specitem.content}
                </div>
                <table>
                    <thead>
                        <tr>
                           <th>
                               Key
                            </th>
                            <th>
                                Value
                            </th> 
                            <th>
                                
                            </th> 
                        </tr>
                    </thead>
                <tbody>
                {specitem.tagInfo.tags && specitem.tagInfo.tags.split(",").map(k =>
                    <tr id={k}>
                        <td>
                            <div>{k.split(":")[0]}</div>  
                        </td>
                        <td>
                            <div>{k.split(":")[1]}</div>  
                        </td>
                        <td>
                            <input type="button" value="&#10006;" onClick={() => {deleteTag(k)}}></input>
                        </td>
                    </tr>                                          
                )}   
                    <tr id="newInput">
                        <td>
                            <input placeholder="Key" type="text" id="newKey"></input>
                        </td>  
                        <td> 
                            <input placeholder="Value" type="text" id="newValue"></input>
                        </td>
                        <td>
                            <input type="button" value="+" onClick={() => {addTag()}}></input>
                        </td>
                    </tr>   
                </tbody>
                </table>
                <button onClick={saveTags}>Save Tags</button>
            </div>
            
        }  
            
            <div className='App-tb' style={{marginTop: '15px'}}>
                <Link to={ROUTES.SPECITEMS}>
                <button className='button-close' >     
                    Back
                </button>
                </Link>
            </div>
        </div>
    )
    
}
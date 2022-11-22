import Documents from '../components/documents'
import '../App.css';
import { useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import * as ROUTES from '../constants/routes';
import { useParams } from 'react-router-dom'
import { toast } from "react-toastify";
import TagsInput from '../components/tagsinput'

export default function SpecitemPage({ exportList, setExportList}) {
    const { id } = useParams()
    const [specitem, setSpecitem] = useState()
    useEffect(() => {
        async function handleGet(){

            const response = await fetch('http://localhost:8080/get/'+id , {
                method: 'GET',
            });
            const responseText = await response.text();
            console.log(responseText)
            if(responseText !== ''){setSpecitem(JSON.parse(responseText))}
        }
        
        handleGet()
      }, []);
    
    function appendExportList() {
        let list = exportList;
        if(list.filter(s => s.shortName == specitem.shortName).length > 0) {
            toast.error('Specitem already exists');
            return;
        }
        list.push(specitem);
        setExportList(list);
        toast.success('Saved')
    }

    return(
        <div style={{width: '100%'}} className='App-tb'>
        { specitem &&
            <div>
                <div className="save-export">
                    <button className='save-export-button' onClick={() => appendExportList()}>Save to Export</button>
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
                <div>
                    traceRefs: {specitem.traceRefs}
                </div>
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
                <TagsInput />
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
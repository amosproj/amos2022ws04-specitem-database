import Documents from '../components/documents'
import TagsInput from '../components/tagsinput'
import '../App.css';
import { useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import * as ROUTES from '../constants/routes';
import { useParams } from 'react-router-dom'


export default function SpecitemPage() {
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
    

    return(
        <div style={{width: '100%'}} className='App-tb'>
        { specitem &&
            <div>
                <div>
                    ID: {specitem.shortName}
                </div>
                <div>
                Content: {specitem.content}
                </div>
            </div>
        }        
        <TagsInput />  
            
            <div className='App-tb' style={{marginTop: '15px'}}>
                <Link to={ROUTES.SPECITEMS}>
                    <button className='button-close'>Back</button>  
                </Link>
            </div>

             
          
                
                     
        </div>
    )
    
}
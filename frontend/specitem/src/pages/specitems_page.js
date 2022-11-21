import Documents from '../components/documents'
import '../App.css';
import { useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import * as ROUTES from '../constants/routes';

export default function SpecitemsPage() {

    const [specitemsList, setSpecitemsList] = useState([])
    
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
        console.log(specitemsList)
      }, []);
          

    return(
        <div style={{width: '100%'}}>
            
                {specitemsList.length !== 0 &&
                <div>
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
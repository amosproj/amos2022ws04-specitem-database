import * as ROUTES from '../constants/routes';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { toast } from "react-toastify";

export default function ExportPage({ exportList, setExportList}) {

    const [textFile, setTextFile] = useState(null);

    function handleRemove(shortName) {
        console.log("Removing...");
        setExportList(exportList.filter((specitem) => specitem.shortName != shortName));
    }

    function exportFile() {
        //toString
        if(exportList.length == 0) {
            toast.error("There are no Specitems to be exported.");
            return;
        }
        console.log('Exporting...');
        let text = "";
        exportList.forEach((specitem) => {
            let keys = Object.keys(specitem);
            let values = Object.values(specitem);
            for(let i = 0; i < keys.length; i++) {
                keys[i]= keys[i].charAt(0).toUpperCase() + keys[i].slice(1);
                if(keys[i] == "Content") {
                    text += `${keys[i]}:\n ${values[i]}\n`;
                } else {
                    text += `${keys[i]}: ${values[i]}\n`;
                }
            }
            text += "\n\n\n\n";
        })
        //toFile
        var data = new Blob([text], {type: 'text/plain'});
        if (textFile !== null) {
            window.URL.revokeObjectURL(textFile);
        }
        setTextFile(window.URL.createObjectURL(data));   
        setExportList([]);
    }

    return (
        <div style={{width: '100%'}} className="App-tb">
            <div className="save-export">
                <button className='save-export-button' onClick={() => exportFile()}>Get Download Link</button>
            </div>
            {textFile && <a href={textFile} download="export.txt">Download link</a>}
            <h3>List of SpecItems</h3>
            <div>
                {exportList && exportList.map(specitem => 
                    <div className='row specitem-label'> 
                        <div style={{gridColumn: 'span 8'}}>Specitem: {specitem.shortName}</div>
                        <div style={{gridColumn: 'span 2'}}> 
                            <Link to={`/specitem/${specitem.shortName}`}>
                                <button>     
                                    Select
                                </button>  
                            </Link>
                        </div>
                        <div style={{gridColumn: 'span 2'}}> 
                            <button className='button-remove' onClick={() => handleRemove(specitem.shortName)}>     
                                Remove
                            </button>  
                        </div>
                    </div>
                )}
            </div>
            <Link to={ROUTES.DASHBOARD}>
                <button className='button-close' >     
                    Back
                </button>  
            </Link>
        </div>
    )
}
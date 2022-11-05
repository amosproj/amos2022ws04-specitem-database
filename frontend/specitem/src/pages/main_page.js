import Documents from '../components/documents'
import '../App.css';
import { useEffect, useState } from 'react';

export default function MainPage() {

    const doclist = [
        {id:1,name:'Doc1',commit:1, specItems:[{name:'specItem1', content:'sp1v1'},{name: 'specItem2',content:'sp2v1'}]},
        {id:1,name:'Doc1',commit:2,specItems:[{name:'specItem1', content:'sp1v2'}, {name: 'specItem2',content:'sp2v2'}]},
        {id:2,name:'Doc2',commit:1,specItems:[{name:'specItem3', content:'sp3v1'}, {name: 'specItem4',content:'sp4v1'}]}
    ];
    const [selectDocument, setSelectDocument] = useState(false);
    const [doc, setDocument] = useState({id:0, name:'', commit: 0});
    const [inputVisible, setInputVisible] = useState(false);
    const [docListVisible, setDocListVisible] = useState(false);
    const [file, setFile] = useState(null);

    const handleClick = (doc)=>{ 
        setSelectDocument(true);
        setDocument(doc);
        console.log(doc)
        
    }
    const handleFileChange = (event) => {
        if (event.target.files && event.target.files[0]) {
          setFile(event.target.files[0])

        }
       }

    return(
        <div>
            {
            !selectDocument &&
            <div className='App-logo'>  
                {docListVisible &&
                <div style={{marginTop:'30px'}} className='App-header'> 
                    <div>
                        {doclist.map((doc)=>
                        (<div className='Document'>
                            <button className='button'  onClick={() => handleClick(doc)}>{doc.name+'_v'+doc.commit}</button>
                            </div>)
                        )} 
                        <div className='Document'>
                            <button className='button-close' onClick={() => setDocListVisible(false)}> Close</button>
                        </div>    
                    </div>
                    
                </div>  
                }
                {!docListVisible &&
                <div className='App-header'>
                    {!inputVisible &&
                    <div>
                        <button className='button' onClick={() => setInputVisible(true)}> Add Document</button>
                        <button className='button' onClick={() => setDocListVisible(true)}> Show Documents</button>
                    </div>    
                    }
                    {inputVisible &&
                    <div>
                        <div>
                            <label className="custom-file-upload"> 
                                <input type="file" onChange={handleFileChange}/>
                                Select file 
                            </label>
                        </div>
                        <div>
                            <button className='button' onClick={() => setInputVisible(false)}> Upload</button>
                        </div>
                        <div>
                            <button className='button-close' onClick={() => setInputVisible(false)}> Back</button>
                        </div>   
                    </div>    
                    }
                    
                </div>   
                }
            </div> 
            
            }
            {
             selectDocument &&
             <Documents doc = {doc} setSelectDocument = {setSelectDocument}/>   
            }
        </div>
    )
    
}
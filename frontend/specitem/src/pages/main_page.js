import Documents from '../components/documents'
import '../App.css';
import { useEffect, useState } from 'react';
import { toast } from "react-toastify";
import * as ROUTES from '../constants/routes';
import {Link, useHistory} from 'react-router-dom';
import { SERVER_ADRESS } from '../constants/serverAdress';

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
    const onSubmit = async (data) => {
       
        const formData = new FormData();
        
        formData.append("file", file);
   
        for (var pair of formData.entries()) {
            console.log(pair[0]); 
        }
        
        const res = await fetch(SERVER_ADRESS + "upload/"+file.name, {
            method: "POST",
            body: formData,
        }).then((res) => {
            {if (res.status !== 400){
                console.log(res)
                toast.success("Successfully uploaded");
            }
            else{
                console.log(res)
                toast.error("Upload failed")
            }}
        });
    };

    return(
        <div style={{width: '100%'}}>
                <div className='App-header'>
                    <div className='logo-text'>SpecItem Database</div>
                    {!inputVisible &&
                    <div>


                        <button className='main-button' onClick={() => setInputVisible(true)}>Add Document</button>
                        
                        <Link to={ROUTES.SPECITEMS}>
                        <button className='main-button'> Show Documents</button>
                        </Link>
                        <Link to={ROUTES.EXPORT}>
                        <button className='main-button'> Export</button>
                        </Link>
                    </div>
                    }
                    {inputVisible &&
                    <div style={{justifyContent:'right', alignItems: 'center', display:'block', width:'600px'}}>
                        <div style={{marginBottom:'50px', marginTop:'20px', marginLeft:'40px'}}>Selected file: {file?file.name:''}</div>
                        <div style={{marginLeft:'200px'}}>
                            <label className="custom-file-upload">
                                <input type="file" onChange={handleFileChange}/>
                                Select file 
                            </label>
                        </div>
                        <div>
                            <button style={{marginLeft:'200px'}} className='main-button' onClick={onSubmit}>Upload</button>
                        </div>
                        <div>
                            <button style={{marginLeft:'200px'}} className='button-close' onClick={() => setInputVisible(false)}>Back</button>
                        </div>
                    </div>    
                    }
            </div>
        </div>
    )
}
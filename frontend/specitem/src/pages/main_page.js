import Documents from '../components/documents'
import '../App.css';
import { useEffect, useState } from 'react';

const model = require('../model.js')

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
        
        const res = await fetch("http://localhost:8080/upload/filename", {
            method: "POST",
            body: formData,
        }).then((res) => console.log(res));
        alert(JSON.stringify(`${res.message}, status: ${res.status}`));};

          

    return(
        <div>
            {
            !selectDocument &&
            <div className='App-logo'>  
                
                {docListVisible &&
                <div style={{marginTop:'30px'}} className='App-tb'> 
                    <div className='App-tb'>
                    
                    <table>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Commit</th>
                            <th></th>
                        </tr>
                        {doclist.map((val,key) => {
                        return (
                                <tr key={key}>
                                    <td>{val.id}</td>
                                    <td>{val.name}</td>
                                    <td>{val.commit}</td>
                                    <td><button onClick={() => handleClick(val)} > Open </button></td>
                                </tr>
                                )
                            })}
                    </table>
                </div>
                    <div style={{marginTop: '20px'}}>
                        <button className='button-close' onClick={() => setDocListVisible(false)}> Close</button>
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
                    <div style={{justifyContent:'right', alignItems: 'center',display:'block', width:'600px'}}>
                        <div style={{marginBottom:'50px',marginTop:'20px', marginLeft:'100px'}}>File to Upload : {file.name}</div>
                        <div style={{marginLeft:'200px'}}>
                            <label className="custom-file-upload"> 
                                <input type="file" onChange={handleFileChange}/>
                                Select file 
                            </label>
                        </div>
                        <div>
                            <button style={{marginLeft:'200px'}} className='button' onClick={onSubmit}> Upload</button>
                        </div>
                        <div>
                            <button style={{marginLeft:'200px'}} className='button-close' onClick={() => setInputVisible(false)}> Back</button>
                        </div>   
                    </div>    
                    }
                    
                </div>   
                }
            </div> 
            
            }
            {
             selectDocument &&
            <div className='App-logo'>
                <Documents doc = {doc} setSelectDocument = {setSelectDocument}/>   
            </div>    
            }
        </div>
    )
    
}
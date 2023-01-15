import '../App.css';
import { useEffect, useState } from 'react';
import SpecItem from './specItem'

export default function Documents({doc, setSelectDocument}) {
    

    const [selectSpecItem, setSelectSpecItem] = useState(false);
    const [specItem, setSpecItem] = useState(null);
    const [searchedSpecItem, setSearchedSpecItem] = useState('');
    const [isOpen, setIsOpen] = useState(false);
    const [specItemToShow, setSpecItemToShow] = useState([])

    const handleClick = (specitem)=>{ 
        setSelectSpecItem(true);
        setSpecItem(specitem);
    }
    const handleSearch = (value) => {
        setSearchedSpecItem(value);
        
        if (value.length > 0) {
            setIsOpen(true);
            // eslint-disable-next-line prefer-const
            let specitems = [];
            doc.specItems.forEach(x => {
               
                if (
                    value.toUpperCase() === x.name.toUpperCase().substring(0, value.length)
                  ) {
                    specitems.push(x);
                 
                  }
            })
            if (specitems.length > 0) {
              setSpecItemToShow(specitems);
            
            } else {
              setIsOpen(false);
            }
          } else {
            setIsOpen(false);
          }
    }

    const handleIsOpen = (event) => {
        event.preventDefault()
        setIsOpen(!isOpen)
    }

    const handleSelectSpec = (event, par) => {
        event.preventDefault()
        setSearchedSpecItem(par)
    }

    return(
        <div className="App-tb">
            {
                !selectSpecItem &&
                <div className="App-tb">
                    <input style={{marginTop:'100px', width:'500px', height:'40px'}} value={searchedSpecItem} onChange={({target}) => handleSearch(target.value)} placeholder='Search SpecItem'>
                        
                    </input>
                    {isOpen && (
                        <div style={{marginTop:'1px', borderWidth:'3px', borderStyle:'solid', width:'500px'}}>
                            <div>
                                {specItemToShow.map(e => (
                                    <div style={{paddingTop: '0.25rem',paddingBottom: '0.25rem', alignItems: 'center', display:'flex', paddingRight:'1rem', paddingLeft:'0.5rem', fontSize:'1.25rem', lineHeight:'1.25rem',height:'50px'}}>
                                    <a href='#' onClick={event => {handleSelectSpec(event, e.name);handleIsOpen(event); handleClick(e)}} >
                                    {e.name}
                                    </a>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                <div style={{marginTop:'100px'}}><button className='button-close' onClick={()=>{setSelectDocument(false)}}>Close</button></div>
                </div>
            }
            {
                selectSpecItem &&
                <SpecItem specItem = {specItem} setSelectSpecItem = {setSelectSpecItem}/>
            }
        </div>
    )
}
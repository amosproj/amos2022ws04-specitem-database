import { useEffect, useState } from "react"
import "./pageBar.css"

export default function PageBar({page, setPage}) {

    const [maxPage, setMaxPage] = useState(1);

    useEffect(() => {
        async function getMaxPage(){
            const response = await fetch(`http://localhost:8080/pageNumber` , {
                method: 'GET',
            });
            const responseText = await response.text()
            if(responseText=='') 
                console("Error get /pageNumber")
            setMaxPage(parseInt(responseText));
        }
        getMaxPage();
      }, []);
    

    return (
        <div className="page-bar">
            <button onClick={() => setPage(page-1)} disabled={page==1}>Previous</button>
            <span className="page-number">{page}</span>
            <button onClick={() => setPage(page+1)} disabled={page==maxPage}>Next</button>
        </div>
    )
}
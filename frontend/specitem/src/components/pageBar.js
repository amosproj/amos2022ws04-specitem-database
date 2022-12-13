import { useEffect, useState } from "react"
import "./pageBar.css"

export default function PageBar({page, setPage, maxPage}) {    

    return (
        <div className="page-bar">
            <button onClick={() => setPage(page-1)} disabled={page==1}>Previous</button>
            <span className="page-number">{page}</span>
            <button onClick={() => setPage(page+1)} disabled={page==maxPage}>Next</button>
        </div>
    )
}
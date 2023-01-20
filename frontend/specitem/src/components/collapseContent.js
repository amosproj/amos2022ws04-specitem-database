import "./collapseContent.css";
import { Link } from 'react-router-dom';

export default function CollapseContent({specitemsList, specitem, getPageOfSpecItem, trimLongerStrings}) {

    return (
        <table className="collapse-content">
            <tr>
                <td>ID:</td> <td>{specitem.shortName}</td>
            </tr>
            <tr>
                <td>Fingerprint:</td> <td>{specitem.fingerprint}</td>
            </tr>
            <tr>
                <td>Category:</td> <td>{specitem.category}</td>
            </tr>
            <tr>
                <td>LcStatus:</td> <td>{specitem.lcStatus}</td>
            </tr>
            <tr>
                <td>Use Instead:</td> <td>{specitem.useInstead}</td>
            </tr>
            <tr>
                <td>Trace References:</td> 
                <td>
                    {specitem.traceRefs.map((val,key) => {
                    return (
                        <span key={key}> { 
                                <Link onClick={() => getPageOfSpecItem(val)}>{trimLongerStrings(val)}</Link>
                        } </span>
                    )})
                    }
                </td>
            </tr>
            <tr>
                <td>Longname:</td> <td>{specitem.longName}</td>
            </tr>
            <tr>
                <td>Commit_ID:</td> <td>{specitem.commit? specitem.commit.id : ''}</td>
            </tr>
            <tr>
                <td>Content: </td> <td>{specitem.content}</td>
            </tr>
        </table>
    );

}
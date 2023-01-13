
import { useState } from 'react'
import './tagsinput.css'

function TagsInput({specitem}){
    const [tags, setTags] = useState([])

    const onSubmit = async (data) => {
       
        const obj = {tagList: tags, fingerprint: specitem.fingerprint, shortname: specitem.shortName, category: specitem.category, lcStatus: specitem.lcStatus, longname: specitem.longName, content: specitem.content, traceref: specitem.traceRefs, commitHash: specitem.commit.commitHash, commitMsg:specitem.commit.commitMessage, commitTime: specitem.commit.commitTime, commitAuthor: specitem.commit.commitAuthor}
        
        const res = await fetch("http://localhost:8080/post/tags", {
            headers: {
                'Content-Type': 'application/json'
                // 'Content-Type': 'application/x-www-form-urlencoded',
              },
            method: "POST",
            body: JSON.stringify(obj),
        }).then((res) => {
            {if (res.status !== 400){
                console.log(res)
            }
            else{
                console.log(res)
            }}
        });
    };
    
    function handleKeyDown(e){
        if(e.key !== 'Enter') return
        const value = e.target.value
        if(!value.trim()) return
        setTags([...tags, value])
        e.target.value = ''
    }

    function removeTag(index){
        setTags(tags.filter((el, i) => i !== index))
    }

    return (
        <div className="tags-input-container">
            <div>
            {tags.map((tag, index) => (
                <div className="tag-item" key={index}>    
                    <span className="text">{tag}</span>
                    <span className="close" onClick={() => removeTag(index)}>&times;</span>
                </div>

            ))}
            <input className="tags-input" onKeyDown={handleKeyDown} type="text" placeholder = "Add a tag..."></input>
            </div>
            <button onClick={onSubmit}>Save Tags</button>
        </div>
    )
}

export default TagsInput; 
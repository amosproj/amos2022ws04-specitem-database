import { useState } from 'react'

function TagsInput(){
    const [tags, setTags] = useState([])

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
        <div>
            {tags.map((tag, index) => (
                <div key={index}>    
                    <span>{tag}</span>
                    <span onClick={() => removeTag(index)}>&times;</span>
                </div>
            ))}
            <input onKeyDown={handleKeyDown} type="text" placeholder = "Add a tag..."></input>
        </div>
    )
}

export default TagsInput;
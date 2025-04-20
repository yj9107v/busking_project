import React, {useState} from "react";
import { useNavigate } from "react-router-dom";

const BoardCreatePage = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        title: '',
        content: '',
    });

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        
        const newPost = {
            id: Date.now(),
            ...formData,
            createdAt: new Date().toISOString(),
        };

        navigate('/boards', {state: {newPost}});
    };

    return (
        <div style={{padding: '20px'}}>
            <h2>📝 게시글 작성</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>제목</label><br />
                    <input
                        type="text"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                    />
                </div>
                <br />
                <div>
                    <label>내용</label><br />
                    <textarea
                        name="content"
                        value={formData.content}
                        onChange={handleChange}
                        required
                        rows="20"
                        cols="100"
                    />
                </div>
                <br />
                <button type="submit">등록</button>
            </form>
        </div>
    );
};

export default BoardCreatePage;
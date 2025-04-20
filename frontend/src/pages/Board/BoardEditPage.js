import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const BoardEditPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const originalPost = location.state?.post;

    const [formData, setFormData] = useState({
        ...originalPost,
        id: Number(originalPost.id),
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
        navigate('/boards', {state: {updatedPost: formData}});
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>📝 게시글 수정</h2>
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
                <button type="submit">수정 완료</button>
            </form>
        </div>
    );
};

export default BoardEditPage;
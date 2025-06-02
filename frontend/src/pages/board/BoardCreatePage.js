import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/axios";

const BoardCreatePage = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        title: '',
        content: '',
        userId: 1 // TODO: 로그인 사용자로 교체 예정
    });

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        try {
            const res = await api.post("/boards", formData);
            alert("게시글이 등록되었습니다.");
            navigate("/boards", {state: {newPost: res.data}});
        } catch (err) {
            console.error("등록 실패:", err);
            alert("등록에 실패했습니다.");
        }
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
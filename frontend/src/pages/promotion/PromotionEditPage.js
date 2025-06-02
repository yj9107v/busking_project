import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../../api/axios";

const PromotionEditPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const originalPost = location.state?.post;

    const [formData, setFormData] = useState({
        ...originalPost,
        id: Number(originalPost.id),
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const res = await api.put(`/promotions/${formData.id}`, formData);
            alert("게시글이 수정되었습니다.");
            navigate("/promotions", { state: { updatedPost: res.data } });
        } catch (err) {
            console.error("수정 실패:", err);
            alert("수정에 실패했습니다.");
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>📝 버스커 홍보글 수정</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>제목</label><br />
                    <input type="text" name="title" value={formData.title} onChange={handleChange} required />
                </div>
                <br />
                <div>
                    <label>카테고리</label><br />
                    <select name="category" value={formData.category} onChange={handleChange}>
                        <option value="ART">예술</option>
                        <option value="DANCE">댄스</option>
                        <option value="MUSIC">음악</option>
                        <option value="TALK">토크</option>
                    </select>
                </div>
                <br />
                <div>
                    <label>내용</label><br />
                    <textarea name="content" value={formData.content} onChange={handleChange} required rows="30" cols="150" />
                </div>
                <br />
                <div>
                    <label>장소</label><br />
                    <input type="text" name="place" value={formData.place} onChange={handleChange} placeholder="도로명주소를 입력하세요" required />
                </div>
                <div>
                    <label>사진/영상 URL</label><br />
                    <input type="text" name="mediaUrl" value={formData.mediaUrl} onChange={handleChange} placeholder="ex) https://youtube.com/..." />
                </div>
                <br />
                <button type="submit">수정 완료</button>
            </form>
        </div>
    );
};

export default PromotionEditPage;
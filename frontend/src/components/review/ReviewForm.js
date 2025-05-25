import React, { useState, useEffect } from "react";
import api from "../../api/axios";


const ReviewForm = ({ postId, onAddReview }) => {
    const [userId, setUserId] = useState(null);
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState('');

    useEffect(() => {
        const fetchUserId = async () => {
            try {
                const res = await api.get("/api/users/user", {
                    withCredentials: true,
                });
                setUserId(res.data.userId);
            } catch (err) {
                console.error("로그인된 사용자 정보 가져오기 실패:", err);
            }
        };
        fetchUserId();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        if (!comment.trim()) {
            alert("댓글을 입력해주세요.");
            return;
        }
    
        const newReview = {
            postId,
            userId,
            rating,
            comment,
        };
    
        try {
            const res = await api.post('/reviews', newReview, {
                withCredentials: true,
            });
            alert("리뷰가 등록되었습니다.");
            onAddReview(res.data);
            setRating(5);
            setComment('');
        } catch (err) {
            console.error("리뷰 등록 실패:", err.response?.data || err.message);
            alert("리뷰 등록에 실패했습니다: " + (err.response?.data || err.message));
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>별점:</label>
            <select value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                {[1, 2, 3, 4, 5].map(val => (
                    <option key={val} value={val}>{val}점</option>
                ))}
            </select>
            <textarea
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder="댓글을 입력하세요"
            />
            <button type="submit">등록</button>
        </form>
    );
};

export default ReviewForm;

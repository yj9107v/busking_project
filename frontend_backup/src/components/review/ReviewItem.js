import React, { useState, useEffect } from "react";
import api from "../../api/axios";

const ReviewItem = ({ review, onDelete, onUpdate, postId }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [editedComment, setEditedComment] = useState(review.comment);
    const [editedRating, setEditedRating] = useState(review.rating);
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const res = await api.get(`/reviews/post/${postId}`);
                setReviews(res.data);
            } catch (err) {
                console.error("리뷰 목록 불러오기 실패:", err);
                alert("리뷰 목록을 불러오는 데 실패했습니다.");
            }
        };
        fetchReviews();
    }, [postId]);

    const handleUpdate = async () => {
        try {
            const updated = { ...review, comment: editedComment, rating: editedRating };
            await api.put(`/reviews/${review.id}`, updated);
            onUpdate(updated);
            setIsEditing(false);
        } catch (err) {
            console.error("리뷰 수정 실패:", err);
            alert("리뷰 수정에 실패했습니다.");
        }
    };

    const handleDelete = async () => {
        const confirm = window.confirm("정말 삭제하시겠습니까?");
        if (!confirm) return;

        try {
            // /api 제거
            await api.delete(`/reviews/${review.id}`);
            alert("리뷰가 삭제되었습니다.");
            onDelete(review.id);
        } catch (err) {
            console.error("리뷰 삭제 실패:", err.response?.data || err.message);
            alert("리뷰 삭제에 실패했습니다: " + (err.response?.data || err.message));
        }
    };
    

    return (
        
        <div className="review-item">
            {isEditing ? (
                <>
                    <textarea value={editedComment} onChange={(e) => setEditedComment(e.target.value)} />
                    <select value={editedRating} onChange={(e) => setEditedRating(Number(e.target.value))}>
                        {[1, 2, 3, 4, 5].map(val => (
                            <option key={val} value={val}>{val}점</option>
                        ))}
                    </select>
                    <button onClick={handleUpdate}>💾 수정완료</button>
                    <button onClick={() => setIsEditing(false)}>취소</button>
                </>
            ) : (
                <>
                    <div>⭐ {review.rating}점</div>
                    <div>{review.comment}</div>
                    <button onClick={() => setIsEditing(true)}>✏️ 수정</button>
                    <button onClick={handleDelete}>🗑️ 삭제</button>
                </>
            )}

        <div>
            {reviews.map((review) => (
                <div key={review.id} style={{ border: "1px solid #ccc", padding: "10px", marginBottom: "10px" }}>
                    <div>⭐ {review.rating}점</div>
                    <div>{review.comment}</div>
                    <div style={{ fontSize: "12px", color: "gray" }}>
                        작성일: {new Date(review.createdAt).toLocaleString()}
                    </div>
                </div>
            ))}
        </div>
        </div>
    );
};

export default ReviewItem;

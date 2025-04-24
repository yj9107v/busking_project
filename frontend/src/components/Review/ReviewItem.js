import React, {useState} from "react";
import api from "../../api/axios";

const ReviewItem = ({ review, onDelete, onUpdate }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [editedComment, setEditedComment] = useState(review.comment);
    const [editedRating, setEditedRating] = useState(review.rating);

    const handleUpdate = async () => {
        const updated = {
            ...review,
            comment: editedComment,
            rating: editedRating,
            updatedAt: new Date().toISOString(),
        };
        try {
            const res = await api.put(`/reviews/${review.id}`, updated);
            alert('리뷰가 수정되었습니다.');
            onUpdate(updated);
            setIsEditing(false);
        } catch (err) {
            console.error("리뷰 수정 실패:", err);
            alert("리뷰 수정에 실패했습니다.");
        }
    };

    const handleDelete = async () => {
        const confirm = window.confirm('리뷰를 삭제하시겠습니까?');
        if(!confirm) return;

        try {
            await api.delete(`/reviews/${review.id}`);
            alert('리뷰가 삭제되었습니다.');
            onDelete(review.id);
        } catch (err) {
            console.error('삭제 실패:', err);
            alert('리뷰 삭제에 실패했습니다.');
        }
    };

    return (
        <div style={{ border: '1px solid #ccc', padding: '10px', marginBottom: '10px' }}>
            {isEditing ? (
                <>
                    <div>
                        <label>별점: </label>
                        <select
                            value={editedRating}
                            onChange={(e) => setEditedRating(Number(e.target.value))}
                        >
                            {[1, 2, 3, 4, 5].map((val) => (
                                <option key={val} value={val}>
                                    {val}
                                </option>
                            ))}
                        </select>
                    </div>
                    <textarea
                        value={editedComment}
                        onChange={(e) => setEditedComment(e.target.value)}
                        rows={3}
                        cols={50}
                    />
                    <div>
                        <button onClick={handleUpdate}>💾 수정완료</button>
                        <button onClick={() => setIsEditing(false)}>취소</button>
                    </div>
                </>
            ) : (
                <>
                    <div>⭐ {review.rating}점</div>
                    <div>{review.comment}</div>
                    <div style={{ fontSize: '12px', color: 'gray' }}>
                        작성일: {new Date(review.createdAt).toLocaleString()}
                        {review.updatedAt && 
                        review.updatedAt !== review.createdAt && (
                        <> (수정됨: {new Date(review.updatedAt).toLocaleString()})</>
                        )}
                    </div>
                    <button onClick={() => setIsEditing(true)}>✏️ 수정</button>
                    <button onClick={handleDelete}>🗑️ 삭제</button>
                </>
            )}
        </div>
    );
};

export default ReviewItem;
import React, {useState} from "react";

const ReviewItem = ({ review, onDelete, onUpdate }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [editedComment, setEditedComment] = useState(review.comment);
    const [editedRating, setEditedRating] = useState(review.rating);

    const handleSave = () => {
        const updated = {
            ...review,
            comment: editedComment,
            rating: editedRating,
            updatedAt: new Date().toISOString(),
        };
        onUpdate(updated);
        setIsEditing(false);
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
                        <button onClick={handleSave}>💾 저장</button>
                        <button onClick={() => setIsEditing(false)}>취소</button>
                    </div>
                </>
            ) : (
                <>
                    <div>⭐ {review.rating}점</div>
                    <div>{review.comment}</div>
                    <div style={{ fontSize: '12px', color: 'gray' }}>
                        작성일: {new Date(review.createdAt).toLocaleString()}
                        {review.updatedAt && ` (수정됨: ${new Date(review.updatedAt).toLocaleString()})`}
                    </div>
                    <button onClick={() => setIsEditing(true)}>✏️ 수정</button>
                    <button onClick={() => onDelete(review.id)}>🗑️ 삭제</button>
                </>
            )}
        </div>
    );
};

export default ReviewItem;
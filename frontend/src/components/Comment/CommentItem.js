import React, { useState } from 'react';
import CommentForm from './CommentForm';

const CommentItem = ({ comment, replies, onDelete, onReply, onUpdate }) => {
    const [showReplyForm, setShowReplyForm] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [editedContent, setEditedContent] = useState(comment.content);

    const handleEditSubmit = (e) => {
        e.preventDefault();
        if (!editedContent.trim()) return;

        const updated = {
            ...comment,
            content: editedContent,
            updatedAt: new Date().toISOString(),
        };
        onUpdate(updated);
        setIsEditing(false);
    };

    return (
        <div style={{ marginLeft: comment.parentId ? 30 : 0, marginTop: '10px' }}>
            <div style={{ border: '1px solid #ccc', padding: '10px' }}>
                {isEditing ? (
                    <form onSubmit={handleEditSubmit}>
                        <textarea
                            value={editedContent}
                            onChange={(e) => setEditedContent(e.target.value)}
                            rows={3}
                            cols={60}
                        />
                        <br />
                        <button type="submit">💾 저장</button>
                        <button onClick={() => setIsEditing(false)} type="button" style={{ marginLeft: '10px' }}>
                            취소
                        </button>
                    </form>
                ) : (
                    <>
                        <p>{comment.content}</p>
                        <small style={{ color: 'gray' }}>
                            작성일: {new Date(comment.createdAt).toLocaleString()}
                            {comment.updatedAt && ` (수정됨: ${new Date(comment.updatedAt).toLocaleString()})`}
                        </small>
                        <br />
                        <button onClick={() => setShowReplyForm((prev) => !prev)}>
                            {showReplyForm ? '답글 닫기' : '답글 입력'}
                        </button>
                        <button onClick={() => setIsEditing(true)} style={{ marginLeft: '10px' }}>
                            ✏️ 수정
                        </button>
                        <button onClick={() => onDelete(comment.id)} style={{ marginLeft: '10px' }}>
                            삭제
                        </button>
                    </>
                )}
            </div>

            {showReplyForm && !isEditing && (
                <CommentForm
                    postId={comment.postId}
                    parentId={comment.id}
                    onSubmit={(newReply) => {
                        onReply(newReply);
                        setShowReplyForm(false);
                    }}
                />
            )}

            {replies?.map((reply) => (
                <CommentItem
                    key={reply.id}
                    comment={reply}
                    replies={[]}
                    onDelete={onDelete}
                    onReply={onReply}
                    onUpdate={onUpdate}
                />
            ))}
        </div>
    );
};

export default CommentItem;
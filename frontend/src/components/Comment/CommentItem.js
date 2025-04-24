import React, { useState } from 'react';
import CommentForm from './CommentForm';
import api from '../../api/axios';

const CommentItem = ({ comment, replies, onDelete, onReply, onUpdate }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [editedContent, setEditedContent] = useState(comment.content);
    const [showReplyForm, setShowReplyForm] = useState(false);

    const handleUpdate = async () => {
        try {
            const res = await api.put(`/comments/${comment.id}`, {
                postId: comment.postId,
                userId: comment.userId,
                content: editedContent,
                parentId: comment.parentId,
            });
            onUpdate(res.data);
            setIsEditing(false);
        } catch (err) {
            console.error('댓글 수정 실패:', err);
            alert('수정에 실패했습니다.');
        }
    };

    const handleDelete = async () => {
        const confirmDelete = window.confirm("댓글을 삭제하시겠습니까?");
        if (!confirmDelete) return;

        try {
            await api.delete(`/comments/${comment.id}`);
            onDelete(comment.id);
        } catch (err) {
            console.error('삭제 실패:', err);
            alert('삭제에 실패했습니다.');
        }
    };

    return (
        <div style={{ marginBottom: '10px', marginLeft: comment.parentId ? '30px' : '0' }}>
            {isEditing ? (
                <>
                    <textarea
                        value={editedContent}
                        onChange={(e) => setEditedContent(e.target.value)}
                        rows={3}
                        cols={50}
                    />
                    <br />
                    <button onClick={handleUpdate}>💾 저장</button>
                    <button onClick={() => setIsEditing(false)}>취소</button>
                </>
            ) : (
                <>
                    <div>{comment.content}</div>
                    <div style={{ fontSize: '12px', color: 'gray' }}>
                        작성일: {new Date(comment.createdAt).toLocaleString()}
                        {comment.updatedAt && comment.updatedAt !== comment.createdAt &&
                            ` (수정됨: ${new Date(comment.updatedAt).toLocaleString()})`}
                    </div>
                    <button onClick={() => setIsEditing(true)}>✏️ 수정</button>
                    <button onClick={handleDelete}>🗑️ 삭제</button>
                    <button onClick={() => setShowReplyForm(!showReplyForm)}>
                        {showReplyForm ? '취소' : '답글 입력'}
                    </button>
                </>
            )}

            {showReplyForm && (
                <div style={{ marginTop: '5px' }}>
                    <CommentForm
                        postId={comment.postId}
                        parentId={comment.id}
                        onSubmit={onReply}
                    />
                </div>
            )}

            {/* 답글 렌더링 */}
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
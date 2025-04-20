import React, { useEffect, useState } from 'react';
import CommentForm from './CommentForm';
import CommentItem from './CommentItem';

const CommentList = ({ postId }) => {
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const initial = {
        1: [
            {
                id: 1,
                postId,
                parentId: null,
                content: '첫 번째 글 댓글입니다.',
                createdAt: new Date().toISOString(),
            },
            {
                id: 2,
                postId,
                parentId: 1,
                content: '답글입니다.',
                createdAt: new Date().toISOString(),
            },
        ],
        2: [
            {
                id: 3,
                postId: 2,
                parentId: null,
                content: '두 번째 글 댓글입니다.',
                createdAt: new Date().toISOString(),
            },
        ],
    };
    setComments(initial[postId] || []);
}, [postId]);

    const handleAddComment = (newComment) => {
        setComments((prev) => [...prev, newComment]);
    };

    const handleDeleteComment = (commentId) => {
        setComments((prev) => 
            prev.filter((c) => c.id !== commentId && c.parentId !== commentId));
    };

    const handleUpdateComment = (updatedComment) => {
        setComments((prev) =>
            prev.map((c) => (c.id === updatedComment.id ? updatedComment : c))
        );
    };

    const topLevel = comments.filter((c) => c.parentId === null);
    const getReplies = (parentId) => 
        comments.filter((c) => c.parentId === parentId);

    return (
        <div style={{ marginTop: '30px' }}>
            <h3>💬 댓글 ({comments.length}개)</h3>
            <CommentForm postId={postId} onSubmit={handleAddComment} />
            {topLevel.map((comment) => (
                <CommentItem
                    key={comment.id}
                    comment={comment}
                    replies={getReplies(comment.id)}
                    onDelete={handleDeleteComment}
                    onReply={handleAddComment}
                    onUpdate={handleUpdateComment}
                />
            ))}
        </div>
    );
};

export default CommentList;
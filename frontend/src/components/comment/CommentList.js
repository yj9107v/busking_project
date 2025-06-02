import React, { useEffect, useState } from 'react';
import CommentForm from './CommentForm';
import CommentItem from './CommentItem';
import api from '../../api/axios';

const CommentList = ({ postId }) => {
    const [comments, setComments] = useState([]);

    // 🔁 댓글 목록 불러오기 (GET /api/comments?postId=)
    useEffect(() => {
        api.get(`/comments?postId=${postId}`)
            .then((res) => setComments(res.data))
            .catch((err) => {
                console.error('댓글 불러오기 실패:', err);
                alert('댓글을 불러오지 못했습니다.');
            });
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
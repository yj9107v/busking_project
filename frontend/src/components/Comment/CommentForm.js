import React, { useState } from 'react';

const CommentForm = ({ postId, parentId = null, onSubmit }) => {
    const [content, setContent] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!content.trim()) return;

        const newComment = {
            id: Date.now(),
            postId,
            parentId,
            content,
            createdAt: new Date().toISOString(),
        };

        onSubmit(newComment);
        setContent('');
    };

    return (
        <form onSubmit={handleSubmit} style={{ marginTop: '10px' }}>
            <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder={parentId ? '답글을 입력하세요...' : '댓글을 입력하세요...'}
                rows={3}
                cols={60}
                required
            />
            <br />
            <button type="submit">{parentId ? '답글 등록' : '댓글 등록'}</button>
        </form>
    );
};

export default CommentForm;
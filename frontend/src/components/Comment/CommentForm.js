import React, { useState } from 'react';
import api from '../../api/axios';

const CommentForm = ({ postId, parentId = null, onSubmit }) => {
    const [content, setContent] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newComment = {
            postId,
            userId: 1, // TODO: 로그인 유저로 대체
            parentId,
            content,
        };

        try {
            const res = await api.post('/comments', newComment);
            onSubmit(res.data); // 부모 컴포넌트에 전달
            setContent('');
        } catch (err) {
            console.error('댓글 등록 실패:', err);
            alert('댓글 등록에 실패했습니다.');
        }
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
            <button type="submit">작성</button>
        </form>
    );
};

export default CommentForm;
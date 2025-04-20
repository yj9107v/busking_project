import React from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import CommentList from '../../components/Comment/CommentList';

function BoardPage() {
    const { id } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const post = location.state?.post;

    const handleDelete = () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if (!confirmDelete) return;
        navigate('/boards', { state: { deletePostId: Number(id) } });
    };

    if (!post) {
        return <div>게시글 데이터를 불러올 수 없습니다.</div>;
    }

    return (
        <div style={{ padding: '20px' }}>
            <h1>{post.title}</h1>
            <p>{post.content}</p>
            <p style={{ color: 'gray' }}>작성일: {new Date(post.createdAt).toLocaleString()}</p>

            <div style={{ marginTop: '20px' }}>
                <button onClick={handleDelete} style={{ marginRight: '10px', backgroundColor: 'tomato', color: 'white' }}>
                    삭제하기
                </button>
                <button onClick={() => navigate(-1)}>← 목록으로 돌아가기</button>
            </div>

            <CommentList postId={post.id} />
        </div>
    );
}

export default BoardPage;
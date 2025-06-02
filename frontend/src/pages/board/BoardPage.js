import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import CommentList from '../../components/comment/CommentList';
import api from '../../api/axios';

function BoardPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [post, setPost] = useState(null);

    // ✅ 게시글 단건 조회
    useEffect(() => {
        api.get(`/boards/${id}`)
            .then(res => setPost(res.data))
            .catch(err => {
                console.error("게시글 조회 실패:", err);
                alert("존재하지 않는 게시글입니다.");
                navigate("/boards");
            });
    }, [id, navigate]);

    // ✅ 게시글 삭제
    const handleDelete = () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if (!confirmDelete) return;

        api.delete(`/boards/${id}`)
            .then(() => {
                alert("게시글이 삭제되었습니다.");
                navigate("/boards", { state: { deletePostId: Number(id) } });
            })
            .catch(err => {
                console.error("삭제 실패:", err);
                alert("삭제에 실패했습니다.");
            });
    };

    if (!post) {
        return <div>게시글 데이터를 불러오는 중...</div>;
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
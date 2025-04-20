import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import './BoardListPage.css';

const BoardListPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [posts, setPosts] = useState([]);
    const [comments, setComments] = useState([]);

    useEffect(() => {
        // 댓글 mock 데이터
        setComments([
            { id: 1, postId: 1, parentId: null, content: '좋은 글 감사합니다!', createdAt: new Date().toISOString() },
            { id: 2, postId: 1, parentId: 1, content: '저도 동감입니다!', createdAt: new Date().toISOString() },
            { id: 3, postId: 2, parentId: null, content: '잘 읽었어요!', createdAt: new Date().toISOString() },
        ]);
    }, []);

    useEffect(() => {
        if (posts.length === 0) {
            setPosts([
                {
                    id: 1,
                    title: '첫 번째 자유 글입니다',
                    content: '자유롭게 이야기해보세요.',
                    createdAt: new Date().toISOString(),
                },
                {
                    id: 2,
                    title: '두 번째 게시글',
                    content: '댓글 기능도 준비 중입니다.',
                    createdAt: new Date().toISOString(),
                },
            ]);
        }
    }, []);

    useEffect(() => {
        const state = location.state;
        if(state?.newPost) setPosts((prev) => [...prev, state.newPost]);
        if(state?.updatedPost) {
            setPosts((prev) =>
                prev.map((post) =>
                    Number(post.id) === Number(state.updatedPost.id)
                        ? state.updatedPost
                        : post
                )
            );
        }
        if(state?.deletePostId) {
            setPosts((prev) =>
                prev.filter((post) => Number(post.id) !== Number(state.deletePostId))
            );
        }
        navigate(`/boards`, { replace: true });
    }, [location.state, navigate]);

    const getCommentCount = (postId) =>
        comments.filter((c) => c.postId === postId).length;

    return (
        <div className="board-list-container">
            <h2>📋 일반 게시판</h2>
            <Link to="/boards/new"><button>글쓰기</button></Link>
            {posts.map((post) => (
                <div key={post.id} className="board-card">
                    <div className="board-info">
                        <h3>
                            <Link
                                to={`/boards/${post.id}`}
                                state={{ post }}
                                style={{ color: 'blue', textDecoration: 'underline', cursor: 'pointer' }}
                            >
                                {post.title}
                            </Link>
                        </h3>
                        <p>작성일: {new Date(post.createdAt).toLocaleString()}</p>
                        <p>💬 댓글: {getCommentCount(post.id)}개</p>
                    </div>
                    <button 
                        onClick={() => 
                            navigate(`/boards/edit/${post.id}`, {
                                state: { post },
                            })
                        }
                    >
                        게시글 수정
                    </button>
                </div>
            ))}
        </div>
    );
};

export default BoardListPage;
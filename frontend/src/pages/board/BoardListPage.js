import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import './BoardListPage.css';
import api from '../../api/axios';

const BoardListPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [posts, setPosts] = useState([]);

    // 🔄 게시글 목록 조회 (GET /api/boards)
    useEffect(() => {
        api.get("/boards")
            .then((res) => setPosts(res.data))
            .catch((err) => {
                console.error("목록 불러오기 실패:", err);
                alert("게시글을 불러오지 못했습니다.");
            });
    }, []);

    // 🔁 상태 기반 업데이트 (등록, 수정, 삭제)
    useEffect(() => {
        const state = location.state;
        if (!state) return;

        if (state.newPost) {
            setPosts((prev) => [...prev, state.newPost]);
        }

        if (state.updatedPost) {
            setPosts((prev) =>
                prev.map((post) =>
                    Number(post.id) === Number(state.updatedPost.id)
                        ? state.updatedPost
                        : post
                )
            );
        }

        if (state.deletePostId) {
            setPosts((prev) =>
                prev.filter((post) => Number(post.id) !== Number(state.deletePostId))
            );
        }

        navigate(`/boards`, { replace: true });
    }, [location.state, navigate]);

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
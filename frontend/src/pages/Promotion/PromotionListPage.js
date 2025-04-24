import React, {useEffect, useState} from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import './PromotionListPage.css';

const PromotionListPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [posts, setPosts] = useState([]);

    // ✅ 백엔드에서 게시글 목록 불러오기
    useEffect(() => {
        api.get('http://localhost:8080/api/promotions')
            .then(res => {
                setPosts(res.data);
            })
            .catch(err => {
                console.error("게시글 목록 불러오기 실패:", err);
            });
    }, []);

    // ✅ 삭제, 수정, 추가된 게시물 반영
    useEffect(() => {
        const state = location.state;
        if(!state) return;

        if(state?.deletePostId) {
            setPosts((prev) => prev.filter((post) => Number(post.id) !== Number(state.deletePostId)));
        }

        if(state?.newPost) {
            setPosts((prev) => [...prev, state.newPost]);
        }

        if(state?.updatedPost) {
            setPosts((prev) => 
                prev.map((post) =>
                    Number(post.id) === Number(state.updatedPost.id)
                        ? state.updatedPost
                        : post
                )
            );
        }

        navigate(`/promotions`, {replace: true});
    }, [location.state, navigate]);

    return (
        <div className="promotion-list-container">
            <h2>📢 버스커 홍보 게시판</h2>
            <Link to="/promotions/new">
                <button>글쓰기</button>
            </Link>
            {posts.map((post) => (
                <div key={post.id} className="promotion-card">
                    <div className="promotion-info">
                        <h3>
                            <Link
                                to={`/promotions/${post.id}`}
                                state={{post}}
                                style={{color: 'blue', textDecoration: 'underline', cursor: 'pointer'}}
                            >
                                {post.title}
                            </Link>
                        </h3>
                        <p><strong>카테고리:</strong> {post.category}</p>
                        {/* place는 아직 API에 없으니 주석 처리 또는 추후 추가 */}
                        {/* <p><strong>장소:</strong> {post.place}</p> */}
                    </div>

                    <button 
                        onClick={() => 
                            navigate(`/promotions/edit/${post.id}`, {
                                state: {post},
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

export default PromotionListPage;
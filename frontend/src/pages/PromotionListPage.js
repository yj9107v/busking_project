import React, {useEffect, useState} from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import './PromotionListPage.css'; // 스타일 따로 관리하고 싶을 때

const {kakao} = window;

const PromotionListPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [posts, setPosts] = useState([]);

    // 최초 한 번만 mock 데이터 설정
    useEffect(() => {
        if(posts.length === 0) {
            setPosts([
                {
                    id: 1,
                    title: '홍대 버스킹',
                    content: '금요일 오후 6시 홍대 거리에서 공연합니다!',
                    category: 'MUSIC',
                    place: '서울 마포구 양화로 지하 160',
                    mediaUrl: 'https://example.com/photo1.jpg',
                },
                {
                    id: 2,
                    title: '강남 댄스 퍼포먼스',
                    content: '댄스 팀 퍼포먼스 보러오세요!',
                    category: 'DANCE',
                    place: '서울 강남구 강남대로 지하 396',
                    mediaUrl: '',
                },
            ]);
        }
    }, []);

    // 새 게시물 or 수정된 게시물 반영
    useEffect(() => {
        const state = location.state;
        console.log("현재 location.state:", state);

        if(state?.newPost) {
            setPosts((prev) => [...prev, state.newPost]);
        }

        if(state?.updatedPost) {
            console.log("받은 수정 데이터", state.updatedPost);
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

    // 미니맵 렌더링
    useEffect(() => {
        const renderMiniMaps = () => {
            posts.forEach((post) => {
                const container = document.getElementById(`map-${post.id}`);
                if(!container) return;

                const geocoder = new window.kakao.maps.services.Geocoder();

                geocoder.addressSearch(post.place, (result, status) => {
                    if(status === window.kakao.maps.services.Status.OK) {
                        const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
                        const mapOption = {
                            center: coords,
                            level: 4,
                        };

                        const map = new window.kakao.maps.Map(container, mapOption);
                        new window.kakao.maps.Marker({
                            map: map,
                            position: coords,
                        });
                    }
                });
            });
        };

        if(window.kakao && window.kakao.maps && window.kakao.maps.services && posts.length > 0) {
            // posts가 완전히 반영된 후에 미니맵 그리기
            setTimeout(renderMiniMaps, 300); // 0.3초 뒤에 렌더
        }
    }, [posts]);

    return (
        <div className="promotion-list-container">
            <h2>📢 버스커 홍보 게시판</h2>
            <Link to="/promotions/new">
                <button>글쓰기</button>
            </Link>
            {posts.map((post) => (
                <div key={post.id} className="promotion-card">
                    <div className="promotion-info">
                        <h3>{post.title}</h3>
                        <p><strong>카테고리:</strong> {post.category}</p>
                        <p>{post.content}</p>
                        <p><strong>장소:</strong> {post.place}</p>
                        {post.mediaUrl && (
                            <div>
                                <img src={post.mediaUrl} alt="미디어" style={{ maxWidth: '300px' }} />
                            </div>
                        )}
                    </div>
                    <div id={`map-${post.id}`} className="mini-map"></div>

                    {/*게시물 수정 버튼 추가*/}
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

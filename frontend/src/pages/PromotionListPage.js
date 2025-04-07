import React, {useEffect, useState} from 'react';
import { Link } from 'react-router-dom';
import './PromotionListPage.css'; // 스타일 따로 관리하고 싶을 때

const {kakao} = window;

const PromotionListPage = () => {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        // 목업 데이터
        const mockPosts = [
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
        ];
        setPosts(mockPosts);
    }, []);

    useEffect(() => {
        const interval = setInterval(() => {
            if (
                window.kakao &&
                window.kakao.maps &&
                window.kakao.maps.services
            ) {
                posts.forEach((post) => {
                    const container = document.getElementById(`map-${post.id}`);
                    if (!container) return;
    
                    const geocoder = new window.kakao.maps.services.Geocoder();
    
                    geocoder.addressSearch(post.place, (result, status) => {
                        if (status === window.kakao.maps.services.Status.OK) {
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
    
                clearInterval(interval); // ✅ 한 번만 실행되도록 clear
            }
        }, 200); // 0.2초마다 체크
    
        return () => clearInterval(interval);
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
                </div>
            ))}
        </div>
    );
};

export default PromotionListPage;
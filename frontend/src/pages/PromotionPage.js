import React, {useEffect} from 'react';
import { useParams, useLocation, Link, useNavigate } from 'react-router-dom';
import ReviewList from '../components/Review/ReviewList';

const {kakao} = window;

function PromotionPage() {
    const {id} = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const post = location.state?.post;

    useEffect(() => {
        if (!post) return;
    
        const checkKakaoLoaded = () => {
            if (!window.kakao || !window.kakao.maps || !window.kakao.maps.services) {
                console.log("⏳ Kakao Maps 아직 로드 안됨. 재시도...");
                setTimeout(checkKakaoLoaded, 100); // 0.1초 후 재시도
                return;
            }
    
            const container = document.getElementById('mini-map');
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
                        map,
                        position: coords,
                    });
                }
            });
        };
    
        checkKakaoLoaded();
    }, [post]);

    if(!post) {
        return <div>게시글 데이터를 불러올 수 없습니다.</div>;
    }

    return (
        <div>
            <h1>{post.title}</h1>
            <p><strong>카테고리:</strong> {post.category}</p>
            <p>{post.content}</p>
            <p><strong>장소:</strong> {post.place}</p>
            {post.mediaUrl && (
                <img src={post.mediaUrl} alt="미디어" style={{ maxWidth: '400px' }} />
            )}
            <div id="mini-map" style={{ width: '400px', height: '300px', marginTop: '20px' }}></div>
            <button onClick={() => navigate(-1)}>← 목록으로 돌아가기</button>
            
            <h2>리뷰</h2>
            <ReviewList postId={post.id} />
        </div>
    );
}

export default PromotionPage;

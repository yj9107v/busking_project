import React, {useEffect, useState} from 'react';
import { useParams, useLocation, Link, useNavigate } from 'react-router-dom';
import ReviewList from '../components/Review/ReviewList';

const {kakao} = window;

function PromotionPage() {
    const {id} = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const post = location.state?.post;

    const [reviews, setReviews] = useState([]);

    const handleDelete = () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if(!confirmDelete) return;

        // 목록 페이지로 이동 + 삭제할 게시글 정보 전달
        navigate('/promotions', {
            state: {deletePostId: Number(id)}
        });
    }

    const calculateAverageRating = (reviews) => {
        if(reviews.length === 0) return null;
        const sum = reviews.reduce((acc, r) => acc + r.rating, 0);
        const avg = sum / reviews.length;
        return{avg: avg.toFixed(1), count: reviews.length};
    }

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

    const rating = calculateAverageRating(reviews);

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
            <div style={{marginTop: '20px'}}>
                <button onClick={handleDelete} style={{marginRight: '10px', backgroundColor: 'tomato', color: 'white'}}>
                    삭제하기
                </button>
                <button onClick={() => navigate(-1)}>← 목록으로 돌아가기</button>
            </div>
            <h2>⭐ 리뷰</h2>
            {rating? (
                <p>⭐ 평균 별점: {rating.avg}점 ({rating.count}개 리뷰)</p>
            ) : (
                <p>⭐ 리뷰 없음</p>
            )}
            <ReviewList postId={post.id} onReviewsLoaded={setReviews}/>
        </div>
    );
}

export default PromotionPage;

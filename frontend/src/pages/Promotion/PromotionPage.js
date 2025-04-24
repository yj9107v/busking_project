import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReviewList from '../../components/Review/ReviewList';
import api from '../../api/axios';

function PromotionPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [post, setPost] = useState(null);
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        api.get(`/promotions/${id}`)
            .then(res => setPost(res.data))
            .catch(err => {
                alert("존재하지 않는 게시글입니다.");
                navigate("/promotions");
            });
    }, [id, navigate]);

    const handleDelete = () => {
        const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
        if (!confirmDelete) return;

        api.delete(`/promotions/${id}`)
            .then(() => {
                alert("게시글이 삭제되었습니다.");
                navigate("/promotions", { state: { deletePostId: Number(id) } });
            })
            .catch(err => {
                alert("삭제에 실패했습니다.");
            });
    };

    const calculateAverageRating = (reviews) => {
        if (reviews.length === 0) return null;
        const sum = reviews.reduce((acc, r) => acc + r.rating, 0);
        const avg = sum / reviews.length;
        return { avg: avg.toFixed(1), count: reviews.length };
    };

    useEffect(() => {
        if (!post) return;

        const checkKakaoLoaded = () => {
            if (!window.kakao || !window.kakao.maps || !window.kakao.maps.services) {
                setTimeout(checkKakaoLoaded, 100);
                return;
            }

            const container = document.getElementById('mini-map');
            const geocoder = new window.kakao.maps.services.Geocoder();

            geocoder.addressSearch(post.place, (result, status) => {
                if (status === window.kakao.maps.services.Status.OK) {
                    const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
                    const map = new window.kakao.maps.Map(container, {
                        center: coords,
                        level: 4,
                    });
                    new window.kakao.maps.Marker({ map, position: coords });
                }
            });
        };

        checkKakaoLoaded();
    }, [post]);

    const rating = calculateAverageRating(reviews);

    if (!post) return <div>게시글 데이터를 불러오는 중...</div>;

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
            <div style={{ marginTop: '20px' }}>
                <button onClick={handleDelete} style={{ marginRight: '10px', backgroundColor: 'tomato', color: 'white' }}>
                    삭제하기
                </button>
                <button onClick={() => navigate(-1)}>← 목록으로 돌아가기</button>
            </div>
            <h2>⭐ 리뷰</h2>
            {rating ? (
                <p>⭐ 평균 별점: {rating.avg}점 ({rating.count}개 리뷰)</p>
            ) : (
                <p>⭐ 리뷰 없음</p>
            )}
            <ReviewList postId={post.id} onReviewsLoaded={setReviews} />
        </div>
    );
}

export default PromotionPage;
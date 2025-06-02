import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReviewList from '../../components/review/ReviewList';
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
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

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
    return {
      avg: (sum / reviews.length).toFixed(1),
      count: reviews.length,
    };
  };

  // ✅ Kakao Map 안전하게 로드하고 표시
  useEffect(() => {
    if (!post || !post.place) {
      console.warn("🟡 post or post.place 가 없습니다:", post);
      return;
    }
  
    const loadMap = () => {
      const container = document.getElementById('mini-map');
      if (!container) {
        console.error("❌ #mini-map 요소를 찾을 수 없습니다.");
        return;
      }
  
      console.log("📦 카카오 지도 로딩 시작");
  
      const map = new window.kakao.maps.Map(container, {
        center: new window.kakao.maps.LatLng(37.5665, 126.9780),
        level: 4,
      });
  
      const geocoder = new window.kakao.maps.services.Geocoder();
      geocoder.addressSearch(post.place, (result, status) => {
        console.log("🔍 주소 검색 결과:", result);
  
        if (status === window.kakao.maps.services.Status.OK) {
          const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
          map.setCenter(coords);
          new window.kakao.maps.Marker({
            map,
            position: coords,
            title: post.title,
          });
  
          console.log("✅ 마커 등록 완료");
        } else {
          console.error("❌ 주소 검색 실패. Status:", status);
        }
      });
    };
  
    // SDK 로드
    if (!window.kakao || !window.kakao.maps) {
      console.log("🔄 카카오 SDK 스크립트 로딩 중...");
      const script = document.createElement("script");
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=456f3d129e59884c2502dbbcdc8d8c9a&autoload=false&libraries=services`;
      script.async = true;
      script.onload = () => {
        console.log("✅ 카카오 SDK 로드 완료");
        window.kakao.maps.load(loadMap);
      };
      document.head.appendChild(script);
    } else {
      console.log("⚡ 이미 로드된 SDK 사용");
      window.kakao.maps.load(loadMap);
    }
  }, [post]);

  const rating = calculateAverageRating(reviews);

  if (!post) return <div>게시글 데이터를 불러오는 중...</div>;

  return (
    <div>
      <h1>{post.title}</h1>
      <p><strong>카테고리:</strong> {post.category}</p>
      <p>{post.content}</p>
      <p><strong>장소:</strong> {post.place}</p>

    <div id="mini-map" style={{ width: "100%", maxWidth: "500px", height: "300px", marginTop: "10px", border: "1px solid #ccc" }}></div>

    {post.mediaUrl && (
        <img src={post.mediaUrl} alt="미디어" style={{ maxWidth: '400px' }} />
      )}

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
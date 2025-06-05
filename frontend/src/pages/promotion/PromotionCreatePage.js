import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUser } from "../../components/UserContext"; // ✅ UserContext 사용
import KakaoMap from "../../components/KakaoMap";       // ✅ 지도 컴포넌트 import
import api from "../../api/axios";

const PromotionReservationPage = () => {
  const { user, isLoading } = useUser();                // ✅ 사용자 정보 및 로딩 상태
  const navigate = useNavigate();

  // ✅ 홍보글 + 장소 + 일정 통합 폼 데이터
  const [form, setForm] = useState({
    title: '',
    content: '',
    category: 'MUSIC',
    mediaUrl: '',
    place: '',
    date: '',
    startTime: '',
    endTime: '',
    description: '',
    locationId: '',
  });

  const [locationOptions, setLocationOptions] = useState([]);     // ✅ 등록된 장소 목록
  const [selectedLocation, setSelectedLocation] = useState(null); // ✅ 지도에 표시할 장소

  // ✅ 장소 목록 불러오기
  useEffect(() => {
    fetch("/api/locations")
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setLocationOptions(data);
        } else {
          console.error("❌ 장소 목록이 배열이 아님:", data);
          setLocationOptions([]); // fallback to empty array
        }
      })
      .catch((err) => {
        console.error("장소 목록 조회 실패", err);
        setLocationOptions([]); // fallback to avoid undefined
      });
  }, []);

  // ✅ 입력값 상태 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  // ✅ 장소 선택 핸들러 (드롭다운에서 선택)
  const handleLocationSelect = (e) => {
    const id = e.target.value;
    setForm((prev) => ({ ...prev, locationId: id }));

    const location = locationOptions.find((loc) => String(loc.id) === id);
    if (location) setSelectedLocation(location);
  };

  // ✅ 등록 요청 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!user) {
      alert("로그인 후 이용해주세요.");
      return;
    }

    try {
      // 1. 장소 등록
      const locationRes = await api.post("/locations", {
        name: form.place,
        latitude: 0, // (실제 좌표값 필요시 KakaoMap에서 받아올 것)
        longitude: 0,
        region: '서울',
        description: form.place,
      });

      const locationId = locationRes.data.id;
      console.log('✅ 장소 등록 완료:', locationId);

      // 2. 홍보글 등록
      const promotionRes = await api.post("/promotions", {
        title: form.title,
        content: form.content,
        category: form.category,
        mediaUrl: form.mediaUrl,
        place: form.place,
      });

      const promotionId = promotionRes.data.id;
      console.log('✅ 홍보글 등록 완료:', promotionId);

      // 3. 버스킹 일정 등록
      await api.post("/busking", {
        userId: user.id,
        locationId: locationId,
        date: form.date,
        startTime: form.startTime,
        endTime: form.endTime,
        description: form.description,
      });

      console.log('✅ 일정 등록 완료');

      alert("홍보글 + 장소 + 일정 등록 성공!");
      navigate("/promotions");

    } catch (error) {
      console.error("❌ 등록 중 오류 발생:", error);
      alert("등록에 실패했습니다.");
    }
  };

  // ✅ 로그인 확인
  if (isLoading) return <div>로딩 중입니다...</div>;
  if (!user) return <div>로그인이 필요합니다.</div>;

  return (
    <div style={{ padding: "20px", maxWidth: "800px", margin: "0 auto" }}>
      <h2>📝 버스커 홍보글 + 예약 등록</h2>

      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
        
        {/* 홍보글 작성 */}
        <input name="title" placeholder="제목" value={form.title} onChange={handleChange} required />
        <textarea name="content" placeholder="내용" value={form.content} onChange={handleChange} rows="5" required />
        
        <select name="category" value={form.category} onChange={handleChange}>
          <option value="MUSIC">음악</option>
          <option value="DANCE">댄스</option>
          <option value="ART">예술</option>
          <option value="TALK">토크</option>
        </select>

        <input name="place" placeholder="장소(도로명주소)" value={form.place} onChange={handleChange} required />
        <input name="mediaUrl" placeholder="사진/영상 URL" value={form.mediaUrl} onChange={handleChange} />

        {/* ✅ 선택된 장소 드롭다운 (기 등록된 장소 선택용) */}
        <select
          name="locationId"
          value={form.locationId}
          onChange={handleLocationSelect}
          required
        >
          <option value="">-- 장소 선택 --</option>
          {locationOptions.map((loc) => (
            <option key={loc.id} value={loc.id}>
              {loc.name}
            </option>
          ))}
        </select>

        <hr />

        {/* 일정 등록 */}
        <h3>🎤 버스킹 일정 등록</h3>

        <input type="date" name="date" value={form.date} onChange={handleChange} required />
        <input type="time" name="startTime" value={form.startTime} onChange={handleChange} required />
        <input type="time" name="endTime" value={form.endTime} onChange={handleChange} required />
        <textarea name="description" placeholder="공연 소개" value={form.description} onChange={handleChange} rows="3" />

        {/* 제출 버튼 */}
        <button type="submit" style={{ marginTop: "20px" }}>등록하기</button>
      </form>

      {/* 카카오 맵 표시 */}
      <div style={{ marginTop: "40px" }}>
        <KakaoMap
          selectedLocation={selectedLocation}
          setSelectedLocation={setSelectedLocation}
          locationOptions={locationOptions}
          setLocationOptions={setLocationOptions}
        />
      </div>
    </div>
  );
};

export default PromotionReservationPage;
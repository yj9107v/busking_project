import React, { useEffect, useState } from "react"; // React 및 훅들 import
import { useUser } from "./UserContext";            // 로그인 사용자 정보를 가져오는 커스텀 훅
import KakaoMap from "./KakaoMap";                  // 카카오 맵 컴포넌트 import

// ✅ 버스킹 일정 등록 컴포넌트 정의
function BuskingManager() {
  const { user, isLoading } = useUser(); // 사용자 정보와 로딩 상태 가져오기

  // 📦 폼 입력 상태 초기값 설정
  const [form, setForm] = useState({
    locationId: "",     // 선택된 장소 ID
    date: "",           // 공연 날짜
    startTime: "",      // 시작 시간
    endTime: "",        // 종료 시간
    description: "",    // 공연 설명
  });

  const [locationOptions, setLocationOptions] = useState([]);     // 장소 선택 드롭다운용 옵션 목록
  const [selectedLocation, setSelectedLocation] = useState(null); // 지도에 표시할 선택된 장소

    // ✅ 컴포넌트 마운트 시 장소 목록 불러오기
    useEffect(() => {
      fetch("/api/locations")
        .then((res) => res.json())
        .then(setLocationOptions) // 장소 목록 상태에 저장
        .catch((err) => console.error("장소 목록 조회 실패", err));
    }, []);

      // ✅ 장소 선택 시 실행되는 핸들러
  const handleLocationSelect = (e) => {
    const id = e.target.value;
    setForm((prev) => ({ ...prev, locationId: id })); // 폼 상태에 반영

    const location = locationOptions.find((loc) => String(loc.id) === id); // 선택된 장소 객체 찾기
    if (location) setSelectedLocation(location); // 지도에 표시할 장소 설정
  };

    // ✅ 날짜/시간/소개 입력 필드 변경 시 처리
    const handleChange = (e) => {
      setForm((prev) => ({
        ...prev,
        [e.target.name]: e.target.value,
      }));
    };

      // ✅ 폼 제출 시 실행되는 함수 (일정 등록 요청)
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!user) return alert("로그인 후 등록해주세요."); // 인증 확인

    const payload = {
      userId: user.id,
      locationId: Number(form.locationId),
      date: form.date,
      startTime: form.startTime,
      endTime: form.endTime,
      description: form.description,
    };

    if (form.startTime >= form.endTime) {
      return alert("시작 시간은 종료 시간보다 빨라야 합니다.");
    }

    const res = await fetch("/api/busking", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(payload),
    });

    if (res.status === 400) {
      const err = await res.json();
      alert("⚠ " + err.error); // 유효성 검사 실패 시
    } else if (res.ok) {
      alert("✅ 등록 성공");

      // 폼 초기화
      setForm({
        locationId: "",
        date: "",
        startTime: "",
        endTime: "",
        description: "",
      });
      setSelectedLocation(null);
    } else {
      alert("❌ 서버 오류");
    }
  };

    // ✅ 화면 렌더링
    return (
      <div>
        <h3>📝 버스킹 일정 등록</h3>
  
        <form onSubmit={handleSubmit} style={{ maxWidth: "400px", marginBottom: "20px" }}>
          {/* 장소 선택 */}
          <label>
            장소 선택:
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
          </label>
  
          {/* 날짜 입력 */}
          <input
            type="date"
            name="date"
            value={form.date}
            onChange={handleChange}
            required
          />
  
          {/* 시작 시간 입력 */}
          <input
            type="time"
            name="startTime"
            value={form.startTime}
            onChange={handleChange}
            required
          />
  
          {/* 종료 시간 입력 */}
          <input
            type="time"
            name="endTime"
            value={form.endTime}
            onChange={handleChange}
            required
          />
  
          {/* 공연 설명 */}
          <textarea
            name="description"
            placeholder="공연 소개"
            value={form.description}
            onChange={handleChange}
          />
  
          {/* 제출 버튼 */}
          <button type="submit">등록</button>
        </form>
  
        {/* 지도 컴포넌트 */}
        <KakaoMap
          selectedLocation={selectedLocation}             // 현재 선택된 장소
          setSelectedLocation={setSelectedLocation}       // 지도에서 장소 선택 시 상태 반영
          locationOptions={locationOptions}               // 장소 전체 목록
          setLocationOptions={setLocationOptions}         // 장소 목록 업데이트 함수
        />
      </div>
    );
  }
  
  export default BuskingManager; // 컴포넌트 외부로 내보내기
  
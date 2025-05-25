import React, { useState } from "react"; // React와 useState 훅 import

function BuskingListByDate() {
  // 📅 사용자가 선택한 날짜를 저장하는 상태
  const [date, setDate] = useState("");

  // 📋 조회된 버스킹 일정 리스트 상태
  const [schedules, setSchedules] = useState([]);

  // 🔍 날짜를 기반으로 서버에서 버스킹 일정을 조회하는 비동기 함수
  const fetchSchedules = async () => {
    // GET 요청 전송 (쿠키 포함)
    const res = await fetch(`http://localhost:8080/api/busking/date/${date}`, {
      credentials: "include" // 세션 기반 인증 사용 시 필요
    });

    // ✅ 응답 성공 시 일정 리스트 상태에 저장
    if (res.ok) {
      const data = await res.json();
      setSchedules(data);
    } else {
      // ❌ 실패 시 경고 출력
      alert("❌ 조회 실패");
    }
  };

  // ✅ 컴포넌트 렌더링
  return (
    <div style={{ maxWidth: 500, margin: "auto" }}>
      <h3>📅 날짜별 버스킹 일정 조회</h3>

      {/* 📆 날짜 선택 입력 필드 */}
      <input
        type="date"
        value={date}
        onChange={e => setDate(e.target.value)} // 날짜 상태 업데이트
      />

      {/* 🔍 조회 버튼 */}
      <button onClick={fetchSchedules}>조회</button>

      {/* 📋 일정 목록 출력 */}
      <ul>
        {schedules.map(s => (
          <li key={s.uuid}>
            {/* 🗓 날짜 및 시간, 장소 정보 표시 */}
            {s.date} | {s.startTime} ~ {s.endTime} | 장소: {s.locationId}
            <br />
            {/* ✏ 소개글 표시 (없으면 "없음") */}
            소개: {s.description || "없음"}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default BuskingListByDate; // ✅ 외부에서 사용할 수 있도록 export

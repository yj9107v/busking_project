import React, { useEffect, useRef, useState } from "react";

// ✅ 카카오 맵 API 스크립트 경로를 .env에서 불러오기
const KAKAO_MAP_SRC = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.REACT_APP_KAKAO_API_KEY}&autoload=false&libraries=services`;

const KakaoMap = () => {
  const mapRef = useRef(null);
  const markerRef = useRef(null);
  const currentMarkerRef = useRef(null);

  const [map, setMap] = useState(null);
  const [addressInput, setAddressInput] = useState("");
  const [markerName, setMarkerName] = useState("");
  const [locations, setLocations] = useState([]);
  const [selectedLocationId, setSelectedLocationId] = useState("");

  // ✅ SDK 로드 및 지도 생성
  useEffect(() => {
    const script = document.createElement("script");
    script.src = KAKAO_MAP_SRC;
    script.async = true;

    script.onload = () => {
      window.kakao.maps.load(() => {
        const center = new window.kakao.maps.LatLng(37.5665, 126.9780); // 서울 중심
        const mapInstance = new window.kakao.maps.Map(mapRef.current, {
          center,
          level: 5,
        });
        setMap(mapInstance);
        fetchLocations();
      });
    };

    document.head.appendChild(script);
    return () => document.head.removeChild(script);
  }, []);

  // ✅ 장소 목록 가져오기
  const fetchLocations = () => {
    fetch("/api/busking-schedules/locations")
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setLocations(data);
        } else {
          console.warn("장소 목록이 배열이 아닙니다.", data);
          setLocations([]);
        }
      })
      .catch((err) => {
        console.error("장소 목록 조회 실패:", err);
        setLocations([]);
      });
  };

  // ✅ 마커 추가 및 장소 저장
  const addMarkerAndSave = () => {
    if (!map || !addressInput.trim() || !markerName.trim()) {
      alert("주소와 마커 이름을 모두 입력하세요.");
      return;
    }

    const geocoder = new window.kakao.maps.services.Geocoder();
    geocoder.addressSearch(addressInput, (result, status) => {
      if (status !== window.kakao.maps.services.Status.OK) {
        return alert("주소를 찾을 수 없습니다.");
      }

      const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
      map.setCenter(coords);

      const newLocation = {
        name: markerName,
        latitude: result[0].y,
        longitude: result[0].x,
        region: "서울",
        description: result[0].address_name,
      };

      fetch("/api/locations", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newLocation),
      })
        .then((res) => {
          if (!res.ok) throw new Error("장소 저장 실패");
          return res.json();
        })
        .then((savedLocation) => {
          alert("✅ 장소가 저장되었습니다!");
          setLocations((prev) => [...prev, savedLocation]);
          setAddressInput("");
          setMarkerName("");
        })
        .catch((err) => {
          console.error("❌ 장소 저장 실패", err);
          alert("장소 저장 중 문제가 발생했습니다.");
        });
    });
  };

  // ✅ 장소 삭제
  const deleteSelectedMarker = () => {
    if (!selectedLocationId) return;
    if (!window.confirm("정말로 이 장소를 삭제하시겠습니까?")) return;

    fetch(`/api/locations/${selectedLocationId}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (!res.ok) throw new Error("삭제 실패");
        alert("✅ 장소가 삭제되었습니다!");

        if (markerRef.current) {
          markerRef.current.setMap(null);
          markerRef.current = null;
        }

        setLocations((prev) => prev.filter((loc) => String(loc.id) !== selectedLocationId));
        setSelectedLocationId("");
      })
      .catch((err) => {
        console.error("❌ 장소 삭제 실패", err);
        alert("장소 삭제 중 문제가 발생했습니다.");
      });
  };

  // ✅ 현재 위치 표시
  const showCurrentLocation = () => {
    if (!map) return;
    if (!navigator.geolocation) {
      alert("이 브라우저에서는 위치 정보가 지원되지 않습니다.");
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const latLng = new window.kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);

        map.panTo(latLng);

        if (currentMarkerRef.current) {
          currentMarkerRef.current.setMap(null);
        }

        const marker = new window.kakao.maps.Marker({
          position: latLng,
          map,
          title: "현재 위치",
        });

        const infoWindow = new window.kakao.maps.InfoWindow({
          content: `<div style="padding:5px;">📍 현재 위치</div>`,
        });
        infoWindow.open(map, marker);

        currentMarkerRef.current = marker;
      },
      (err) => {
        alert("위치 정보를 가져올 수 없습니다.");
        console.error(err);
      }
    );
  };

  // ✅ 마커 렌더링
  useEffect(() => {
    if (!map || !Array.isArray(locations)) return;

    if (markerRef.current) {
      markerRef.current.setMap(null);
    }

    locations.forEach((loc) => {
      const position = new window.kakao.maps.LatLng(loc.latitude, loc.longitude);

      const marker = new window.kakao.maps.Marker({
        position,
        map,
        title: loc.name,
      });

      const infoWindow = new window.kakao.maps.InfoWindow({
        content: `
          <div style="padding:8px; font-size:14px;">
            <strong>${loc.name}</strong><br/>
            날짜: ${loc.date || "미정"}<br/>
            시간: ${loc.startTime || "?"} ~ ${loc.endTime || "?"}
          </div>
        `,
      });

      window.kakao.maps.event.addListener(marker, "click", () => {
        infoWindow.open(map, marker);
      });

      if (String(loc.id) === selectedLocationId) {
        map.panTo(position);
        infoWindow.open(map, marker);
        markerRef.current = marker;
      }
    });
  }, [map, selectedLocationId, locations]);

    // ✅ UI 렌더링
    return (
      <div>
        <h2>🗺️ 카카오 지도 장소 등록</h2>
  
        <div style={{ marginBottom: "10px" }}>
          <input
            type="text"
            placeholder="도로명 주소 입력"
            value={addressInput}
            onChange={(e) => setAddressInput(e.target.value)}
            style={{ marginRight: "5px" }}
          />
          <input
            type="text"
            placeholder="마커 이름 입력"
            value={markerName}
            onChange={(e) => setMarkerName(e.target.value)}
            style={{ marginRight: "5px" }}
          />
          <button onClick={addMarkerAndSave}>마커 추가 및 저장</button>
          <button onClick={deleteSelectedMarker} style={{ marginLeft: "10px" }}>마커 삭제</button>
          <button onClick={showCurrentLocation} style={{ marginLeft: "10px" }}>현 위치</button>
        </div>
  
        {Array.isArray(locations) && locations.length > 0 && (
          <select onChange={(e) => setSelectedLocationId(e.target.value)} value={selectedLocationId}>
            <option value="">-- 장소를 선택하세요 --</option>
            {locations.map((loc) => (
              <option key={loc.id} value={loc.id}>
                {loc.name} ({loc.description})
              </option>
            ))}
          </select>
        )}
  
        <div
          ref={mapRef}
          style={{
            width: "100%",
            height: "400px",
            border: "1px solid black",
          }}
        ></div>
      </div>
    );
  };
  
  export default KakaoMap;
  
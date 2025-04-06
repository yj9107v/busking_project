import React, { useEffect, useState } from "react";

const MapComponent = () => {
    const [isLoaded, setIsLoaded] = useState(false);
    const [map, setMap] = useState(null);
    const [markers, setMarkers] = useState([]); // 📌 마커 목록 상태
    const [address, setAddress] = useState(""); // 📌 입력된 도로명 주소
    const [markerName, setMarkerName] = useState(""); // 📌 입력된 마커 이름
    const [myLocation, setMyLocation] = useState(null); // 📌 내 위치 마커 상태

    useEffect(() => {
        if (document.getElementById("kakao-map-script")) {
            console.log("📌 Kakao Maps API 스크립트 이미 로드됨");
            return;
        }

        console.log("📌 Kakao Maps API 스크립트 추가 중...");
        const script = document.createElement("script");
        script.id = "kakao-map-script";
        script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.REACT_APP_KAKAO_API_KEY}&libraries=services&autoload=false`;
        script.async = true;
        document.head.appendChild(script);

        script.onload = () => {
            console.log("✅ Kakao Maps API 로드 완료!");
            window.kakao.maps.load(() => {
                console.log("🎯 Kakao Maps API 완전히 로드됨!");
                setIsLoaded(true);
            });
        };
    }, []);

    useEffect(() => {
        if (!isLoaded) return;

        if (!window.kakao || !window.kakao.maps) {
            console.error("🚨 window.kakao.maps 객체를 찾을 수 없습니다!");
            return;
        }

        console.log("🗺️ 기본 지도 로드!");

        const container = document.getElementById("map");
        if (!container) {
            console.error("🚨 지도 컨테이너를 찾을 수 없습니다!");
            return;
        }

        // 📌 기본 지도 (서울 시청 기준)
        const options = {
            center: new window.kakao.maps.LatLng(37.5665, 126.9780),
            level: 3,
        };

        const newMap = new window.kakao.maps.Map(container, options);
        setMap(newMap);

        // 📌 내 위치 가져와서 지도 업데이트
        getMyLocation(newMap);
    }, [isLoaded]);

    // 📌 내 위치 가져오기 함수
    const getMyLocation = (mapInstance) => {
        if (!navigator.geolocation) {
            alert("📢 현재 위치를 가져올 수 없습니다.");
            return;
        }

        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = position.coords.latitude;
                const lng = position.coords.longitude;
                const latLng = new window.kakao.maps.LatLng(lat, lng);

                mapInstance.setCenter(latLng); // 📌 지도 중심 내 위치로 변경
                addMyLocationMarker(mapInstance, latLng);
            },
            (error) => {
                console.log("🚨 현재 위치를 가져오는 데 실패했습니다.", error);
                alert("현재 위치를 가져올 수 없습니다.");
            },
            {
                enableHighAccuracy: true,
                maximumAge: 0,
                timeout: 10000,
            }
        );
    };

    // 📌 내 위치 마커 추가 함수
    const addMyLocationMarker = (mapInstance, latLng) => {
        const marker = new window.kakao.maps.Marker({
            position: latLng,
            map: mapInstance,
            title: "내 위치",
            image: new window.kakao.maps.MarkerImage(
                "https://maps.google.com/mapfiles/ms/icons/red-dot.png",
                new window.kakao.maps.Size(32, 32),
                new window.kakao.maps.Point(16, 32)
            ),
        });

        // 📌 내 위치 인포윈도우
        const infoWindow = new window.kakao.maps.InfoWindow({
            content: `<div style="padding:5px;">📍 내 현재 위치</div>`,
        });

        infoWindow.open(mapInstance, marker);
        setMyLocation(marker);
    };

    // 📌 마커 추가 함수
    const addMarker = () => {
        if (!map) {
            alert("📢 지도가 아직 로드되지 않았습니다!");
            return;
        }

        const trimmedAddress = address.trim();
        const trimmedName = markerName.trim();

        if (!trimmedAddress || !trimmedName) {
            alert("📢 도로명 주소와 마커 이름을 입력하세요!");
            return;
        }

        const geocoder = new window.kakao.maps.services.Geocoder();
        geocoder.addressSearch(trimmedAddress, (result, status) => {
            if (status === window.kakao.maps.services.Status.OK) {
                const latLng = new window.kakao.maps.LatLng(result[0].y, result[0].x);

                // 📌 새 마커 생성
                const newMarker = new window.kakao.maps.Marker({
                    position: latLng,
                    map: map,
                    title: trimmedName,
                });

                // 📌 인포윈도우 생성
                const infoWindow = new window.kakao.maps.InfoWindow({
                    content: `<div style="padding:5px;">📍 ${trimmedName}<br>${trimmedAddress}</div>`,
                });

                infoWindow.open(map, newMarker);

                map.setCenter(latLng);

                setMarkers((prevMarkers) => [
                    ...prevMarkers, 
                    { marker: newMarker, infoWindow, name: trimmedName, address: trimmedAddress },
                ]);
                setAddress("");
                setMarkerName("");
            } else {
                alert("🚨 주소를 찾을 수 없습니다. 다시 확인해주세요.");
            }
        });
    };

    // 📌 마커 삭제 함수
    const removeMarker = (index) => {
        markers[index].marker.setMap(null); // 지도에서 마커 삭제
        markers[index].infoWindow.close(); // 인포윈도우 닫기
        
        setMarkers((prevMarkers) => prevMarkers.filter((_, i) => i !== index)); // 상태에서 제거
    };

    return (
        <div>
            <h2>🗺️ 카카오 지도 - 마커 추가</h2>

            {/* 📌 마커 추가 입력 폼 */}
            <div style={{ marginBottom: "10px" }}>
                <input
                    type="text"
                    placeholder="도로명 주소 입력"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    style={{ marginRight: "5px" }}
                />
                <input
                    type="text"
                    placeholder="마커 이름 입력"
                    value={markerName}
                    onChange={(e) => setMarkerName(e.target.value)}
                    style={{ marginRight: "5px" }}
                />
                <button onClick={addMarker}>마커 추가</button>
            </div>

            {/* 📌 지도 */}
            <div
                id="map"
                style={{
                    width: "600px",
                    height: "400px",
                    border: "1px solid black",
                    marginTop: "10px",
                }}
            ></div>

            {/* 📌 추가된 마커 목록 */}
            <ul>
                {markers.map((m, index) => (
                    <li key={index}>
                        {m.marker.getTitle()} - {m.address}{" "}
                        <button onClick={() => removeMarker(index)}>삭제</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MapComponent;
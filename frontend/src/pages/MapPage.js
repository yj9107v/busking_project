import React from 'react';
import MapComponent from '../components/Map/MapComponent';
import { Link } from 'react-router-dom';

function MapPage() {
    return (
        <div>
            <h1>🗺️ 버스킹 지도</h1>
            <MapComponent />
            <Link to="/promotion">
                <button>버스커 홍보 게시판 가기</button>
            </Link>
        </div>
    );
}

export default MapPage;
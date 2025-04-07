import React from 'react';
import { Link } from 'react-router-dom';

function PromotionPage() {
    return (
        <div>
            <h1>🎤 버스커 홍보 게시판</h1>
            <p>여기에 게시글 목록이 들어갈 예정입니다.</p>
            <Link to="/">
                <button>지도 페이지로 돌아가기</button>
            </Link>
        </div>
    );
}

export default PromotionPage;
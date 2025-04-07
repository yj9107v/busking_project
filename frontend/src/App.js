import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import MapPage from './pages/MapPage';
import PromotionListPage from './pages/PromotionListPage';
import PromotionCreatePage from './pages/PromotionCreatePage';

function App() {
    return (
        <Router>
            <nav>
                <Link to="/">지도</Link> | <Link to="/promotions">홍보 게시판</Link>
            </nav>
            <Routes>
                <Route path="/" element={<MapPage />} />
                <Route path="/promotions" element={<PromotionListPage />} />
                <Route path="/promotions/new" element={<PromotionCreatePage />} />
            </Routes>
        </Router>
    );
}

export default App;

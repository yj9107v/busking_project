import React from 'react';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import MapPage from './pages/Map/MapPage';
import PromotionListPage from './pages/Promotion/PromotionListPage';
import PromotionCreatePage from './pages/Promotion/PromotionCreatePage';
import PromotionEditPage from './pages/Promotion/PromotionEditPage';
import PromotionPage from './pages/Promotion/PromotionPage';
import BoardListPage from './pages/Board/BoardListPage';
import BoardCreatePage from './pages/Board/BoardCreatePage';
import BoardEditPage from './pages/Board/BoardEditPage';
import BoardPage from './pages/Board/BoardPage';
import Navbar from './components/Navbar/Navbar';

function App() {
    return (
        <Router>
            <nav>
                <Navbar />
            </nav>
            <Routes>
                <Route path="/" element={<MapPage />} />
                <Route path="/promotions" element={<PromotionListPage />} />
                <Route path="/promotions/new" element={<PromotionCreatePage />} />
                <Route path="/promotions/edit/:id" element={<PromotionEditPage />} />
                <Route path="/promotions/:id" element={<PromotionPage />} />
                <Route path="/boards" element={<BoardListPage />} />
                <Route path="/boards/new" element={<BoardCreatePage />} />
                <Route path="/boards/edit/:id" element={<BoardEditPage />} />
                <Route path="/boards/:id" element={<BoardPage />} />
            </Routes>
        </Router>
    );
}

export default App;
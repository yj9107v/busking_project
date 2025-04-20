import React from 'react';
import { NavLink } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
    return (
        <nav className="navbar">
            <NavLink
                to="/"
                className={({ isActive }) =>
                    isActive ? 'nav-item active' : 'nav-item'
                }
            >
                🗺 지도
            </NavLink>
            <NavLink
                to="/promotions"
                className={({ isActive }) =>
                    isActive ? 'nav-item active' : 'nav-item'
                }
            >
                📢 홍보 게시판
            </NavLink>
            <NavLink
                to="/boards"
                className={({ isActive }) =>
                    isActive ? 'nav-item active' : 'nav-item'
                }
            >
                📋 일반 게시판
            </NavLink>
        </nav>
    );
};

export default Navbar;
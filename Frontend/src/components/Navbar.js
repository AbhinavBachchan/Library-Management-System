import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <h2 className="navbar-logo">Library Management System</h2>
        <ul className="nav-links">
          <li><Link to="/admin-login" onClick={() => sessionStorage.setItem("role", "ADMIN")} className="nav-link">Admin Login</Link></li>
          <li><Link to="/librarian-login"  onClick={() => sessionStorage.setItem("role", "LIBRARIAN")}  className="nav-link">Librarian Login</Link></li>
          <li><Link to="/student-login"  onClick={() => sessionStorage.setItem("role", "STUDENT")}  className="nav-link">Student Login</Link></li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;

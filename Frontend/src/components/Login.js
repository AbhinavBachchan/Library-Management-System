import React, { useEffect, useState } from 'react';
import AuthService from '../Services/AuthService';
import { useNavigate } from 'react-router-dom';
import Navbar from './Navbar';
import './Styles/Login.css';
import './Styles/Navbar.css'

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await AuthService.login(username, password);
      const { token, role,id } = response.data;

      // Store token and role in local storage
      localStorage.setItem('token', token);
      localStorage.setItem('role', role);
      localStorage.setItem('id',id);
      // Redirect based on role
      if (role === 'ADMIN') {
        navigate('/admin/dashboard');
      } else if (role === 'LIBRARIAN') {
        navigate('/librarian');
      } else if (role === 'STUDENT') {
        navigate('/student');
      }
    } catch (error) {
      alert('Invalid credentials' + error);
    }
  };

  const handleSignup = () => {
    navigate('/signup');
  };

  return (
    <>
      {/* <Navbar /> */}
      <div className='p-4 bg-blue-400 text-3xl '>
        <h1 className='text-center'>LIBRARY MANAGEMENT SYSTEM</h1>
      </div>
      <div className="login-container ">
        <div className="login-card">
          <h2>Student Login</h2>
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <button type="submit" className="login-btn">Login</button>
          </form>
          <button onClick={handleSignup} className="signup-btn">Sign Up</button>
        </div>
      </div>
    </>
  );
}

export default Login;

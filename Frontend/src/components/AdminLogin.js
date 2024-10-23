import React from 'react';
import LoginForm from './LoginForm';
import Navbar from './Navbar';

function AdminLogin() {
  return (
  <>
  <Navbar />
  <LoginForm role="ADMIN" />
  </>)
}

export default AdminLogin;

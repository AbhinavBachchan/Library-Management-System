import React, { useState, useEffect } from 'react';
import { Link,useNavigate } from 'react-router-dom';
import StudentService from '../Services/StudentService';
import './Styles/BookList.css'; // Import the CSS file


const StudentDashboard = () => {
  const studentId = localStorage.getItem('id');
  const [books, setBooks] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const response = await StudentService.getAllBooks();
      console.log(response);
      setBooks(response.data);
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  const requestIssue = async (bookId) => {
    console.log(studentId);
    try {
      if (!studentId) {
        alert('Student not found. Please log in again.');
        return;
      }
      const response = await StudentService.requestIssueBook(studentId, bookId);
      alert(response.data.message);
    } catch (error) {
      alert('Error requesting issue: ' + error.message);
    }
  };

  const handleLogout = () => {
    // user data from localStorage
    localStorage.removeItem('token');

    // Redirect the user to the login page
    navigate('/');
  };

  const requestReturn = async (bookId) => {
    try {
      if (!studentId) {
        alert('Student not found. Please log in again.');
        return;
      }
      const response = await StudentService.requestReturnBook(studentId, bookId);
      alert(response);
    } catch (error) {
      alert('Error requesting return: ' + error.message);
    }
  };

  return (
    <>
    <nav className="navbar">
      <div className="navbar-container">
        <h2 className="navbar-logo">Student Dashboard</h2>
        <ul className="nav-links">
          <li><button onClick={handleLogout} className="signup-btn">Log Out</button></li>
        </ul>
      </div>
    </nav>
    <div className="book-list-container">
      <h2 className="book-list-title">Available Books</h2>
      {books.length > 0 ? (
        <ul>
          {books.map((book) => (
            <li className="book-item" key={book.id}>
              <span>{book.id}</span>
              <span>{book.title} (Copies: {book.availableCopies})</span>
              <div className="book-buttons">
                <button className="book-button" onClick={() => requestIssue(book.id)}>Request Issue</button>
                <button className="book-button return-button" onClick={() => requestReturn(book.id)}>Request Return</button>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p>No books available.</p>
      )}
    </div>
    </>
  );
};

export default StudentDashboard;

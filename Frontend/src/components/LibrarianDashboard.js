import React, { useEffect, useState } from 'react';
import LibrarianService from '../Services/LibrarianService';
import { useNavigate } from 'react-router-dom';
import './Styles/LibrarianDashboard.css'; // Assuming you have a separate CSS file

function LibrarianDashboard() {
  const navigate = useNavigate();
  const [issueRequests, setIssueRequests] = useState([]);
  const [returnRequests, setReturnRequests] = useState([]);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [books, setBooks] = useState([]);
  const [fineDetails, setFineDetails] = useState(null);
  const [isUpdateFormVisible, setIsUpdateFormVisible] = useState(false);
  const [currentBook, setCurrentBook] = useState({ id: '', title: '', author: '', availableCopies: 0 });
  const [newBook, setNewBook] = useState({
    title: '',
    author: '',
    available: true,
    availableCopies: ''
  });

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const issueResponse = await LibrarianService.getAllPendingIssueRequests();
        setIssueRequests(issueResponse.data);
        console.log(issueResponse.data);
        const booksResponse = await LibrarianService.getAllBooks();
        setBooks(booksResponse.data);
        const returnResponse = await LibrarianService.getAllPendingReturnRequests();
        setReturnRequests(returnResponse.data);

        console.log(books);
      } catch (error) {
        console.error('Error fetching requests:', error);
      }
    };
    fetchRequests();
  }, []);

  const approveIssueRequest = async (requestId) => {
    try {
      const response = await LibrarianService.issueBook(requestId);
      // alert(response.data.message);
    } catch (error) {
      alert('Error issuing book: ' + error.message);
    }
  };

  const approveReturnRequest = async (requestId) => {
    try {
      const response = await LibrarianService.returnBook(requestId);
      alert(response.data.message);
    } catch (error) {
      alert('Error returning book: ' + error.message);
    }
  };

  const handleAddBook = async () => {
    try {
      console.log("inside add book", JSON.stringify(newBook));

      const response = await LibrarianService.addBook(newBook);
      alert('Book added successfully!');
      setBooks([...books, response.data]);
    } catch (error) {
      alert('Error adding book: ' + error.message);
    }
  };

  const handleDeleteBook = async (bookId) => {
    try {
      await LibrarianService.deleteBook(bookId);
      alert('Book deleted successfully!');
      setBooks(books.filter(book => book.id !== bookId));
    } catch (error) {
      alert('Error deleting book: ' + error.message);
    }
  };

  const handleOpenUpdateForm = (book) => {
    setCurrentBook(book);
    setIsUpdateFormVisible(true);
  };

  const handleUpdateBook = async (bookId) => {
    try {
      const response = await LibrarianService.updateBook(bookId, currentBook); // Passing currentBook details
      alert('Book updated successfully!');
      setIsUpdateFormVisible(false); // Close the form after updating
      const updatedBooks = await LibrarianService.getAllBooks(); // Refresh the book list
      setBooks(updatedBooks.data); // Set the updated book list
    } catch (error) {
      alert('Error updating book: ' + error.message);
    }
  };

  const getFineDetails = async () => {
    try {
      const response = await LibrarianService.getFineDetails();
      setFineDetails(response.data);
    } catch (error) {
      alert('Error fetching fine details: ' + error.message);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('librarian');
    navigate('/');
  };

  return (
    <>
      <nav className="navbar">
        <div className="navbar-container">
          <h2 className="navbar-logo">Librarian Dashboard</h2>
          <ul className="nav-links">
            <li><button onClick={handleLogout} className="logout-btn">Log Out</button></li>
          </ul>
        </div>
      </nav>

      <div className="dashboard-container">
        <section className="book-management-section">
          <h2 className="section-title">Manage Books</h2>
          <div className="book-form" style={{ margin: '20px 0', textAlign: 'center' }}>
            <button
              className="add-book-btn"
              onClick={() => setIsFormVisible(true)}
              style={{
                padding: '10px 20px',
                fontSize: '16px',
                backgroundColor: '#28a745',
                color: 'white',
                border: 'none',
                borderRadius: '5px',
                cursor: 'pointer',
                transition: 'background-color 0.3s',
              }}
              onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#218838'}
              onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#28a745'}
            >
              Add Book
            </button>

            {isFormVisible && (
              <div className="form" style={{ marginTop: '20px', padding: '20px', border: '1px solid #ccc', borderRadius: '5px', boxShadow: '0 2px 5px rgba(0, 0, 0, 0.1)', backgroundColor: '#f9f9f9' }}>
                <input
                  type="text"
                  placeholder="Title"
                  value={newBook.title}
                  onChange={(e) => setNewBook({ ...newBook, title: e.target.value })}
                  style={{
                    width: '100%',
                    padding: '10px',
                    marginBottom: '10px',
                    border: '1px solid #ccc',
                    borderRadius: '5px',
                  }}
                />
                <input
                  type="text"
                  placeholder="Author"
                  value={newBook.author}
                  onChange={(e) => setNewBook({ ...newBook, author: e.target.value })}
                  style={{
                    width: '100%',
                    padding: '10px',
                    marginBottom: '10px',
                    border: '1px solid #ccc',
                    borderRadius: '5px',
                  }}
                />
                <input
                  type="number"
                  placeholder="Available Copies"
                  value={newBook.availableCopies}
                  onChange={(e) => setNewBook({ ...newBook, availableCopies: parseInt(e.target.value) })}
                  style={{
                    width: '100%',
                    padding: '10px',
                    marginBottom: '10px',
                    border: '1px solid #ccc',
                    borderRadius: '5px',
                  }}
                />
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <button
                    className="submit-book-btn"
                    onClick={handleAddBook}
                    style={{
                      padding: '10px 20px',
                      fontSize: '16px',
                      backgroundColor: '#007bff',
                      color: 'white',
                      border: 'none',
                      borderRadius: '5px',
                      cursor: 'pointer',
                      transition: 'background-color 0.3s',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#0069d9'}
                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#007bff'}
                  >
                    Submit
                  </button>
                  <button
                    className="cancel-book-btn"
                    onClick={() => setIsFormVisible(false)}
                    style={{
                      padding: '10px 20px',
                      fontSize: '16px',
                      backgroundColor: '#dc3545',
                      color: 'white',
                      border: 'none',
                      borderRadius: '5px',
                      cursor: 'pointer',
                      transition: 'background-color 0.3s',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#c82333'}
                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#dc3545'}
                  >
                    Cancel
                  </button>
                </div>
              </div>
            )}
          </div>
          {isUpdateFormVisible && (
            <div className="update-form">
              <h2>Update Book</h2>
              <input
                type="text"
                placeholder="Title"
                value={currentBook.title}
                onChange={(e) => setCurrentBook({ ...currentBook, title: e.target.value })}
              />
              <input
                type="text"
                placeholder="Author"
                value={currentBook.author}
                onChange={(e) => setCurrentBook({ ...currentBook, author: e.target.value })}
              />
              <input
                type="number"
                placeholder="Available Copies"
                value={currentBook.availableCopies}
                onChange={(e) => setCurrentBook({ ...currentBook, availableCopies: parseInt(e.target.value) })}
              />
              <button className="submit-book-btn" onClick={() => handleUpdateBook(currentBook.id)}>Submit</button>
              <button className="cancel-book-btn" onClick={() => setIsUpdateFormVisible(false)}>Cancel</button>
            </div>
          )}

          <h3 className="section-subtitle">Books List</h3>
          <ul className="books-list">
            {books.map((book) => (
              <li key={book.id} className="book-item">
                <span>{book.title} by {book.author} (Copies: {book.availableCopies})</span>
                <button className="delete-btn" onClick={() => handleDeleteBook(book.id)}>Delete</button>
                <button
                  className="update-btn"
                  onClick={() => handleOpenUpdateForm(book)} // Open form with current book details
                >
                  Update
                </button>
              </li>
            ))}
          </ul>
        </section>

        <section className="request-section">
          <h2 className="section-title">Pending Issue Requests</h2>
          <ul className="requests-list">
            {issueRequests.length > 0 ? issueRequests.map((request) => (
              <li key={request.requestId} className="request-item">
                <span>{request.bookTitle} requested by {request.studentName}</span>
                <button className="approve-btn" onClick={() => approveIssueRequest(request.requestId)}>Approve</button>
              </li>
            )) : <p>No pending issue requests</p>}
          </ul>
        </section>

        <section className="request-section">
          <h2 className="section-title">Pending Return Requests</h2>
          <ul className="requests-list">
            {returnRequests.length > 0 ? returnRequests.map((request) => (
              <li key={request.requestId} className="request-item">
                <span>{request.bookTitle} returned by {request.studentName}</span>
                <button className="approve-btn" onClick={() => approveReturnRequest(request.requestId)}>Approve</button>
              </li>
            )) : <p>No pending return requests</p>}
          </ul>
        </section>

        <section className="fine-section">
          <h2 className="section-title">Fine Details</h2>
          <button className="fine-btn" onClick={getFineDetails}>Get Fine Details</button>
          {fineDetails && (
            <div className="fine-details">
              <p>Total Fine: {fineDetails.totalFine}</p>
              {/* Add more fine details as necessary */}
            </div>
          )}
        </section>
      </div>
    </>
  );
}

export default LibrarianDashboard;

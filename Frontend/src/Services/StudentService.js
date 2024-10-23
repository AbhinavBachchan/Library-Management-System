import axios from 'axios';

const API_URL = 'http://localhost:8081/api/students';

const getToken = () => localStorage.getItem('token'); // Retrieve the token dynamically

const requestIssueBook = (studentId, bookId) => {
  const token = getToken();
  return axios.post(`${API_URL}/issuebook/${studentId}/${bookId}`, null, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
};

const requestReturnBook = (studentId, bookId) => {
  const token = getToken();
  return axios.post(`${API_URL}/returnbook/${studentId}/${bookId}`, null, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
};

const getAllBooks = () => {
  const token = getToken();
  return axios.get(`${API_URL}/books`, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
};

const StudentService = {
  requestIssueBook,
  requestReturnBook,
  getAllBooks,
};

export default StudentService;

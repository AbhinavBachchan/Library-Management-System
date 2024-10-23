import axios from 'axios';

const API_URL = 'http://localhost:8081/api/librarian';
const token = localStorage.getItem('token');

const getAllPendingIssueRequests = () => {
    return axios.get(`${API_URL}/pendingissuerequests`,{
        headers:{
            'Authorization':`Bearer ${token}`
        }
    });
};
const getAllPendingReturnRequests = () => {
    return axios.get(`${API_URL}/pendingreturnrequests`,{
        headers:{
            'Authorization':`Bearer ${token}`
        }
    });
};

const issueBook = (requestId) => {
    return axios.post(`${API_URL}/issue/${requestId}`,null,{
        headers:{
            'Authorization':`Bearer ${token}`
        }
    });
};

const returnBook = (requestId) => {
    return axios.put(`${API_URL}/return/${requestId}`,null,{
        headers:{
            'Authorization':`Bearer ${token}`
        }
    });
};

const getAllBooks = () => {
    console.log(token + "in getAllBooks");
    return axios.get(`${API_URL}/all`, {headers: {
        'Authorization': `Bearer ${token}`
      }
});
};

const addBook = (newData) => {
    return axios.post(`${API_URL}/add`, newData,{
        headers:{
            'Authorization':`Bearer ${token}`
        }
    });
};

const deleteBook = (id) => {
    return axios.delete(`${API_URL}/delete/${id}`,{
        headers:{
            'Authorization':`Bearer ${token}`
        }
    });
};

const updateBook = (id,newData) => {
    return axios.put(`${API_URL}/update/${id}`,newData,{
        headers:{
            'Authorization':`Bearer ${token}`
        }
    }
    );
};
const LibrarianService = {
    getAllPendingIssueRequests,
    getAllPendingReturnRequests,
    issueBook,
    returnBook,
    getAllBooks,
    addBook,
    deleteBook,
    updateBook
};

export default LibrarianService;

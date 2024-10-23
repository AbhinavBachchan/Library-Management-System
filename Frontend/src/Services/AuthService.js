import axios from 'axios';

const API_URL = 'http://localhost:8081/api/auth';

const login = (username, password) => {
  const response =  axios.post(`${API_URL}/authenticate`,{ username, password});
  
 console.log(response);

  return response;
};

const AuthService = {
  login
};

export default AuthService;

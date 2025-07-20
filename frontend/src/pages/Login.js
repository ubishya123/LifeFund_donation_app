import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auth } from '../firebase-config';
import { signInWithEmailAndPassword } from 'firebase/auth';
import axios from 'axios';
import { useAuth } from '../context/AuthContext'; // Import useAuth

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth(); // Get the login function from context

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const userCredential = await signInWithEmailAndPassword(auth, email, password);
      const token = await userCredential.user.getIdToken();
      
      // We need to set the header here temporarily to make the next call
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      const response = await axios.get('/api/users/me');
      const userData = response.data;
      
      // Use the context's login function to set the user state globally
      login(userData, token);

      if (userData.role === 'ADMIN') {
  navigate('/dashboard/admin');
} else if (userData.role === 'DONOR') {
  navigate('/dashboard/donor');
} else if (userData.role === 'PATIENT') {
  navigate('/dashboard/patient');
} else {
  navigate('/');
}

    } catch (err) {
      setError('Invalid email or password.');
      console.error(err);
    }
  };
  // ... return statement with form
};

export default Login;
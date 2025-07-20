import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auth } from '../firebase-config'; // Import your auth instance
import { createUserWithEmailAndPassword } from 'firebase/auth';
import axios from 'axios';

const Register = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState(''); // Added name field
  const [role, setRole] = useState('DONOR');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');
    try {
      // Step 1: Create user in Firebase
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      const user = userCredential.user;
      
      // Get the Firebase ID token
      const token = await user.getIdToken();

      // Step 2: Register user in your Spring Boot backend
      // Your backend AuthController uses the token to get the email and name
      // and your UserService creates the user in your database.
      await axios.post('/api/auth/register', { token, role });
      
      alert('Registration successful! Please log in.');
      navigate('/login');
    } catch (err) {
      setError('Registration failed. The email might already be in use.');
      console.error(err);
    }
  };

  return (
    <div className="py-20 bg-gray-50">
      <div className="container mx-auto px-6">
        <div className="bg-white rounded-lg shadow-xl p-8 max-w-md mx-auto">
          <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">Create Your Account</h2>
          <form onSubmit={handleRegister}>
            {/* Add inputs for name, email, password, and role selector */}
             <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="name">Full Name</label>
              <input type="text" id="name" value={name} onChange={(e) => setName(e.target.value)} required className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700"/>
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="email">Email</label>
              <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} required className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700"/>
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password">Password</label>
              <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} required className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700"/>
            </div>
             <div className="mb-6">
              <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="role">Register as a</label>
              <select id="role" value={role} onChange={(e) => setRole(e.target.value)} className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700">
                <option value="DONOR">Donor</option>
                <option value="PATIENT">Patient</option>
              </select>
            </div>
            {error && <p className="text-red-500 text-xs italic mb-4">{error}</p>}
            <button type="submit" className="bg-teal-500 text-white w-full py-3 rounded-md font-bold text-lg hover:bg-teal-600">Register</button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;
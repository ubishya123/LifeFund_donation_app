import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ProtectedRoute = ({ children, adminOnly = false }) => {
  const { user } = useAuth();

  if (!user) {
    // User not logged in, redirect to login page
    return <Navigate to="/login" />;
  }

  if (adminOnly && user.role !== 'ADMIN') {
    // User is not an admin, redirect to home page
    return <Navigate to="/" />;
  }

  return children;
};

export default ProtectedRoute;
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ProtectedRoute = ({ children, adminOnly = false }) => {
  const { user } = useAuth();

  if (!user) {
    // If the user is not logged in, redirect them to the login page.
    return <Navigate to="/login" />;
  }

  if (adminOnly && user.role !== 'ADMIN') {
    // If the route is for admins only and the user is not an admin,
    // redirect them to the home page.
    return <Navigate to="/" />;
  }

  // If the user is logged in and has the correct role, show the page.
  return children;
};

export default ProtectedRoute;
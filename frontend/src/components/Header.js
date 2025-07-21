import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getDashboardPath = () => {
    if (!user) return '/';
    if (user.role === 'ADMIN') return '/dashboard/admin';
    if (user.role === 'DONOR') return '/dashboard/donor';
    if (user.role === 'PATIENT') return '/dashboard/patient';
    return '/';
  }

  return (
    <header className="bg-white shadow-md fixed w-full z-10">
      <div className="container mx-auto px-6 py-4 flex justify-between items-center">
        <Link to="/" className="text-3xl font-bold text-teal-600">LifeFund</Link>
        <nav className="space-x-6 flex items-center">
          <Link to="/patients" className="text-gray-700 hover:text-teal-600">All Fundraisers</Link>
          
          {user && user.role === 'PATIENT' && (
             <Link to="/start-fundraiser" className="bg-teal-500 text-white px-5 py-2 rounded-full font-semibold hover:bg-teal-600">
                Start a Fundraiser
              </Link>
          )}

          {user ? (
            <>
              <Link to={getDashboardPath()} className="text-gray-700 hover:text-teal-600">Dashboard</Link>
              <button onClick={handleLogout} className="bg-red-500 text-white px-5 py-2 rounded-full font-semibold hover:bg-red-600">
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="text-gray-700 hover:text-teal-600">Login</Link>
              <Link to="/register" className="bg-teal-500 text-white px-5 py-2 rounded-full font-semibold hover:bg-teal-600">
                Register
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;

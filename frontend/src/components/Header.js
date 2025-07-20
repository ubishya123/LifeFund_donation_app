import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Header = () => {
  const { user, logout } = useAuth();
  // ... other code

   const navigate = useNavigate(); // Add this

  const handleLogout = () => {
    logout();
    navigate('/login'); // Redirect to login page after logout
  };

  const getDashboardPath = () => {
    if (!user) return '/';
    if (user.role === 'ADMIN') return '/dashboard/admin';
    if (user.role === 'DONOR') return '/dashboard/donor';
    if (user.role === 'PATIENT') return '/dashboard/patient';
    return '/';
  }

  return (
    <header /* ... */ >
      <div /* ... */ >
        <Link to="/" className="text-3xl font-bold text-teal-600">LifeFund</Link>
        <nav className="space-x-6 flex items-center">
          {/* ... other links */}
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
              {/* ... Login/Register Links */}
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
import React from 'react';
// REMOVE: BrowserRouter as Router import
import { Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from './pages/Home';
import AllPatients from './pages/AllPatients';
import PatientDetail from './pages/PatientDetail';
import Donate from './pages/Donate';
import Login from './pages/Login';
import Register from './pages/Register';
import ProtectedRoute from './components/ProtectedRoute';
import DonorDashboard from './pages/DonorDashboard';
import AdminDashboard from './pages/AdminDashboard';
import StartFundraiser from './pages/StartFundraiser';
import PatientDashboard from './pages/PatientDashboard';

function App() {
  return (
    // The Router is no longer here
    <AuthProvider>
      <div className="flex flex-col min-h-screen">
        <Header />
        <main className="flex-grow">
          <Routes>
            {/* All your <Route> components remain exactly the same */}
            <Route path="/" element={<Home />} />
            <Route path="/patients" element={<AllPatients />} />
            <Route path="/patient/:id" element={<PatientDetail />} />
            <Route path="/donate/:id" element={<Donate />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route 
              path="/start-fundraiser" 
              element={<ProtectedRoute><StartFundraiser /></ProtectedRoute>} 
            />
            <Route 
              path="/dashboard/donor" 
              element={<ProtectedRoute><DonorDashboard /></ProtectedRoute>} 
            />
            <Route 
              path="/dashboard/admin" 
              element={<ProtectedRoute adminOnly={true}><AdminDashboard /></ProtectedRoute>} 
            />
            <Route 
              path="/dashboard/patient" 
              element={<ProtectedRoute><PatientDashboard /></ProtectedRoute>} 
            />
          </Routes>
        </main>
        <Footer />
      </div>
    </AuthProvider>
  );
}

export default App;
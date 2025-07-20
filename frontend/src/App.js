import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import axios from 'axios';
// ... import components
import Login from './pages/Login';
import Register from './pages/Register';
import DonorDashboard from './pages/DonorDashboard';
import AdminDashboard from './pages/AdminDashboard';
import StartFundraiser from './pages/StartFundraiser';
import PatientDashboard from './pages/PatientDashboard';

axios.defaults.baseURL = '/';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="flex flex-col min-h-screen">
          <Header />
          <main className="flex-grow">
            <Routes>
              {/* ... other routes */}
              <Route 
                path="/start-fundraiser" 
                element={
                  <ProtectedRoute>
                    <StartFundraiser />
                  </ProtectedRoute>
                } 
              />
               <Route 
                path="/dashboard/patient" 
                element={
                  <ProtectedRoute>
                    <PatientDashboard />
                  </ProtectedRoute>
                } 
              />
            </Routes>
          </main>
          <Footer />
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
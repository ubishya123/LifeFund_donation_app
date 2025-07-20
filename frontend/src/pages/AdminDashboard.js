import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AdminDashboard = () => {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const response = await axios.get('/api/admin/summary');
        setSummary(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching admin summary:', error);
        setLoading(false);
      }
    };
    fetchSummary();
  }, []);

  if (loading) return <p>Loading admin dashboard...</p>;

  return (
    <div className="container mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">Admin Dashboard</h1>
      {summary && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <h2 className="text-xl font-semibold text-gray-700">Total Patients</h2>
            <p className="text-4xl font-bold text-teal-600">{summary.totalPatients}</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <h2 className="text-xl font-semibold text-gray-700">Total Donations</h2>
            <p className="text-4xl font-bold text-teal-600">{summary.totalDonations}</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-md text-center">
            <h2 className="text-xl font-semibold text-gray-700">Total Amount Raised</h2>
            <p className="text-4xl font-bold text-teal-600">₹{summary.totalAmount.toLocaleString()}</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
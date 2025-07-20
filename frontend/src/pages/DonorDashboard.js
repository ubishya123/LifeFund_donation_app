import React, { useState, useEffect } from 'react';
import axios from 'axios';

const DonorDashboard = () => {
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDonationHistory = async () => {
      try {
        // This requires an authenticated request
        const response = await axios.get('/api/donation/my-history');
        setDonations(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching donation history:', error);
        setLoading(false);
      }
    };

    fetchDonationHistory();
  }, []);

  if (loading) return <p>Loading your donation history...</p>;

  return (
    <div className="container mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">My Donations</h1>
      <div className="bg-white shadow-md rounded-lg p-6">
        {donations.length > 0 ? (
          <ul className="space-y-4">
            {donations.map(donation => (
              <li key={donation.id} className="border-b pb-2">
                <p>Amount: <span className="font-semibold">₹{donation.amount}</span></p>
                <p>Donated to: <span className="font-semibold">{donation.patient.name}</span></p>
                <p>Date: <span className="font-semibold">{new Date(donation.createdAt).toLocaleDateString()}</span></p>
              </li>
            ))}
          </ul>
        ) : (
          <p>You have not made any donations yet.</p>
        )}
      </div>
    </div>
  );
};

export default DonorDashboard;
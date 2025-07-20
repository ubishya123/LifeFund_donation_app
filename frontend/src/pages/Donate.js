import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const Donate = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [amount, setAmount] = useState('');
  const [email, setEmail] = useState('');
  const [name, setName] = useState('');
  const [error, setError] = useState('');

  const handleDonate = async (e) => {
    e.preventDefault();
    if (!amount || amount <= 0) {
      setError('Please enter a valid amount.');
      return;
    }
    setError('');

    try {
      const response = await axios.post(`/api/donation/create-order/${id}?amount=${amount}&donorEmail=${email}&donorName=${name || 'Anonymous'}`);
      console.log('Order created:', response.data);
      alert('Donation process initiated! Integrating Razorpay checkout is the next step.');
      navigate(`/patient/${id}`);
    } catch (err) {
      setError('Could not initiate donation. Please try again.');
      console.error('Error creating donation order:', err);
    }
  };

  return (
    <div className="py-20 bg-gray-50">
      <div className="container mx-auto px-6">
        <div className="bg-white rounded-lg shadow-xl p-8 max-w-lg mx-auto">
          <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">Support This Cause</h2>
          <form onSubmit={handleDonate}>
            <div className="mb-6">
              <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="amount">
                Donation Amount (₹)
              </label>
              <input
                type="number"
                id="amount"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                className="shadow appearance-none border rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                required
              />
            </div>
            <div className="mb-6">
              <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="email">
                Your Email
              </label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="shadow appearance-none border rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                required
              />
            </div>
            <div className="mb-8">
              <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="name">
                Your Name (Optional)
              </label>
              <input
                type="text"
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="shadow appearance-none border rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
            {error && <p className="text-red-500 text-xs italic mb-4">{error}</p>}
            <button
              type="submit"
              className="bg-teal-500 text-white w-full py-3 rounded-md font-bold text-lg hover:bg-teal-600 transition-all"
            >
              Proceed to Donate
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Donate;
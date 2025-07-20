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

    // In frontend/src/pages/Donate.js

const handleDonate = async (e) => {
  e.preventDefault();
  if (!amount || amount <= 0) {
    setError('Please enter a valid amount.');
    return;
  }
  setError('');

  try {
    // Step 1: Create the order on your backend
    const orderResponse = await axios.post(`/api/donation/create-order/${id}?amount=${amount}&donorEmail=${email}&donorName=${name || 'Anonymous'}`);
    const orderData = orderResponse.data;

    // Find the donation ID from the response (you'll need to check your backend response structure)
    // Assuming your backend response includes the donation object you saved
    const donationId = orderData.donationId; // You might need to adjust this key based on your backend response

    // Step 2: Configure Razorpay options
    const options = {
      key: process.env.REACT_APP_RAZORPAY_KEY_ID, // You need to add this to your .env file
      amount: orderData.amount,
      currency: orderData.currency,
      name: "LifeFund Donation",
      description: `Donation to patient ${id}`,
      order_id: orderData.id,
      handler: async function (response) {
        // Step 3: Verify the payment on your backend
        try {
          await axios.post('/api/donation/verify', {}, {
            params: {
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
              donationId: donationId
            }
          });
          alert('Donation successful!');
          navigate(`/patient/${id}`);
        } catch (verifyError) {
          setError('Payment verification failed. Please contact support.');
          console.error('Error verifying payment:', verifyError);
        }
      },
      prefill: {
        name: name,
        email: email,
      },
      theme: {
        color: '#3399cc'
      }
    };

    // Step 4: Open the Razorpay checkout modal
    const rzp = new window.Razorpay(options);
    rzp.open();

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
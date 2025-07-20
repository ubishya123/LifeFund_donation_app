import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';

const StartFundraiser = () => {
  const [formData, setFormData] = useState({
    name: '',
    diseases: '',
    hospital: '',
    adharNumber: '',
    accountNumber: '',
    ifscCode: '',
    phoneNumber: '',
    requiredAmount: '',
  });
  const [error, setError] = useState('');
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!user) {
      setError('You must be logged in to start a fundraiser.');
      return;
    }

    try {
      // The backend expects a DTO with these fields.
      // We also add the user's email.
      const patientData = {
        ...formData,
        email: user.email,
      };
      
      // The PatientService in the backend handles creating the patient record.
      await axios.post('/api/patients/add', patientData);
      
      alert('Fundraiser created successfully!');
      navigate('/dashboard/patient'); // Redirect to their new dashboard
    } catch (err) {
      setError('Failed to create fundraiser. Please check your details and try again.');
      console.error('Fundraiser creation error:', err);
    }
  };

  return (
    <div className="py-20 bg-gray-50">
      <div className="container mx-auto px-6">
        <div className="bg-white rounded-lg shadow-xl p-8 max-w-2xl mx-auto">
          <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">Start Your Fundraiser</h2>
          <p className="text-center text-gray-600 mb-8">Tell us your story and start receiving support from our community.</p>
          <form onSubmit={handleSubmit}>
            {/* Create a grid for a nice form layout */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Form fields for each property in the PatientDTO */}
              <div className="md:col-span-2">
                 <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="name">Full Name</label>
                 <input type="text" name="name" id="name" value={formData.name} onChange={handleChange} required className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-teal-500 focus:border-teal-500 block w-full p-2.5"/>
              </div>
              <div>
                 <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="diseases">Disease/Condition</label>
                 <input type="text" name="diseases" id="diseases" value={formData.diseases} onChange={handleChange} required className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-teal-500 focus:border-teal-500 block w-full p-2.5"/>
              </div>
               <div>
                 <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="hospital">Hospital Name</label>
                 <input type="text" name="hospital" id="hospital" value={formData.hospital} onChange={handleChange} required className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-teal-500 focus:border-teal-500 block w-full p-2.5"/>
              </div>
               <div>
                 <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="requiredAmount">Required Amount (₹)</label>
                 <input type="number" name="requiredAmount" id="requiredAmount" value={formData.requiredAmount} onChange={handleChange} required className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-teal-500 focus:border-teal-500 block w-full p-2.5"/>
              </div>
               <div>
                 <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="phoneNumber">Phone Number</label>
                 <input type="tel" name="phoneNumber" id="phoneNumber" value={formData.phoneNumber} onChange={handleChange} required className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-teal-500 focus:border-teal-500 block w-full p-2.5"/>
              </div>
               <div className="md:col-span-2">
                 <h3 className="text-xl font-semibold text-gray-700 mt-6 mb-4 border-t pt-6">Bank Details for Fund Transfer</h3>
              </div>
               <div>
                 <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="accountNumber">Bank Account Number</label>
                 <input type="text" name="accountNumber" id="accountNumber" value={formData.accountNumber} onChange={handleChange} required className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-teal-500 focus:border-teal-500 block w-full p-2.5"/>
              </div>
              <div>
                 <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="ifscCode">IFSC Code</label>
                 <input type="text" name="ifscCode" id="ifscCode" value={formData.ifscCode} onChange={handleChange} required className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-teal-500 focus:border-teal-500 block w-full p-2.5"/>
              </div>
            </div>

            {error && <p className="text-red-500 text-center mt-4">{error}</p>}
            
            <div className="mt-8 text-center">
              <button
                type="submit"
                className="bg-teal-500 text-white w-full md:w-auto px-12 py-3 rounded-md font-bold text-lg hover:bg-teal-600 transition-all"
              >
                Submit for Verification
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default StartFundraiser;
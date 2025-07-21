import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';

const PatientDashboard = () => {
  const { user } = useAuth();
  const [myCampaign, setMyCampaign] = useState(null);
  const [updateContent, setUpdateContent] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchMyCampaign = async () => {
      if (!user) return;
      try {
        setLoading(true);
        const response = await axios.get('/api/patients/all');
        const campaign = response.data.find(p => p.email === user.email);
        setMyCampaign(campaign);
        setLoading(false);
      } catch (error) {
        console.error("Could not fetch patient's campaign", error);
        setLoading(false);
      }
    };
    fetchMyCampaign();
  }, [user]);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleReportUpload = async (event) => {
    event.preventDefault();
    if (!selectedFile || !myCampaign) {
      setMessage('Please select a file to upload.');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      setMessage('Uploading report...');
      await axios.post(`/api/patients/${myCampaign.id}/upload-report`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setMessage('Report uploaded successfully!');
      const response = await axios.get(`/api/patients/${myCampaign.id}`);
      setMyCampaign(response.data);
    } catch (error) {
      setMessage('Error uploading report. Please try again.');
      console.error('Report upload error:', error);
    }
  };

  const handlePostUpdate = async (e) => {
    e.preventDefault();
    if (!updateContent.trim() || !myCampaign) return;
    try {
      await axios.post(`/api/patients/${myCampaign.id}/updates`, { content: updateContent });
      setMessage('Update posted successfully!');
      setUpdateContent('');
    } catch (error) {
      setMessage('Failed to post update.');
      console.error('Failed to post update', error);
    }
  };

  if (loading) return <p className="text-center p-10">Loading your dashboard...</p>;
  
  if (!myCampaign) {
    return (
      <div className="container mx-auto p-8 text-center">
        <h1 className="text-3xl font-bold mb-6">Welcome!</h1>
        <p className="text-lg text-gray-700 mb-8">You haven't created a fundraiser yet. Start one to begin receiving support.</p>
        <Link 
          to="/start-fundraiser" 
          className="bg-teal-500 text-white px-8 py-3 rounded-full font-semibold hover:bg-teal-600 transition-all"
        >
          Start a Fundraiser
        </Link>
      </div>
    );
  }

  const receivedAmount = myCampaign.receivedAmount || 0;
  const requiredAmount = myCampaign.requiredAmount || 0;
  const percentage = requiredAmount > 0 ? Math.min((receivedAmount / requiredAmount) * 100, 100) : 0;

  return (
    <div className="container mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">My Campaign Dashboard</h1>
      {message && <div className="bg-blue-100 border border-blue-400 text-blue-700 px-4 py-3 rounded relative mb-4" role="alert">{message}</div>}
      
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
         <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-center">
            <div>
              <p className="text-gray-600">Funds Raised</p>
              <p className="text-2xl font-bold">₹{receivedAmount.toLocaleString()}</p>
            </div>
            <div>
              <p className="text-gray-600">Goal</p>
              <p className="text-2xl font-bold">₹{requiredAmount.toLocaleString()}</p>
            </div>
            <div>
              <p className="text-gray-600">Progress</p>
              <p className="text-2xl font-bold">{percentage.toFixed(2)}%</p>
            </div>
          </div>
      </div>

      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h2 className="text-2xl font-bold mb-4">Upload Medical Report</h2>
        <form onSubmit={handleReportUpload}>
          <div className="flex items-center space-x-4">
            <input 
              type="file" 
              onChange={handleFileChange} 
              className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-teal-50 file:text-teal-700 hover:file:bg-teal-100"
            />
            <button 
              type="submit" 
              disabled={!selectedFile}
              className="px-6 py-2 rounded-md font-semibold text-white bg-teal-500 hover:bg-teal-600 disabled:bg-gray-400"
            >
              Upload
            </button>
          </div>
        </form>
        {myCampaign.reportFileUrl && (
          <div className="mt-4">
            <p className="text-gray-700">Current Report: 
              <a href={`/${myCampaign.reportFileUrl}`} target="_blank" rel="noopener noreferrer" className="text-teal-600 hover:underline ml-2">
                View Uploaded Report
              </a>
            </p>
          </div>
        )}
      </div>

      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-4">Post an Update</h2>
        <form onSubmit={handlePostUpdate}>
          <textarea
            value={updateContent}
            onChange={(e) => setUpdateContent(e.target.value)}
            className="w-full p-2 border rounded-md"
            rows="4"
            placeholder="Share your progress with your donors..."
          ></textarea>
          <button type="submit" className="mt-4 px-6 py-2 bg-teal-500 text-white rounded-md font-semibold hover:bg-teal-600">
            Post Update
          </button>
        </form>
      </div>
    </div>
  );
};

export default PatientDashboard;

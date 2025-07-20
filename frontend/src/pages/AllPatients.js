import React, { useState, useEffect } from 'react';
import axios from 'axios';
import PatientCard from '../components/PatientCard';

const AllPatients = () => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAllPatients = async () => {
      try {
        setLoading(true);
        const response = await axios.get('/api/patients/all');
        setPatients(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching all patients:', error);
        setLoading(false);
      }
    };
    fetchAllPatients();
  }, []);

  return (
    <div className="py-20 bg-gray-50">
      <div className="container mx-auto px-6">
        <h2 className="text-4xl font-bold text-center text-gray-800 mb-12">All Active Fundraisers</h2>
        {loading ? (
          <p className="text-center text-gray-500">Loading fundraisers...</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
            {patients.map(patient => (
              <PatientCard key={patient.id} patient={patient} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default AllPatients;
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import PatientCard from '../components/PatientCard';
import { Link } from 'react-router-dom';

const Home = () => {
  const [featuredPatients, setFeaturedPatients] = useState([]);

  useEffect(() => {
    const fetchFeaturedPatients = async () => {
      try {
        const response = await axios.get('/api/patients/featured');
        setFeaturedPatients(response.data);
      } catch (error) {
        console.error('Error fetching featured patients:', error);
      }
    };
    fetchFeaturedPatients();
  }, []);

  return (
    <div className="bg-gray-50">
      <section className="bg-teal-600 text-white py-24">
        <div className="container mx-auto px-6 text-center">
          <h1 className="text-5xl font-extrabold mb-4 leading-tight">Your Compassion Can Ignite Hope</h1>
          <p className="text-xl mb-8 text-teal-100">Join a community of givers providing critical medical funding for those in need.</p>
          <Link
            to="/patients"
            className="bg-white text-teal-600 px-8 py-4 rounded-full font-bold text-lg hover:bg-gray-200 transition-all"
          >
            Explore Fundraisers
          </Link>
        </div>
      </section>

      <section className="py-20">
        <div className="container mx-auto px-6">
          <h2 className="text-4xl font-bold text-center text-gray-800 mb-12">Urgent Campaigns</h2>
          {featuredPatients.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
              {featuredPatients.map(patient => (
                <PatientCard key={patient.id} patient={patient} />
              ))}
            </div>
          ) : (
            <p className="text-center text-gray-500">Loading featured campaigns...</p>
          )}
        </div>
      </section>
    </div>
  );
};

export default Home;
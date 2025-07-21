import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';

const PatientDetail = () => {
  const { id } = useParams();
  const [patient, setPatient] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPatientData = async () => {
      try {
        setLoading(true);
        const patientRes = await axios.get(`/api/patients/${id}`);
        setPatient(patientRes.data);

        const commentsRes = await axios.get(`/api/patients/${id}/comments`);
        setComments(commentsRes.data);

        setLoading(false);
      } catch (error) {
        console.error('Error fetching patient data:', error);
        setLoading(false);
      }
    };
    fetchPatientData();
  }, [id]);

  if (loading) return <div className="text-center py-10">Loading patient details...</div>;
  if (!patient) return <div className="text-center py-10 text-red-500">Could not find patient data.</div>;

  const receivedAmount = patient.receivedAmount || 0;
  const requiredAmount = patient.requiredAmount || 0;
  const percentage = requiredAmount > 0 ? Math.min((receivedAmount / requiredAmount) * 100, 100) : 0;

  return (
    <div className="bg-gray-50 py-20">
      <div className="container mx-auto px-6">
        <div className="bg-white rounded-lg shadow-xl p-8 max-w-4xl mx-auto">
          <h2 className="text-4xl font-bold text-gray-800 mb-2">{patient.name}</h2>
          <p className="text-xl text-gray-600 mb-4">Diagnosis: {patient.disease}</p>
          <p className="text-lg text-gray-500 mb-6">Receiving treatment at: {patient.hospital}</p>

           <div className="mb-8">
            <div className="w-full bg-gray-200 rounded-full h-4 dark:bg-gray-700">
              <div className="bg-green-500 h-4 rounded-full" style={{ width: `${percentage}%` }}></div>
            </div>
            <div className="flex justify-between text-lg text-gray-700 mt-3">
              <span className="font-bold">Raised: ₹{receivedAmount.toLocaleString()}</span>
              <span className="font-bold">Goal: ₹{requiredAmount.toLocaleString()}</span>
            </div>
          </div>

          <div className="flex flex-col md:flex-row items-center justify-center gap-4 mb-8">
            <Link
              to={`/donate/${patient.id}`}
              className="bg-green-500 text-white text-center w-full md:w-auto px-10 py-4 rounded-full font-bold text-xl hover:bg-green-600 transition-all"
            >
              Donate Now
            </Link>
            
            {patient.reportFileUrl && (
              <a
                 href={`/${patient.reportFileUrl}`}
                 target="_blank"
                 rel="noopener noreferrer"
                 className="bg-gray-200 text-gray-800 text-center w-full md:w-auto px-10 py-4 rounded-full font-bold text-xl hover:bg-gray-300 transition-all"
                >
                View Medical Report
              </a>
            )}
          </div>

           <div className="mt-12">
            <h3 className="text-2xl font-bold text-gray-800 mb-4">Words of Support</h3>
            <div className="space-y-4">
              {comments.length > 0 ? comments.map(comment => (
                <div key={comment.id} className="bg-gray-100 p-4 rounded-lg">
                  <p className="text-gray-700">"{comment.content}"</p>
                  <p className="text-right text-sm text-gray-500 mt-2">- {comment.commenterName}</p>
                </div>
              )) : (
                <p className="text-gray-500">Be the first to leave a comment.</p>
              )}
            </div>
          </div>

        </div>
      </div>
    </div>
  );
};

export default PatientDetail;

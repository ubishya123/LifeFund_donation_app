import React from 'react';
import { Link } from 'react-router-dom';

const PatientCard = ({ patient }) => {
  const receivedAmount = patient.receivedAmount || 0;
  const requiredAmount = patient.requiredAmount || 0;
  const percentage = requiredAmount > 0 ? Math.min((receivedAmount / requiredAmount) * 100, 100) : 0;

  return (
    <div className="bg-white rounded-lg shadow-lg overflow-hidden transform hover:-translate-y-2 transition-transform duration-300 ease-in-out flex flex-col">
      <div className="p-6 flex-grow">
        <h3 className="text-2xl font-bold text-gray-800 mb-2">{patient.name}</h3>
        <p className="text-gray-600 mb-4 h-12">{patient.disease}</p>
        <div className="mb-4">
          <div className="w-full bg-gray-200 rounded-full h-2.5 dark:bg-gray-700">
            <div
              className="bg-teal-500 h-2.5 rounded-full"
              style={{ width: `${percentage}%` }}
            ></div>
          </div>
          <div className="flex justify-between text-sm text-gray-600 mt-2">
            <span className="font-semibold">Raised: ₹{receivedAmount.toLocaleString()}</span>
            <span className="font-semibold">Goal: ₹{requiredAmount.toLocaleString()}</span>
          </div>
        </div>
      </div>
      <div className="p-6 bg-gray-50">
        <Link
          to={`/patient/${patient.id}`}
          className="bg-teal-500 text-white px-4 py-3 rounded-md font-semibold hover:bg-teal-600 w-full text-center inline-block transition-all"
        >
          View & Donate
        </Link>
      </div>
    </div>
  );
};

export default PatientCard;

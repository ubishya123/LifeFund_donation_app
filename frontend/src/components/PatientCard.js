import React from 'react';
import { Link } from 'react-router-dom';

const PatientCard = ({ patient }) => {
  const percentage = Math.min((patient.receivedAmount / patient.requiredAmount) * 100, 100);

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
            <span className="font-semibold">Raised: ₹{patient.receivedAmount.toLocaleString()}</span>
            <span className="font-semibold">Goal: ₹{patient.requiredAmount.toLocaleString()}</span>
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
import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white py-10 mt-auto">
      <div className="container mx-auto px-6 text-center">
        <p>&copy; {new Date().getFullYear()} LifeFund. All Rights Reserved.</p>
        <p className="text-sm text-gray-400 mt-2">A platform for compassionate giving.</p>
      </div>
    </footer>
  );
};

export default Footer;
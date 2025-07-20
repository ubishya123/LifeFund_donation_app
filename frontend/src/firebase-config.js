import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";

// Paste your Firebase config object here
const firebaseConfig = {
  apiKey: "AIzaSyCN_0HnpFvzsAjkQUjLc1iZcRZCSMROisM",
  authDomain: "lifefund-donation-app.firebaseapp.com",
  projectId: "lifefund-donation-app",
  storageBucket: "lifefund-donation-app.firebasestorage.app",
  messagingSenderId: "590351127142",
  appId: "1:590351127142:web:0e674404f5c837429d93a7",
  measurementId: "G-3QE4SES9J3"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Authentication and get a reference to the service
export const auth = getAuth(app);
import React from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";

const Home = () => {
  return (
    <div>
        <Navbar />
      <h1>Welcome to Lazy VIT</h1>
      <Link to="/login">Go to Login</Link>
    </div>
  );
};

export default Home;

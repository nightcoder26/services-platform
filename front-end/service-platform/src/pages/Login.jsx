import React, { useState } from "react";
import "../styles/Login.scss"; // Import the CSS file
import Navbar from "../components/Navbar";
import { useDispatch } from "react-redux";
import { setTrue } from "../redux/signupSlice";

const Login = () => {
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();


  const handleLogin = () => {
    setLoading(true);
    dispatch(login());
    dispatch(setTrue(0));
    window.location.href = "http://localhost:5173/my-profile?signup=0";
  };
  const handleSignUp = () => {
    setLoading(true);
    dispatch(login());
    dispatch(setTrue(1));

    window.location.href = "http://localhost:5173/my-profile?signup=1";
    };
  return (
    <div className="login-container">
        <Navbar />
      <div className="login-card">
        <h2>Welcome to Lazy VIT</h2>
        <p>Outsource Your Campus Struggles</p>

        {loading ? (
          <button className="login-btn disabled">Logging in...</button>
        ) : (
            <div>
          <button className="login-btn" onClick={handleLogin}>
            {/* <img
              src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
              alt="Google"
              className="google-logo"
            /> */}
            Sign in with Google
          </button>
          <button className="login-btn" onClick={handleSignUp}>
            Sign up with Google
            </button>
            </div>
        )}
      </div>
    </div>
  );
};

export default Login;

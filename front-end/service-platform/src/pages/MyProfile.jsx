import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import Navbar from "../components/Navbar";

const MyProfile = () => {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const signupValue = params.get("signup"); // '0' or '1'

  useEffect(() => {
    console.log("Signup Value:", signupValue); // Check if it's received correctly
  }, [signupValue]);

  return (
    <div>
        <div className="navbar-container">
            <Navbar />
        </div>
        <div className="my-profile-container">
      <h1>My Profile</h1>
      <p>{signupValue === "1" ? "Signed Up" : "Logged In"}</p>
        </div>
    </div>
  );
};

export default MyProfile;

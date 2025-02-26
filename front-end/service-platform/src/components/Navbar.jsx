import React from "react";
import { Link } from "react-router-dom";
import { useState } from "react";
import "../styles/Navbar.scss";

const Navbar = () => {
    const [loggedIn, setLoggedIn] = useState(false);

    return (
        <div>
            <nav>
                <h1>Lazy VIT</h1>
                <ul>
                    <li><a href="/home">Home</a></li>
                   {loggedIn ? <li><a href="/my-profile">My Profile</a></li> : <>
                    <li><a href="/login">Sign Up to access</a></li>  
                   </>} 
                    <li><a href="/login">Login</a></li>
                </ul>
            </nav>
        </div>
    );
}

export default Navbar;
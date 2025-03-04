import React from "react";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { login } from "./redux/authSlice";


const Login = () => {
    // const [isLoggedIn, setIsLoggedIn] = useState(false);
    const dispatch = useDispatch();
    const handleLogin = (value) => {
     if(value){
        dispatch(login(true));
     }else{
        dispatch(logout(true));
     }
    }
    return(
        <>
            <button onClick={handleLogin(true)} >Login</button>
            <button onClick={handleLogin(false)} >Logout</button>

            {isLoggedIn ? <h1>Welcome to the platform</h1> : <h1>Please login</h1>}
        </>
    );

}

export default Login;
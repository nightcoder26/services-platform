import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import signupReducer from "./signupSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
    signup: signupReducer,
  },
});

export default store;

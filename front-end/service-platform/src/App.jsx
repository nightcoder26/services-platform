import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import MyRequests from "./pages/MyProfile";
import { Provider } from "react-redux";
import store from "./redux/store";



function App() {

  return (
    <>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/home" element={<Home />} />
          <Route path="/my-profile" element={<MyRequests />} />
        </Routes>
      </Router>
    </>
  )
}

export default App

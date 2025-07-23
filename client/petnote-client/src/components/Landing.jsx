
import { Link } from "react-router-dom"; 
import petnoteLogo from "../assets/petnote-logo-new.png";
import "../css/Landing.css"
import runningDog from "../assets/running-dog.gif"

function Landing() {
  return (
    <div className="body-container">
      <div className="content fade-in">
        <img src={petnoteLogo} alt="Petnote Logo" className="logo" />
        <h1 className="title">Welcome to Petnote!</h1>
        <p className="subtitle">We're so glad you're here.</p>
        <div className="button-group">
          <Link to="/login" className="login-btn">
            Login
          </Link>
          <Link to="/signup" className="register-btn">
            Register
          </Link>
        </div>
      </div>

      <img src={runningDog} alt="Running Dog" className="running-dog" />
    </div>
  );
}


export default Landing

import { Link, useNavigate } from "react-router-dom";
import petnotelogo from "../assets/petnote-logo-new.png";
import "../css/Logout.css"
import { useEffect } from "react";

function Logout({ setUser }) {

    const navigate = useNavigate();

    useEffect(() => {
        localStorage.removeItem("user")
        setUser(null)
    }, [navigate, setUser]);


    return (
        <div className="thank-you-page">
            <div className="thank-you-card">
                <img
                    src={petnotelogo}
                    alt="Petnote Logo"
                    className="logo"
                />
                <h1>Thank You for Using Petnote!</h1>
                <p>
                    We hope to see you again soon!
                </p>
                <Link to="/" className="home-link">Go back to home</Link>
            </div>
        </div>
    );
}

export default Logout;
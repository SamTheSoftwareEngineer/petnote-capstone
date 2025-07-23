
import { Link } from "react-router-dom";
import petnotelogo from "../assets/petnote-logo-new.png";
import "../css/Logout.css"

function Logout() {
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
                <Link to="/" className="home-link">
                    Return to Home Page
                </Link>
            </div>
        </div>
    );
}

export default Logout;
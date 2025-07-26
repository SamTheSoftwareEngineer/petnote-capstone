import { Link } from "react-router-dom";
import petnotelogo from "../assets/petnote-logo-new.png"
import Logout from "./Logout"
import "../css/Navbar.css"
function Navbar() {


    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/dashboard" className="navbar-logo">
                    <img src={petnotelogo} alt="Petnote logo" />
                </Link>
                <ul className="navbar-links">
                    <li><Link to="/mypets">My Pets</Link></li>
                    <li><Link to="/addpet">Add Pet</Link></li>
                    <li><Link to="/addactivity/:petId">Add Activity</Link></li>
                    <li><Link to="/mystats">Stats</Link></li>
                    <li><Link to="/notes">Notes</Link></li>
                    <li><Link to="/logout" className="logout-link">Logout</Link></li>
                </ul>
            </div>
        </nav>
    );
}

export default Navbar;

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import "../css/SignUpForm.css"

const SignUpForm = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [errors, setErrors] = useState([]);

  const handleSubmit = async (event) => {
    event.preventDefault();

    const response = await fetch("http://localhost:8080/api/user", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, email, password }),
    });
    if (200 <= response.status && response.status < 300) {
      const user = await response.json(); // Parse the registered user
        console.log("Registered user:", user);

        // Save user info to localStorage
        localStorage.setItem("user", JSON.stringify(user));
      navigate("/verify");
    } else {
      const errorsPayload = await response.json();
      setErrors(errorsPayload);
    }
}

    return (
      <>
        <div className="signup-form-container">
          <h1 className="signup-title">
            Welcome to the pack! <br /> Create an Account:
          </h1>
        {/* Show errors if there are any */}
        {errors.length > 0 && <ul id="errors">
                        {errors.map(error => <li key={error}>{error}</li>)}
        </ul>}

          <form onSubmit={handleSubmit} className="signup-form">
            <div className="form-group">
              <label htmlFor="username">Username</label>
              <input
                name="username"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
              {errors.username && <p className="error">{errors.username}</p>}
            </div>

            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                name="email"
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
              {errors.email && <p className="error">{errors.email}</p>}
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                name="password"
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              {errors.password && <p className="error">{errors.password}</p>}
            </div>

            <button type="submit" className="submit-button">
              Sign Up
            </button>
          </form>

          <p className="login-link">
            Already have an account? <Link to="/login">Log In</Link>
          </p>
        </div>
      </>
    );
  };

export default SignUpForm;

import { useNavigate } from "react-router-dom"
import { useState } from "react";
import "../css/LoginForm.css"

const LoginForm = ({setUser}) => {
    const navigate = useNavigate()

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState([]);

    const handleSubmit = async (event) => {
        event.preventDefault();

        const response = await fetch("http://localhost:8080/api/user/authenticate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        });
        if (200 <= response.status && response.status < 300) {
            const user = await response.json();
            setUser(user);
            localStorage.setItem("user", JSON.stringify(user));
            navigate("/dashboard");
        } else {
            const errorsPayload = await response.json();
            setErrors(errorsPayload);
        }
    }

    return (
        <div className="login-form-container">
            <h1 className="login-title">Login</h1>
            <form className="login-form" onSubmit={handleSubmit}>
                <label htmlFor="username">Username:</label>
                <input
                    type="text"
                    id="username"
                    value={username}
                    onChange={(event) => setUsername(event.target.value)}
                />
                <label htmlFor="password">Password:</label>
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                />
                <button type="submit">Login</button>
            </form>
            {errors.map((error) => (
                <p key={error}>{error}</p>
            ))}
        </div>
    )
}

export default LoginForm;
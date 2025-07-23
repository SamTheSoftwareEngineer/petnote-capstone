import { useEffect, useState, useRef } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

import "../css/VerifyPage.css";

function VerifyPage() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const [status, setStatus] = useState("loading");
  const navigate = useNavigate();

  const hasFetched = useRef(false); // prevents double-fetch

  useEffect(() => {
    if (!token || hasFetched.current) return;

    hasFetched.current = true; // set flag to prevent second fetch
    console.log("Verifying token:", token);

    fetch(`http://localhost:8080/api/user/verify?token=${token}`)
      .then((res) => {
        if (res.ok) {
          setStatus("success");
          setTimeout(() => navigate("/dashboard"), 3000);
        } else {
          setStatus("error");
        }
      })
      .catch(() => setStatus("error"));
  }, [token, navigate]);

  return (
    <div className="verify-container">
      {status === "loading" && (
        <>
          <h1>Please check your email for a verification code</h1>
          <div className="spinner"></div>
          <p>Waiting to verify...</p>
        </>
      )}
      {status === "success" && (
        <>
          <h1 className="success">✅ Email Verified!</h1>
          <p>Redirecting to dashboard...</p>
        </>
      )}
      {status === "error" && (
        <>
          <h1 className="error">❌ Verification Failed</h1>
          <p>The verification link is invalid or expired.</p>
          <button className="btn-home" onClick={() => navigate("/")}>
            Back to Home
          </button>
        </>
      )}
    </div>
  );
}

export default VerifyPage;

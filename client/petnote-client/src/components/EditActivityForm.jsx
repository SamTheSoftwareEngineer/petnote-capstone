import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

const EditActivityForm = ({ user }) => {
  const { activityId } = useParams();
  const navigate = useNavigate();

  const [activity, setActivity] = useState(null);
  const [errors, setErrors] = useState([]);

  useEffect(() => {
    fetch(`http://localhost:8080/api/activities/${activityId}`)
      .then((res) => res.json())
      .then((data) => setActivity(data))
      .catch(() => setErrors((prev) => [...prev, "Failed to load activity."]));
  }, [activityId]);

  const handleSubmit = (e) => {
    e.preventDefault();
    fetch(`http://localhost:8080/api/activities/${activityId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(activity),
    })
      .then((res) => {
        if (res.ok) {
          navigate("/dashboard");
        } else {
          res.json().then((data) =>
            setErrors([data.message || "Error updating activity."])
          );
        }
      })
      .catch(() => setErrors(["Network error while updating activity."]));
  };

  if (!activity) return <p>Loading activity...</p>;

  return (
    <div className="edit-activity-form-container">
      <h1>Edit Activity</h1>
      {errors.length > 0 && (
        <ul className="errors">
          {errors.map((err, idx) => (
            <li key={idx}>{err}</li>
          ))}
        </ul>
      )}

      <form onSubmit={handleSubmit} className="edit-activity-form">
        <div className="form-group">
          <label htmlFor="activityName">Activity Name</label>
          <input
            type="text"
            id="activityName"
            value={activity.activityName}
            onChange={(e) =>
              setActivity({ ...activity, activityName: e.target.value })
            }
          />
        </div>
        <div className="form-group">
          <label htmlFor="activityDate">Activity Date</label>
          <input
            type="datetime-local"
            id="activityDate"
            value={activity.activityDate}
            onChange={(e) =>
              setActivity({ ...activity, activityDate: e.target.value })
            }
          />
        </div>
        <button type="submit" className="submit-activity-btn">
          Save Changes
        </button>
      </form>
    </div>
  );
};

export default EditActivityForm;

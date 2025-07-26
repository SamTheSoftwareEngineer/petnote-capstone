import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

const AddActivity = () => {
  const { petId } = useParams();
  const navigate = useNavigate();

  const [pet, setPet] = useState(null);
  const [activityTypes, setActivityTypes] = useState([]);
  const [selectedActivity, setSelectedActivity] = useState("");
  const [activityDate, setActivityDate] = useState("");
  const [completed, setCompleted] = useState(false);
  const [errors, setErrors] = useState([]);

  useEffect(() => {
    // Fetch pet info
    fetch(`http://localhost:8080/api/pet/${petId}`)
      .then((res) => res.json())
      .then((data) => setPet(data))
      .catch(() => setErrors((prev) => [...prev, "Failed to load pet data."]));

    // Fetch activity enum values
    fetch(`http://localhost:8080/api/activities/types`)
      .then((res) => res.json())
      .then((data) => setActivityTypes(data))
      .catch(() => setErrors((prev) => [...prev, "Failed to load activity types."]));
  }, [petId]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const newActivity = {
      petName: pet.petName,
      userId: pet.userId,
      petId: pet.id,    
      activityDate,
      activityName: selectedActivity,
      completed,
    };

    console.log("Posting newActivity:", newActivity);


    fetch(`http://localhost:8080/api/activities`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newActivity),
    })
      .then((res) => {
        if (res.ok) {
          console.log("Activity added successfully!");
          navigate("/dashboard");
        } else {
          res.json().then((data) => setErrors([data.message || "Error adding activity."]));
        }
      })
      .catch(() => setErrors(["Network error while adding activity."]));
  };

  if (!pet) return <p>Loading pet info...</p>;

  return (
    <div className="add-activity-form-container">
      <h1>Add Activity for {pet.petName}</h1>
      {errors.length > 0 && (
        <ul className="errors">
          {errors.map((err, idx) => (
            <li key={idx}>{err}</li>
          ))}
        </ul>
      )}

      <form onSubmit={handleSubmit} className="add-activity-form">
        {/* Activity Name */}
        <div className="form-group">
          <label htmlFor="activityName">Activity Type</label>
          <select
            id="activityName"
            value={selectedActivity}
            onChange={(e) => setSelectedActivity(e.target.value)}
            required
          >
            <option value="">Select an activity</option>
            {activityTypes.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </div>

        {/* Activity Date */}
        <div className="form-group">
          <label htmlFor="activityDate">Activity Date</label>
          <input
            type="datetime-local"
            id="activityDate"
            value={activityDate}
            onChange={(e) => setActivityDate(e.target.value)}
            required
          />
        </div>

        {/* Completed Checkbox */}
        <div className="form-group">
          <label>
            <input
              type="checkbox"
              checked={completed}
              onChange={(e) => setCompleted(e.target.checked)}
            />
            Completed?
          </label>
        </div>

        <button type="submit" className="submit-activity-btn">
          Add Activity
        </button>
      </form>
    </div>
  );
};

export default AddActivity;

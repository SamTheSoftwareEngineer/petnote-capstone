import React, { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import "../css/Dashboard.css";
const Dashboard = ({ user }) => {
  const [petRows, setPetRows] = useState([]); // [{ pet, activities }]
  const [loading, setLoading] = useState(true);
  const location = useLocation();

  useEffect(() => {
    setLoading(true);
    fetch(`http://localhost:8080/api/activities/user/${user.id}/pets-with-activities`)
      .then((res) => res.json())
      .then((data) => setPetRows(data))
      .catch((err) => console.error("Failed to load pets and activities:", err))
      .finally(() => setLoading(false));
  }, [user.id, location]);

  if (loading) {
    return <p>Loading your pets and activities...</p>;
  }

  if (!petRows || petRows.length === 0) {
    return <p>You have no registered pets or activities.</p>;
  }


  const handleDeleteActivity = (activityId) => {
  if (!window.confirm("Are you sure you want to delete this activity?")) return;

  fetch(`http://localhost:8080/api/activities/${activityId}?userId=${user.id}`, {
    method: "DELETE",
  })
    .then((res) => {
      if (res.ok) {
        setPetRows((prev) =>
          prev.map((row) => ({
            ...row,
            activities: row.activities.filter((a) => a.id !== activityId),
          }))
        );
      } else {
        console.error("Failed to delete activity");
      }
    })
    .catch((err) => console.error("Error deleting activity:", err));
};


  return (
    <div className="dashboard">
      {petRows.map(({ pet, activities }) => (
        <div key={pet.id} className="pet-card">
          <div className="pet-header">
          <img
            src={pet.profilePictureURL}
            alt={pet.petName}
            className="pet-photo"
          />
          <h2>{pet.petName}</h2>
        </div>

        {activities && activities.length > 0 ? (
          <ul className="activity-list">
            {activities.map((activity) => (
              <li key={activity.id} className="activity-item">
                <span className="activity-name">
                  {activity.activityName}
                </span>
                <span className="activity-time">
                  {new Date(activity.activityDate).toLocaleString()}
                </span>
                <div className="activity-actions">
                  <Link to={`editactivity/${activity.id}`} className={`edit-activity-btn`}>
                  Edit
                  </Link>
                  <button className="delete-activity-btn" onClick={() => handleDeleteActivity(activity.id)}>
                  Delete
                  </button>
                  <Link to={`/addactivity/${pet.id}`} className="add-activity-btn">
                  Add Activity
                  </Link>
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <>
            <p className="no-activities">No activities logged for this pet.</p>
            <Link to={`/addactivity/${pet.id}`} className="add-activity-btn">
              Add Activity
            </Link>
          </>
        )}
      </div>
      ))}
    </div>
  );
};

export default Dashboard;

import { useEffect, useState } from "react";
import { getPetsByUserId } from "../services/petService";
import { getRemindersByPetId } from "../services/remindersService";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import "../css/RemindersList.css";

function RemindersList({ userId }) {
  const [pets, setPets] = useState([]);
  const [remindersMap, setRemindersMap] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchPetsAndReminders() {
      try {
        const petList = await getPetsByUserId(userId);
        setPets(petList);

        const reminders = {};
        for (const pet of petList) {
          const petReminders = await getRemindersByPetId(pet.id);
          reminders[pet.id] = petReminders;
        }
        setRemindersMap(reminders);
      } catch (err) {
        console.error("Error fetching pets or reminders:", err);
      }
    }

    fetchPetsAndReminders();
  }, [userId]);

  const handleDeleteReminder = async (reminderId) => {
    try {
      await fetch(`http://localhost:8080/api/reminders/${reminderId}`, {
        method: "DELETE",
      });
      // Refresh the reminders
      const updatedMap = { ...remindersMap };
      for (const pet of pets) {
        const petReminders = await getRemindersByPetId(pet.id);
        updatedMap[pet.id] = petReminders;
      }
      setRemindersMap(updatedMap);
    } catch (error) {
      console.error("Failed to delete reminder", error);
    }
  };

  return (
    <div className="reminders-container">
      <h2 className="reminders-heading">Your Pet Reminders</h2>
      {pets.map((pet) => (
        <div key={pet.id} className="pet-card">
          <div className="pet-header">
            <h3 className="pet-name">{pet.petName}</h3>
            <Link to={`/addreminder/${pet.id}`} className="btn add-btn">
              Add Reminder
            </Link>
          </div>

          <ul className="reminder-list">
            {(remindersMap[pet.id] || []).map((reminder) => (
              <li key={reminder.id} className="reminder-item">
                <div>
                  <p className="reminder-message">{reminder.message}</p>
                  <p className="reminder-time">
                    {new Date(reminder.remindAt).toLocaleString()}
                  </p>
                </div>
                <div className="reminder-actions">
                  <Link
                    to={`/editreminder/${reminder.id}`}
                    className="btn edit-btn"
                  >
                    Edit
                  </Link>

                  <button
                    className="btn delete-btn"
                    onClick={() => handleDeleteReminder(reminder.id)}
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}

export default RemindersList;

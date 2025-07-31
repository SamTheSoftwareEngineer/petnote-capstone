import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function AddReminderForm({ userId }) {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    message: "",
    remindAt: "",
    userId: userId || "",
    petId: "", 
  });

  const [pets, setPets] = useState([]);

  // 🐾 Fetch the pets for this user
  useEffect(() => {
    async function fetchPets() {
      try {
        const response = await fetch(`http://localhost:8080/api/pet/user/${userId}`);
        if (response.ok) {
          const data = await response.json();
          console.log(data);
          setPets(data);
        } else {
          console.error("Failed to fetch pets");
        }
      } catch (error) {
        console.error("Error fetching pets:", error);
      }
    }

    if (userId) {
      fetchPets();
    }
  }, [userId]);

  // ✏️ Handle form input changes
  function handleChange(e) {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  }

  // 📨 Submit reminder
  async function handleSubmit(e) {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/reminders", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    });

    if (response.ok) {
      navigate("/reminderlist");
    } else {
      console.error("Failed to add reminder");
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4 bg-gray-100 p-4 rounded-xl shadow max-w-md mx-auto">
      <h2 className="text-lg font-bold">Add Reminder</h2>

      <input
        type="text"
        name="message"
        placeholder="Reminder message"
        value={formData.message}
        onChange={handleChange}
        required
        className="block w-full border p-2 rounded"
      />

      <input
        type="datetime-local"
        name="remindAt"
        value={formData.remindAt}
        onChange={handleChange}
        required
        className="block w-full border p-2 rounded"
      />

      {/* 🐶 Pet dropdown */}
      <select
        name="petId"
        value={formData.petId}
        onChange={handleChange}
        required
        className="block w-full border p-2 rounded"
      >
        <option value="">Select a pet</option>
        {pets.map((pet) => (
          <option key={pet.id} value={pet.id}>
            {pet.petName}
          </option>
        ))}
      </select>

      <button type="submit" className="bg-blue-500 text-white p-2 rounded hover:bg-blue-600 w-full">
        Save Reminder
      </button>
    </form>
  );
}

export default AddReminderForm;

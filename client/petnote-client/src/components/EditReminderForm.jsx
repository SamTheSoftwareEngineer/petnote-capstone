import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect  } from "react";
function ReminderEditForm() {
  const { id } = useParams(); 
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    id: "",
    userId: "",
    message: "",
    remindAt: ""
  });

  useEffect(() => {
    fetch(`http://localhost:8080/api/reminders/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error("Reminder not found");
        return res.json();
      })
      .then((data) => {
        const formattedDate = data.remindAt?.slice(0, 16); // strip seconds + ms
        setFormData({ ...data, remindAt: formattedDate });
      })
      .catch((err) => console.error(err));
  }, [id]);

  function handleChange(e) {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  }

  function handleSubmit(e) {
    e.preventDefault();
    fetch(`http://localhost:8080/api/reminders/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Update failed");
        navigate("/reminderlist"); 
      })
      .catch((err) => console.error(err));
  }

  return (
    <div className="reminder-form">
      <h2>Edit Reminder</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Message:
          <input
            type="text"
            name="message"
            value={formData.message}
            onChange={handleChange}
            required
          />
        </label>
        <label>
          Date:
          <input
            type="datetime-local"
            name="remindAt"
            value={formData.remindAt}
            onChange={handleChange}
            required
      />
        </label>
        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
}

export default ReminderEditForm;
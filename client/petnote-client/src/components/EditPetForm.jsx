import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "../css/EditPetForm.css"; // We'll create this CSS

function EditPetForm({ user }) {
  const navigate = useNavigate();
  const { petId } = useParams();
  const [pet, setPet] = useState(null);
  const [originalPet, setOriginalPet] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPet = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/pet/${petId}`);
        if (response.ok) {
          const data = await response.json();
          if (data.userId !== user.id) {
            navigate("/NotFound");
          } else {
            setPet(data);
            setOriginalPet(data);
          }
        } else {
          navigate("/NotFound");
        }
      } catch (err) {
        console.log(err)
        setError("Failed to fetch pet.");
      } finally {
        setLoading(false);
      }
    };
    fetchPet();
  }, [petId, user.id, navigate]);

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (files) {
      setPet((prev) => ({ ...prev, [name]: files[0] }));
    } else {
      setPet((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
  e.preventDefault();

  const formData = new FormData();
  formData.append("userId", user.id)
  formData.append("petName", pet.petName || "");
  formData.append("breed", pet.breed || "");
  formData.append("species", pet.species || "");
  formData.append("age", pet.age || 0);
  formData.append("weight", pet.weight || 0);

  if (pet.profilePicture instanceof File) {
    formData.append("profilePicture", pet.profilePicture);
  }

  try {
    const response = await fetch(`http://localhost:8080/api/pet/${petId}`, {
      method: "PUT",
      body: formData,
    });

    if (response.ok) {
      navigate("/mypets");
    } else {
      setError("Failed to update pet.");
    }
  } catch (err) {
    console.log(err)
    setError("An error occurred while updating.");
  }
};


  if (loading) return <p>Loading pet details...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="edit-pet-container">
      <h1 className="edit-pet-title">Edit Pet</h1>
      <form className="edit-pet-form" onSubmit={handleSubmit}>
        <label htmlFor="petName">Pet Name:</label>
        <input
          type="text"
          name="petName"
          value={pet.petName || ""}
          onChange={handleChange}
        />

        <label htmlFor="breed">Breed:</label>
        <input
          type="text"
          name="breed"
          value={pet.breed || ""}
          onChange={handleChange}
        />

        <label htmlFor="species">Species:</label>
        <input
          type="text"
          name="species"
          value={pet.species || ""}
          onChange={handleChange}
        />

        <label htmlFor="age">Age:</label>
        <input
          type="number"
          name="age"
          value={pet.age || ""}
          onChange={handleChange}
        />

        <label htmlFor="weight">Weight (lbs):</label>
        <input
          type="number"
          name="weight"
          step="0.1"
          value={pet.weight || ""}
          onChange={handleChange}
        />

        <label htmlFor="profilePicture">Profile Picture:</label>
        <input
          type="file"
          name="profilePicture"
          accept="image/*"
          onChange={handleChange}
        />

        <button type="submit" className="btn-save">Update Pet</button>
        <button type="button" className="btn-cancel" onClick={() => navigate("/mypets")}>
          Cancel
        </button>
      </form>
    </div>
  );
}

export default EditPetForm;

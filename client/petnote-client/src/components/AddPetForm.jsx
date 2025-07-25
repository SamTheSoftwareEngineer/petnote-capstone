 import { useState } from "react";
 import { useNavigate } from "react-router-dom";
import "../css/AddPetForm.css"; 

function AddPetForm({ userId }) {
  const navigate = useNavigate();
  const [petName, setPetName] = useState("");
  const [breed, setBreed] = useState("");
  const [species, setSpecies] = useState("");
  const [age, setAge] = useState("");
  const [weight, setWeight] = useState("");
  const [profilePicture, setProfilePicture] = useState(null);
  const [message, setMessage] = useState("");

  const handleFileChange = (e) => {
    setProfilePicture(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("petName", petName);
    formData.append("breed", breed);
    formData.append("species", species);
    formData.append("age", age);
    formData.append("weight", weight);
    formData.append("userId", userId);
    if (profilePicture) {
      formData.append("profilePicture", profilePicture);
    } 

    try {
      const res = await fetch("http://localhost:8080/api/pet", {
        method: "POST",
        body: formData,
      });

      if (res.ok) {
        const data = await res.json();
        setMessage(`Pet "${data.petName}" added successfully!`);
        setPetName("");
        setBreed("");
        setSpecies("");
        setAge("");
        setWeight("");
        setProfilePicture(null);
        navigate("/mypets");
        
      } else {
        setMessage("Failed to add pet. Please try again.");
      }
    } catch (error) {
      console.error("Error adding pet:", error);
      setMessage("An error occurred. Please try again.");
    }
  };

  return (
    <div className="add-pet-container">
      <h1>Add a New Pet</h1>
      {message && <p className="message">{message}</p>}
      <form className="add-pet-form" onSubmit={handleSubmit} encType="multipart/form-data">
        <label>
          Pet Name:
          <input type="text" value={petName} onChange={(e) => setPetName(e.target.value)} required />
        </label>
        <label>
          Breed:
          <input type="text" value={breed} onChange={(e) => setBreed(e.target.value)} />
        </label>
        <label>
          Species:
          <input type="text" value={species} onChange={(e) => setSpecies(e.target.value)} />
        </label>
        <label>
          Age:
          <input type="number" value={age} onChange={(e) => setAge(e.target.value)} />
        </label>
        <label>
          Weight (lbs):
          <input type="number" value={weight} onChange={(e) => setWeight(e.target.value)} />
        </label>
        <label>
          Pet Picture:
          <input type="file" onChange={handleFileChange} accept="image/*" />
        </label>
        <button type="submit">Add Pet</button>
      </form>
    </div>
  );
}

export default AddPetForm;

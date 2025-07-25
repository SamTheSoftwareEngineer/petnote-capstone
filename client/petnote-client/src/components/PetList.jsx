import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "../css/PetList.css";
import { deletePet } from "../services/petService";
import ConfirmModal from "./ConfirmModal";


function PetList({ userId }) {
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [petToDelete, setPetToDelete] = useState(null);
 

  const handleDeleteModal = (confirm) => {
    if (confirm && petToDelete) {
      deletePet(petToDelete.id);
    }
    setPetToDelete(null);
  };


  useEffect(() => {
    const fetchPets = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/pet/user/${userId}`);
        if (!response.ok) {
          throw new Error("Failed to fetch pets");
        }
        const data = await response.json();
        setPets(data);
      } catch (error) {
        console.error("Error fetching pets:", error);
      } finally {
        setLoading(false);
      }
    };

    if (userId) fetchPets();
  }, [userId, petToDelete]);

  if (loading) {
    return <p>Loading pets...</p>;
  }

  if (pets.length === 0) {
    return (
      <div className="no-pets-message">
        <h2>You don't have any pets added yet! 🐾</h2>
        <p>Start by adding your first pet to track their care and activities.</p>
        <a className="add-pet-link" href="/addpet">Add Your First Pet</a>
      </div>
    );
  }

if (!pets || pets.length === 0) {
    return (
      <div className="no-pets-message">
        <h2>You don’t have any pets added yet.</h2>
        <p>Start by adding a pet to keep track of their activities and notes!</p>
      </div>
    );
  }

  return (
    <>
      <div className="pet-list">
        {pets.map((pet) => (
          <div key={pet.id} className="pet-card">
            <img
              src={pet.profilePictureURL || "/default-pet.png"}
              alt={pet.petName}
              className="pet-photo"
            />
            <div className="pet-info">
              <p><strong>Name:</strong> {pet.petName}</p>
              <p><strong>Species:</strong> {pet.species || "Unknown"}</p>
              <p><strong>Breed:</strong> {pet.breed || "Unknown"}</p>
              <p><strong>Age:</strong> {pet.age ?? "N/A"} years</p>
              <p><strong>Weight:</strong> {pet.weight ?? "N/A"} lbs</p>
            </div>
            <div className="pet-actions">
              <Link to={`/editpet/${pet.id}`} className="edit-pet-btn">Edit</Link>
              <button className="delete-pet-btn" onClick={() => setPetToDelete(pet)}>Delete</button>
              <div className="note-button">
                <Link to={`/notes/${pet.id}`} className="note-btn">Notes</Link>
            </div>
            </div>
            
          </div>
        ))}
      </div>

      {petToDelete && (
        <ConfirmModal
          message={`Are you sure you want to delete ${petToDelete.petName}?`}
          onConfirm={() => handleDeleteModal(true)}
          onCancel={() => handleDeleteModal(false)}
        />
      )}
    </>
  );
}

export default PetList;

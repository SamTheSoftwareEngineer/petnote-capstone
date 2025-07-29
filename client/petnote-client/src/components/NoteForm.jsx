import { use, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import "../css/NoteForm.css";
import ConfirmModal from './ConfirmModal';
function NoteForm({ user }) {
  const { petId } = useParams();
  const [notes, setNotes] = useState([]);
  const [formNote, setFormNote] = useState({ id: 0, description: "", petId: parseInt(petId), userId: user.id });
  const [pet, setPet] = useState({});
  const [noteToDelete, setNoteToDelete] = useState(null);

  // Fetch pets

  useEffect(() => {
      fetch(`http://localhost:8080/api/pet/${petId}`
      )
      .then(res => res.json())
      .then(data => setPet(data))
      .catch(err => console.error(err)
      );
  }, [petId]);


  // Fetch notes for this pet
  useEffect(() => {
    fetch(`http://localhost:8080/api/notes/pet/${petId}`)
      .then(res => res.json())
      .then(data => setNotes(data))
      .catch(err => console.error(err));
  }, [petId]);

  // Handle input change
  const handleChange = (e) => {
    setFormNote({ ...formNote, [e.target.name]: e.target.value });
  };

  // Handle form submit (create or update)
  const handleSubmit = (e) => {
    e.preventDefault();
    const method = formNote.id === 0 ? 'POST' : 'PUT';
    const url = formNote.id === 0 
      ? 'http://localhost:8080/api/notes' 
      : `http://localhost:8080/api/notes/${formNote.id}`;

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formNote)
    })
      .then(res => res.ok ? res.json() : Promise.reject('Failed to save'))
      .then(() => {
        // refetch notes
        return fetch(`http://localhost:8080/api/notes/pet/${petId}`);
      })
      .then(res => res.json())
      .then(data => {
        console.log(data);
        setNotes(data);
        setFormNote({ id: 0, description: "", petId: parseInt(petId), userId: user.id });
      });
  };

  // Edit a note
  const handleEdit = (note) => {
    setFormNote(note);
  };

 const handleDelete = (noteId) => {
    setNoteToDelete(noteId);
  };

  const confirmDelete = () => {
    fetch(`http://localhost:8080/api/notes/${noteToDelete}?userId=${user.id}`, {
      method: "DELETE",
    }).then(() => {
      setNotes(notes.filter((n) => n.id !== noteToDelete));
      setNoteToDelete(null);
    });
  };

  const cancelDelete = () => {
    setNoteToDelete(null);
  };

  return (
  <div className="note-form-container">
    <h2>Notes for {pet.petName}</h2>

    <ul className="note-list">
      { notes.length === 0 && <p>No notes yet.</p>}
      {notes.map(note => (
        <li key={note.id} className="note-item">
          <p>{note.description}</p>
          <p>Created at: {note.createdAt}</p>
          <p>Edited at: {note.editedAt}</p>
          <button onClick={() => handleEdit(note)} className="edit">Edit</button>
          <button onClick={() => handleDelete(note.id)} className="delete">Delete</button>
        </li>
      ))}
    </ul>

    <form onSubmit={handleSubmit} className="note-form">
      <textarea
        name="description"
        value={formNote.description}
        onChange={handleChange}
        placeholder="Write a note..."
        required
      />
      <button type="submit">
        {formNote.id === 0 ? 'Add Note' : 'Update Note'}
      </button>


      {/* Conditionally show modal */}
      {noteToDelete !== null && (
        <ConfirmModal
          message="Are you sure you want to delete this note?"
          onConfirm={confirmDelete}
          onCancel={cancelDelete}
        />
      )}

    </form>
  </div>
);
}

export default NoteForm;

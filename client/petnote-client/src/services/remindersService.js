// src/services/reminderApi.js
const BASE_URL = 'http://localhost:8080/api/reminders';

export async function getRemindersByPetId(petId) {
  const res = await fetch(`${BASE_URL}/pet/${petId}`);
  return res.ok ? res.json() : [];
}

export async function createReminder(reminder) {
  const res = await fetch(BASE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reminder),
  });
  return res.ok ? res.json() : Promise.reject(await res.json());
}

export async function updateReminder(id, reminder) {
  const res = await fetch(`${BASE_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reminder),
  });
  return res.ok;
}

export async function deleteReminder(id) {
  const res = await fetch(`${BASE_URL}/${id}`, { method: 'DELETE' });
  return res.ok;
}

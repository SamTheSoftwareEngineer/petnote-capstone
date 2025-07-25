const BASE_URL = "http://localhost:8080/api/pet";

export async function getPetsByUserId(userId) {
  const response = await fetch(`${BASE_URL}/user/${userId}`);
  if (!response.ok) throw new Error("Failed to fetch pets");
  return await response.json();
}

export async function createPet(pet) {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(pet),
  });
  if (!response.ok) throw new Error("Failed to create pet");
  return await response.json();
}

export async function updatePet(pet) {
  const response = await fetch(`${BASE_URL}/${pet.id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(pet),
  });
  if (!response.ok) throw new Error("Failed to update pet");
  return await response.json();
}

export async function deletePet(id) {
  const response = await fetch(`${BASE_URL}/${id}`, { method: "DELETE" });
  if (!response.ok) throw new Error("Failed to delete pet");
}

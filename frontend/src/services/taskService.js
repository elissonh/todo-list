const API_URL = import.meta.env.VITE_API_URL;

export async function fetchTasks() {
  const res = await fetch(`${API_URL}/tasks`);
  if (!res.ok) throw new Error('Failed to fetch tasks.');
  const data = await res.json();
  return data.content;
}

export async function createTask(description) {
    console.log(`${API_URL}/tasks`)
    const res = await fetch(`${API_URL}/tasks`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ description, created_at: "2026-06-04T22:22", done: false }),
    });
    if (!res.ok) throw new Error('Failed to create task');
    return res.json();
}

export async function toggleTask(id, done) {
  const res = await fetch(`${API_URL}/tasks/${id}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ done }),
  });
  if (!res.ok) throw new Error('Failed to update task');
  return res.json();
}

export async function deleteTask(id) {
  const res = await fetch(`${API_URL}/tasks/${id}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Failed to delete task');
}
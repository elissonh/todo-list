import { useState, useEffect } from 'react'
import AddIcon from '@mui/icons-material/Add';
import './App.css'
import Task from './components/Task.jsx';
import { fetchTasks, createTask, toggleTask, deleteTask } from './services/taskService';

function App() {
  const [inputValue, setInputValue] = useState('');
  const [tasks, setTasks] = useState([]);
  const suggestions = ['Lavar a louça', 'Ler um livro', 'Limpar o quarto', 'Fazer exercício'];
  const [suggestionIndex, setSuggestionIndex] = useState(0);

  useEffect(() => {
    fetchTasks()
      .then(data => setTasks(data))
      .catch(err => console.error(err));
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      setSuggestionIndex(i => (i + 1) % suggestions.length);
    }, 1500);

    return () => clearInterval(interval); // 👈 cleanup on unmount
  }, []);

  async function addTask() {
    if (inputValue.trim() === '') return;
    try {
      const newTask = await createTask(inputValue);
      setTasks(tasks => [...tasks, newTask]);
      setInputValue('');
    } catch (err) {
      console.error(err);
    }
  }

  async function handleToggleTask(id) {
    const task = tasks.find(t => t.id === id);
    try {
      const updatedTask = await toggleTask(id, !task.done);
      setTasks(tasks => tasks.map(t => t.id === id ? updatedTask : t));
    } catch (err) {
      console.error(err);
    }
  }

  async function handleTaskDelete(id) {
    try {
      await deleteTask(id);
      setTasks(tasks => tasks.filter(t => t.id !== id));
    } catch (err) {
      console.error(err);
    }
  }

  return (
    <>
      <main id="container">
        <h1>Tarefas</h1>
        <section className='input-container'>
          <input
            type="text"
            name="task-input"
            id="task-input"
            placeholder={suggestions[suggestionIndex]}
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && addTask()}
          />
          <button className='add-task-button' onClick={addTask}>
            <AddIcon />
          </button>
        </section>
        <section id="tasks-container" className='tasks-container'>
          {tasks.map(task =>
            <Task
              key={task.id}
              id={task.id}
              isDone={task.done}
              onToggle={handleToggleTask}
              onDelete={handleTaskDelete}>
              {task.description}
            </Task>
          )}
        </section>
      </main>
    </>
  )
}

export default App
import { useState } from 'react'
import AddIcon from '@mui/icons-material/Add';

import './App.css'

function App() {

  return (
    <>
      <main id="container">
        <h1>Tarefas</h1>
        <section className='input-container'>
          <input type="text" name="task-input" id="task-input" placeholder='Lavar louça'/>
          <button className='add-task-button'>
            <AddIcon />
          </button>
        </section>
        <section id="tasks-container" className='tasks-container'>
          
        </section>
      </main>
    </>
  )
}

export default App

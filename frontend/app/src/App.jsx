import { RouterProvider } from 'react-router-dom'
import { useState } from 'react'
import router from './router'
import './App.css'

function App() {
  return (
    <RouterProvider router={router}></RouterProvider>
  )
}

export default App

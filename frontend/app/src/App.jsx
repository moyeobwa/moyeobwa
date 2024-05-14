import { RouterProvider } from 'react-router-dom'
import router from './router'
import './App.css'
import { GroupProvider } from './context/GroupContext'

function App() {
  return (
    <GroupProvider>
      <RouterProvider router={router}></RouterProvider>
    </GroupProvider>
  )
}

export default App

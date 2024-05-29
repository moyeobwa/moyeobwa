import React, { useContext, useState } from 'react';
import './TodoEdit.css'; 
import { AiOutlineClose } from 'react-icons/ai';
import ColorRadio from './ColorRadio';

const TodoEdit = ({
  todo,
  month,
  updateTodoItem,
  schedule,
  handleEditFalse,
  handleTodo,
}) => {
  const [color, setColor] = useState(todo.color);
  const [title, setTitle] = useState(todo.title);
  const [description, setDescrpition] = useState(todo.description);
  const [time, setTime] = useState(todo.time);

  const updateSchedule = (e) => {
    e.preventDefault();
    if (title === '') {
      return;
    }

    const newTodo = {
      id: todo.id,
      date: todo.date,
      color: `${color}`,
      title: `${title}`,
      description: `${description}`,
      time: `${time}`,
      idx: todo.idx,
    };

    updateTodoItem(month, newTodo);
    handleTodo(newTodo);
    handleEditFalse();
  };

  return (
    <div className="edit-form">
      <ColorRadio color={color} handleColor={setColor} />
      <div className="edit-header">
        <input
          type="text"
          className="edit-title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="title edit"
        />
        <input
          type="time"
          className="edit-time"
          value={time}
          onChange={(e) => setTime(e.target.value)}
        />
      </div>
      <textarea
        className="edit-description"
        rows="5"
        placeholder="description"
        value={description}
        onChange={(e) => setDescrpition(e.target.value)}
      />
      <div className="edit-btnBox">
        <AiOutlineClose className="close" onClick={handleEditFalse} />
        <button onClick={updateSchedule} className="edit-updateBtn">
          수정
        </button>
      </div>
    </div>
  );
}

export default TodoEdit;
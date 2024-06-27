import React, { useContext } from 'react';
import { BsFillPencilFill, BsFillTrashFill } from 'react-icons/bs';
import './TodoItem.css'; 

const TodoItem = ({
  todo,
  month,
  deleteTodoItem,
  handleTodo,
  handleEditTrue,
}) => {

  const selectedColor =
    todo.color === 'PINK'
      ? '#ff8f8f'
      : todo.color === 'YELLOW'
      ? '#fbde7e'
      : '#8cbc59';
  return (
    <div className="list-todoBox" data-testid="todoItem">
      <div
        className="list-titleBox"
        onClick={() => {
          handleTodo(todo);
        }}
      >
        <div
          className="list-color"
          style={{ backgroundColor: selectedColor }}
        ></div>
        <p className="list-title">{todo.title}</p>
      </div>
      <div className="list-btnBox">
        <BsFillPencilFill
          className="list-edit"
          onClick={() => {
            handleTodo(todo);
            handleEditTrue();
          }}
          data-testid="modify"
        />
        <BsFillTrashFill
          className="list-delete"
          onClick={() => {
            deleteTodoItem(month, todo.id);
          }}
        />
      </div>
    </div>
  );
}

export default TodoItem;
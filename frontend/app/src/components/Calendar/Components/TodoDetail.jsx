import React from 'react';
import './TodoDetail.css'; 
import { BsFillPencilFill, BsFillTrashFill } from 'react-icons/bs';
import { AiOutlineClose } from 'react-icons/ai';

const TodoDetail = ({
  todo,
  month,
  closeDetail,
  deleteTodoItem,
  handleEditTrue,
}) => {
  const selectedColor =
    todo.color === 'PINK'
      ? '#ff8f8f'
      : todo.color === 'YELLOW'
      ? '#fbde7e'
      : '#8cbc59';

  return (
    <div className="detail-todoBox">
      <div className="detail-header">
        <h2 className="detail-title" style={{ backgroundColor: selectedColor }}>
          {todo.title}
        </h2>
        <p className="detail-time">{todo.time}</p>
      </div>
      <p className="detail-description">{todo.content}</p>
      <div className="detail-btnBox">
        <AiOutlineClose
          className="detail-close"
          onClick={closeDetail}
          data-testid="closeDetail"
        />
        <div className="detail-itemBtnBox">
          <BsFillPencilFill
            className="detail-edit"
            onClick={() => {
              handleEditTrue();
            }}
          />
          <BsFillTrashFill
            className="detail-delete"
            onClick={() => {
              deleteTodoItem(month, todo.id);
              closeDetail();
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default TodoDetail;
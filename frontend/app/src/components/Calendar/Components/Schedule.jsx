import React, { useState, useEffect } from 'react';
import moment from 'moment';
import { AiOutlinePlus } from 'react-icons/ai';
import './Schedule.css';
import TodoItem from './TodoItem';
import TodoDetail from './TodoDetail';
import TodoEdit from './TodoEdit';

const Schedule = ({
  date,
  openModal,
  schedule,
  deleteTodoItem,
  isList,
  selectedTodo,
  handleTodo,
  closeDetail,
  updateTodoItem,
}) => {
  const [isEdit, setIsEdit] = useState(false);

  useEffect(() => {
    
  }, [schedule]);

  const handleEditTrue = () => {
    setIsEdit(true);
  };

  const handleEditFalse = () => {
    setIsEdit(false);
  };

  const month = date.getMonth() + '월';

  const scheduleList = Object.keys(schedule).includes(`${date.getMonth()}월`)
    ? schedule[month]
        .filter((todo) => todo.date === moment(date).format('YYYY-MM-DD'))
        .sort((a, b) => a.idx - b.idx)
    : [];

    

  return (
    <div className="schedule-container">
      <div className="schedule-header">
        <p className="schedule-title">Schedule</p>
        <div className="schedule-dateBox">
          <p className="schedule-date">
            {moment(date).format('YYYY년 MM월 DD일')}
          </p>
          <button
            className="schedule-addBtn"
            onClick={openModal}
            aria-label="addBtn"
          >
            <AiOutlinePlus />
          </button>
        </div>
      </div>
      <div className="schedule-scheduleBox">
        {isList &&
          scheduleList.map((todo) => (
            <TodoItem
              key={todo.id}
              todo={todo}
              deleteTodoItem={deleteTodoItem}
              handleTodo={handleTodo}
              handleEditTrue={handleEditTrue}
            />
          ))}
        {!isList && !isEdit && (
          <TodoDetail
            todo={selectedTodo}
            closeDetail={closeDetail}
            deleteTodoItem={deleteTodoItem}
            handleEditTrue={handleEditTrue}
          />
        )}
        {!isList && isEdit && (
          <TodoEdit
            todo={selectedTodo}
            updateTodoItem={updateTodoItem}
            handleEditFalse={handleEditFalse}
            handleTodo={handleTodo}
          />
        )}
      </div>
    </div>
  );
};

export default Schedule;

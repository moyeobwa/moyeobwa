import React, { useContext, useEffect, useState } from 'react';
import CalendarBox from './Components/CalendarBox';
import Schedule from './Components/Schedule';
import TodoAddModal from './Components/TodoAddModal';
import './Calendar.css';

export default function Calendar() {
  const [date, setDate] = useState(new Date());
  const [modal, setModal] = useState(false);
  const [schedule, setSchedule] = useState(() => loadData());

  useEffect(() => {
    localStorage.setItem('schedule', JSON.stringify(schedule));
  }, [schedule]);

  // 일정 클릭 시 일정의 상세 페이지로 이동하는 기능
  const [isList, setIsList] = useState(true);
  const [selectedTodo, setSelectedTodo] = useState({});
  const handleTodo = (todo) => {
    setSelectedTodo(todo);
    setIsList(false);
  };
  const closeDetail = () => {
    setIsList(true);
  };

  const openModal = () => {
    setModal(true);
  };
  const closeModal = () => {
    setModal(false);
  };
  const deleteTodoItem = (month, id) => {
    const newList = schedule[month].filter((todo) => todo.id !== id);
    setSchedule((prev) => ({
      ...prev,
      [month]: newList,
    }));
  };
  const updateTodoItem = (month, newTodo) => {
    const newList = schedule[month]
      .filter((todo) => todo.id !== newTodo.id)
      .concat(newTodo);
    setSchedule((prev) => ({
      ...prev,
      [month]: newList,
    }));
  };

  const [color, setColor] = useState('pink');
  const handleColor = (color) => setColor(color);
  const [colorTab, setColorTab] = useState(false);


  return (
    <div
      className="back"
    >
      <div className="box">
        <CalendarBox
          date={date}
          handleDate={setDate}
          schedule={schedule}
          closeDetail={closeDetail}
        />
        <Schedule
          date={date}
          openModal={openModal}
          schedule={schedule}
          deleteTodoItem={deleteTodoItem}
          isList={isList}
          selectedTodo={selectedTodo}
          handleTodo={handleTodo}
          closeDetail={closeDetail}
          updateTodoItem={updateTodoItem}
        />
        <TodoAddModal
          open={modal}
          date={date}
          closeModal={closeModal}
          schedule={schedule}
          addSchedule={setSchedule}
        />
      </div>
    </div>
  );
}

function loadData() {
  const schedule = JSON.parse(localStorage.getItem('schedule'));
  return schedule ? schedule : {};
}

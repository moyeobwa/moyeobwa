import React, { useEffect, useState } from 'react';
import CalendarBox from './Components/CalendarBox';
import Schedule from './Components/Schedule';
import TodoAddModal from './Components/TodoAddModal';
import axios from 'axios';
import './Calendar.css';

export default function Calendar({ gatheringId }) {
  const [date, setDate] = useState(new Date());
  const [modal, setModal] = useState(false);
  const [schedule, setSchedule] = useState({});
  const [miniSchedule, setMiniSchedule] = useState({});
  const [isList, setIsList] = useState(true);
  const [selectedTodo, setSelectedTodo] = useState({});
  
  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    const fetchSchedules = async () => {
      try {
        const response = await axios.get(`${apiUrl}/api/v1/schedules/${gatheringId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        const formattedSchedule = formatSchedule(response.data);
        const formattedMiniSchedule = formatMiniSchedule(response.data);
        setSchedule(formattedSchedule);
        setMiniSchedule(formattedMiniSchedule);
      } catch (error) {
        if (error.response.status === 404) {
          alert('모임 정보를 찾을 수 없습니다.');
        } else {
          console.error(error);
          alert('네트워크 오류가 발생했습니다.');
        }
      }
    };
    fetchSchedules();
  }, [gatheringId]);

  const formatSchedule = (data) => {
    const formatted = {};
    data.forEach((item) => {
      const month = `${new Date(item.date).getMonth()}월`;

      if (!formatted[month]) {
        formatted[month] = [];
      }
      formatted[month].push(item);
    });
    return formatted;
  };

  const formatMiniSchedule = (data) => {
    const formatted = {};
    data.forEach((item) => {
      const dateKey = item.date.substring(0, 10);
      if (!formatted[dateKey]) {
        formatted[dateKey] = [];
      }
      formatted[dateKey].push(item);
    });
    return formatted;
  };

  
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

  const deleteTodoItem = async (month, id) => {
    try {
      const response = await axios.delete(`${apiUrl}/api/v1/schedules/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.status === 200) {
        const month = date.getMonth() + '월';

        const monthSchedule = schedule[month].filter((todo) => todo.id !== id);
        setSchedule((prev) => ({
          ...prev,
          [month]: monthSchedule,
        }));
        alert("일정이 삭제되었습니다.");
      }
    } catch (error) {
      if (error.response.status === 400){
        alert('일정과 사용자 정보가 일치하지 않습니다.');
      } else if (error.response && error.response.status === 404) {
        alert('사용자 또는 일정 정보를 찾을 수 없습니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };
  

  const updateTodoItem = async (month, updatedTodo) => {
    try {
      const response = await axios.post(`${apiUrl}/api/v1/schedules/${updatedTodo.id}`, updatedTodo, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.status === 200) {
        const month = date.getMonth() + '월';

        updatedTodo.idx = schedule[month].length + 1;
        const monthSchedule = schedule[month]
          .filter((todo) => todo.id !== updatedTodo.id)
          .concat(updatedTodo);
        setSchedule((prev) => ({
          ...prev,
          [month]: monthSchedule,
        }));
      }
    } catch (error) {
      if (error.response.status === 400){
        alert('일정과 사용자 정보가 일치하지 않습니다.');
      } else if (error.response && error.response.status === 404) {
        alert('사용자 또는 일정 정보를 찾을 수 없습니다.');
      } else {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    }
  };

  return (
    <div className="back">
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
          gatheringId={gatheringId}
        />
      </div>
    </div>
  );
}

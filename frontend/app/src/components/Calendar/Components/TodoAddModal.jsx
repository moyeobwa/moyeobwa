import { useState } from 'react';
import moment from 'moment';
import './TodoAddModal.css'; 
import ColorRadio from './ColorRadio';
import Button from '../../Button';
import { AiOutlineClose } from 'react-icons/ai';
import axios from 'axios';

const TodoAddModal = ({
  date,
  open,
  closeModal,
  schedule,
  addSchedule,
  gatheringId
}) => {
  const [color, setColor] = useState('pink');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [time, setTime] = useState('09:00');
  const [activeTab, setActiveTab] = useState('Messages');
  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  const createSchedule = async (e) => {
    e.preventDefault();

    if (title === '') {
      return;
    }

    const formattedDate = moment(date).format('YYYY-MM-DD');
    const formattedTime = time + ':00';

    const newSchedule = {
      color: color.toUpperCase(),
      title,
      content: description,
      date: formattedDate,
      time: formattedTime,
      gatheringId,
    };

    try {
      const response = await axios.post(`${apiUrl}/api/v1/schedules`, newSchedule, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });

      if (response.status === 200) {
        const month = date.getMonth() + '월';
        const newTodo = response.data;

        if (Object.keys(schedule).includes(`${date.getMonth()}월`)) {
          newTodo.idx = schedule[month].length + 1;
          const monthSchedule = schedule[month].concat(newTodo);
          addSchedule((prev) => ({
            ...prev,
            [month]: monthSchedule,
          }));
        } else {
          addSchedule((prev) => ({
            ...prev,
            [month]: [newTodo],
          }));
        }

        setColor('pink');
        setTitle('');
        setDescription('');
        setTime('00:00');
        closeModal();
        alert("일정이 추가되었습니다.");
      }
    } catch (error) {
      console.error('Failed to create schedule', error);
    }
  };

  return (
    <div className={open ? 'modal openModal' : 'modal-calendar'}>
      <div className='add_form'>
        <div className='add_infoBox'>
          <h2 className='add_info'>일정 등록</h2>
          <AiOutlineClose className='add_closeBtn' onClick={closeModal} />
        </div>
        <ColorRadio color={color} handleColor={setColor} />
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className='add_title'
          placeholder="title"
        />
        <textarea
          className='add_description'
          rows="5"
          cols="33"
          placeholder="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <input
          type="time"
          className='add_time'
          value={time}
          onChange={(e) => setTime(e.target.value)}
        />
        <Button onClick={createSchedule} text={"등록"}/>
      </div>
    </div>
  );
}

export default TodoAddModal;

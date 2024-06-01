import React, { useState } from 'react';
import moment from 'moment';
import './TodoAddModal.css'; 
import ColorRadio from './ColorRadio';
import Button from '../../Button'
import { AiOutlineClose } from 'react-icons/ai';
import { v4 as uuidv4 } from 'uuid';

const TodoAddModal = ({
  date,
  open,
  closeModal,
  schedule,
  addSchedule,
}) => {
  const [color, setColor] = useState('pink');
  const [title, setTitle] = useState('');
  const [description, setDescrpition] = useState('');
  const [time, setTime] = useState('09:00');

  const createSchedule = (e) => {
    e.preventDefault();

    if (title === '') {
      return;
    }

    const month = date.getMonth() + '월';
    const newTodo = {
      id: `${uuidv4()}`,
      date: `${moment(date).format('YYYY년 MM월 DD일')}`,
      color: `${color}`,
      title: `${title}`,
      description: `${description}`,
      time: `${time}`,
      idx: 1,
    };

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
    setDescrpition('');
    setTime('00:00');
    closeModal();
  };

  return (
    <div className={open ? 'modal openModal' : 'modal'}>
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
          onChange={(e) => setDescrpition(e.target.value)}
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
import React from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './CalendarBox.css';
import moment from 'moment';

const CalendarBox = ({
  date: selectedDate,
  handleDate,
  schedule,
  closeDetail,
}) => {
  const show = ({ date, view }) => {
    const miniFormatted = convertScheduleToMiniSchedule(schedule);

    if (view === 'month') {
      const dateKey = moment(date).format('YYYY-MM-DD');
      const scheduleList = miniFormatted[dateKey] || [];

      const korean = /[ㄱ-ㅎ|ㅏ-ㅣ-가-힣]/;
      return (
        <div className="scheduleBox">
          {scheduleList.map((todo) => (
            <div
              key={todo.id}
              className="scheduleItem"
              style={{ backgroundColor: getColor(todo.color) }}
              onClick={() => closeDetail()}
            >
              {korean.test(todo.title) && todo.title.length > 4
                ? `${todo.title.substring(0, 4)}..`
                : todo.title.length > 5
                ? `${todo.title.substring(0, 5)}..`
                : todo.title}
            </div>
          ))}
        </div>
      );
    }
  };

  const convertScheduleToMiniSchedule = (schedule) => {
    const miniFormatted = {};
    for (const month in schedule) {
      schedule[month].forEach((item) => {
        const dateKey = item.date.substring(0, 10);
        if (!miniFormatted[dateKey]) {
          miniFormatted[dateKey] = [];
        }
        miniFormatted[dateKey].push(item);
      });
    }
    return miniFormatted;
  };

  const getColor = (color) => {
    switch (color.toUpperCase()) {
      case 'YELLOW':
        return '#fbde7e';
      case 'GREEN':
        return '#8cbc59';
      default:
        return '#ff8f8f';
    }
  };

  return (
    <div className="container">
      <Calendar
        onChange={handleDate}
        value={selectedDate}
        formatDay={(locale, date) =>
          date.toLocaleString('en', { day: 'numeric' })
        }
        next2Label={null}
        prev2Label={null}
        tileContent={show}
        onClickDay={closeDetail} // 날짜 클릭 시 일정 상세 뷰 닫기
        className="white"
      />
    </div>
  );
};

export default CalendarBox;

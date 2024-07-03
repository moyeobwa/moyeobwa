import './MyGatheringList.css';
import { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import axios from 'axios';

const MyGatheringList = () => {
  const nav = useNavigate();
  const [gatheringList, setGatheringList] = useState([]);
  const token = localStorage.getItem('token');
  const apiUrl = import.meta.env.VITE_API_BASE_URL;

  useEffect(() => {
    const fetchGatheringList = async () => {
      try {
        const response = await axios.get(`${apiUrl}/api/v1/gatherings/user`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          setGatheringList(response.data);
        }
      } catch (error) {
        console.error(error);
        alert('네트워크 오류가 발생했습니다.');
      }
    };

    fetchGatheringList();
  }, [token]);

  const goGatheringPage = (id) => {
      nav(`/gathering/${id}`)
  }

  return (
    <div className="gathering_list">
      <h3>내 모임</h3>
      <ul>
        {gatheringList.map((gathering) => (
            <li key={gathering.id} onClick={() => goGatheringPage(gathering.id)}>
              {gathering.name}
            </li>
        ))}
      </ul>
    </div>
  );
}

export default MyGatheringList;
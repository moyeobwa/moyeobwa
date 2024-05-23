import './MyGatheringList.css';
import { useNavigate } from "react-router-dom";

const myGathering = [
  {
    id:11,
    title:"소울 휘트니스 헬스 모임"
  },
  {
    id:12,
    title:"스파르타 코딩 모임"
  }
]

const MyGatheringList = () => {
  const nav = useNavigate();

  const goGatheringPage = (id) => {
      nav(`/gathering/${id}`)
  }

  return (
    <div className="gathering_list">
      <h3>내 모임</h3>
      <ul>
        {myGathering.map((gathering) => (
            <li key={gathering.id} onClick={() => goGatheringPage(gathering.id)}>
              {gathering.title}
            </li>
        ))}
      </ul>
    </div>
  );
}

export default MyGatheringList;
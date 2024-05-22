import './MyGatheringList.css';

const MyGatheringList = () => {
  return (
    <div className="gathering_list">
      <h3>내 모임</h3>
      <ul>
        <li>소울 휘트니스 헬스 모임</li>
        <li>스파르타 코딩 모임</li>
        {/* 다른 모임 항목 추가 */}
      </ul>
    </div>
  );
}

export default MyGatheringList;
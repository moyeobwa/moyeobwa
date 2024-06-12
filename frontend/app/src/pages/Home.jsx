import { useState, useRef } from 'react';
import { useNavigate } from "react-router-dom";
import Header from '../components/Header';
import UserSidebar from '../components/UserSidebar';
import Card from '../components/Card';
import Radio from '../components/Radio';
import './Home.css';

const gatheringData = [
    {
        id:1,
        title: "사진 모임",
        describe: "사진 좋아하는 사람들 모이세요",
        memberCnt: 15,
        date: new Date(2024, 4, 23, 0, 0, 0).getTime()
    },
    {
        id:2,
        title: "운동 모임",
        describe: "운동 좋아하는 사람들 모이세요",
        memberCnt: 20,
        date: new Date(2024, 4, 15, 0, 0, 0).getTime()
    },
    {
        id:3,
        title: "독서 모임",
        describe: "독서 좋아하는 사람들 모이세요",
        memberCnt: 10,
        date: new Date(2024, 3, 12, 0, 0, 0).getTime()
    },
    {
        id:4,
        title: "게임 모임",
        describe: "게임 좋아하는 사람들 모이세요",
        memberCnt: 5,
        date: new Date(2024, 4, 19, 0, 0, 0).getTime()
    },
    {
        id:5,
        title: "등산 모임",
        describe: "등산 좋아하는 사람들 모이세요",
        memberCnt: 8,
        date: new Date(2024, 4, 25, 0, 0, 0).getTime()
    },
]

const Home = () => {
    const [sortType, setSortType] = useState("latest");
    const navigate = useNavigate();
    const onChangeSortType = (e) => {
        setSortType(e.target.value);
    };

    const scrollContainerRef = useRef(null);

    const handleWheelScroll = (event) => {
        const scrollAmount = event.deltaY;
        scrollContainerRef.current.scrollLeft += scrollAmount;
    };

    const getSortedGatheringData = () => {
        return gatheringData.toSorted((a, b) => {
          if (sortType === "memberOrder") {
            return Number(b.memberCnt) - Number(a.memberCnt);
          } else if (sortType === "recency") {
            return b.date - a.date;
          } else {
            return Number(a.id) - Number(b.id);
          }
        });
      };

    const sortedGatheringData = getSortedGatheringData();

    return (
        <div>
            <div className="home_">
                <Header />
            </div>
            <div className="main_content">
                <div className="sidebar">
                    <UserSidebar />
                </div>
                <div className="card_section">
                    <div className="title_section">
                        <h2>이런 모임은 어떠세요?</h2>
                        <div className="radio-inputs">
                            <label className="radio">
                            <input 
                                type="radio" 
                                name="radio" 
                                value="memberOrder" 
                                checked={sortType === "memberOrder"} 
                                onChange={onChangeSortType} 
                            />
                            <span className="name">모임원 순</span>
                            </label>
                            <label className="radio">
                            <input 
                                type="radio" 
                                name="radio" 
                                value="recency" 
                                checked={sortType === "recency"} 
                                onChange={onChangeSortType} 
                                />
                                <span className="name">최근 활동 순</span>
                            </label>
                        </div>
                    </div>
                    <div
                        className="container scroll-1"
                        onWheel={handleWheelScroll}
                        ref={scrollContainerRef}
                    >
                        {sortedGatheringData.map((gathering) => (
                            <div key={gathering.id} onClick={() => navigate(`/gathering/${gathering.id}`)}>
                                <Card {...gathering} />
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
        
    );
};

export default Home;

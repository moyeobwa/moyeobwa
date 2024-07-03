import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Header from '../components/Header';
import UserSidebar from '../components/UserSidebar';
import Card from '../components/Card';
import './Home.css';

const Home = () => {
    const [gatheringData, setGatheringData] = useState([]);
    const [sortType, setSortType] = useState("LATEST");
    const [cursor, setCursor] = useState(null);
    const [hasNext, setHasNext] = useState(true);
    const scrollContainerRef = useRef(null);
    const tempCursor = useRef(0);

    const token = localStorage.getItem('token');
    const apiUrl = import.meta.env.VITE_API_BASE_URL;

    useEffect(() => {
        fetchGatherings();
    }, [sortType, cursor]);

    const fetchGatherings = async () => {
        try {
            const response = await axios.get(`${apiUrl}/api/v1/gatherings`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                params: {
                    sortType,
                    ...(cursor && { cursor }),
                    pageSize: 3
                }
            });
            
            const newGatherings = response.data.values;
            tempCursor.current  = response.data.cursor;
            if (cursor) {
                setGatheringData(prevData => {
                    const existingIds = prevData.map(g => g.id);
                    const filteredNewGatherings = newGatherings.filter(g => !existingIds.includes(g.id));
                    return [...prevData, ...filteredNewGatherings];
                });
            } else {
                setGatheringData(newGatherings);
            }
            setHasNext(response.data.hasNext);
        } catch (error) {
            console.error('모임 데이터를 가져오는데 실패했습니다:', error);
        }
    };

    const onChangeSortType = (e) => {
        setSortType(e.target.value);
        setCursor(null);
    };

    const handleWheelScroll = (event) => {
        const scrollAmount = event.deltaY;
        scrollContainerRef.current.scrollLeft += scrollAmount;
    };

    const loadMoreGatherings = () => {
        if (hasNext) {
            setCursor(tempCursor.current);
        }
    };

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
                                    value="MEMBER_COUNT" 
                                    checked={sortType === "MEMBER_COUNT"} 
                                    onChange={onChangeSortType} 
                                />
                                <span className="name">모임원 순</span>
                            </label>
                            <label className="radio">
                                <input 
                                    type="radio" 
                                    name="radio" 
                                    value="LATEST" 
                                    checked={sortType === "LATEST"} 
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
                        {Array.isArray(gatheringData) && gatheringData.map((gathering) => (
                            <a href={`/gathering/${gathering.id}`} key={gathering.id}>
                                <Card {...gathering} />
                            </a>
                        ))}
                        {hasNext && (
                            <button onClick={loadMoreGatherings} className="load-more-button">
                                더보기
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;

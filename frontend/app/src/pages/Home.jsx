import { useRef } from 'react';
import Header from '../components/Header';
import UserSidebar from '../components/UserSidebar';
import Card from '../components/Card';
import Radio from '../components/Radio';
import './Home.css';

const Home = () => {
    const scrollContainerRef = useRef(null);

    const handleWheelScroll = (event) => {
        const scrollAmount = event.deltaY;
        scrollContainerRef.current.scrollLeft += scrollAmount;
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
                        <Radio/>
                    </div>
                    <div
                        className="container scroll-1"
                        onWheel={handleWheelScroll}
                        ref={scrollContainerRef}
                    >
                        <Card />
                        <Card />
                        <Card />
                        <Card />
                        <Card />
                        <Card />
                        <Card />
                        <Card />
                        <Card />
                        <Card />
                    </div>
                </div>
            </div>
        </div>
        
    );
};

export default Home;

import './Radio.css';

const Radio = ({ sortType, onChangeSortType }) => {

  return (
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
  );
}

export default Radio;
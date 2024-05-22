import './Radio.css';

const Radio = () => {
  return (
    <div className="radio-inputs">
        <label className="radio">
            <input type="radio" name="radio" checked={true}/>
            <span className="name">모임원 순</span>
        </label>
        <label className="radio">
            <input type="radio" name="radio"/>
            <span className="name">최근 활동 순</span>
        </label>
    </div>
  );
}

export default Radio;
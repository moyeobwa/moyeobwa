import './ColorRadio.css'; 
const ColorRadio = ({ color, handleColor, isTheme }) => {
  return (
    <div className={`color-box ${isTheme ? 'theme' : ''}`}>
      <div
        className={`pink color ${color === 'pink' ? 'selected' : ''} ${
          isTheme ? 'themeColor' : ''
        }`}
        onClick={() => handleColor('pink')}
      ></div>
      <div
        className={`yellow color ${color === 'yellow' ? 'selected' : ''} ${
          isTheme ? 'themeColor' : ''
        }`}
        onClick={() => handleColor('yellow')}
      ></div>
      <div
        className={`green color ${color === 'green' ? 'selected' : ''} ${
          isTheme ? 'themeColor' : ''
        }`}
        onClick={() => handleColor('green')}
      ></div>
    </div>
  );
}

export default ColorRadio;
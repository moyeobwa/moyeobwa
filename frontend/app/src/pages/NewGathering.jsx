import Editor from "../components/Gathering/Editor";
import { useGroupContext } from "../context/GroupContext";
import Header from "../components/Header";
const NewGathering = () => {

    const { addGroup } = useGroupContext();

    return (
         <div className="addGroup">
            <Header />
            <Editor />
         </div>
    );
}

export default NewGathering;